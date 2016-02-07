/*
 * Copyright 2015 Jett Marks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by jett on 12/28/15.
 */
package com.clueride.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Point;
import org.apache.log4j.Logger;

import com.clueride.dao.NetworkProposalStore;
import com.clueride.dao.NodeStore;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkProposal;
import com.clueride.domain.dev.NetworkRecommendation;
import com.clueride.domain.dev.NewNodeProposal;
import com.clueride.domain.dev.rec.OnSegment;
import com.clueride.domain.dev.rec.OnTrack;
import com.clueride.domain.dev.rec.Rec;
import com.clueride.domain.dev.rec.ToNode;
import com.clueride.domain.dev.rec.ToSegment;
import com.clueride.domain.dev.rec.ToSegmentAndNode;
import com.clueride.domain.dev.rec.ToTwoNodes;
import com.clueride.domain.dev.rec.ToTwoSegments;
import com.clueride.feature.Edge;
import com.clueride.feature.TrackFeature;
import com.clueride.service.builder.NewLocRecBuilder;
import com.clueride.service.builder.OnSegmentRecBuilder;
import com.clueride.service.builder.TrackRecBuilder;

/**
 * Implementation of the Recommendation Service supporting the creation
 * and evaluation of recommendations to add to the Network.
 */
public class DefaultRecommendationService implements RecommendationService {
    private static final Logger LOGGER = Logger.getLogger(DefaultRecommendationService.class);
    private final NodeStore nodeStore;
    private static int countBuildNewLocRequests;
    // TODO: Use the NetworkProposalStore; this isn't thread safe.
    private static NewNodeProposal lastProposal;

    @Inject
    public DefaultRecommendationService(NodeStore nodeStore) {
        this.nodeStore = nodeStore;
    }

    @Override
    public NetworkProposal buildProposalForNewNode(GeoNode newNode) {
        LOGGER.debug("start - buildProposalForNewNode(): "
                + countBuildNewLocRequests++);
        GeoEval geoEval = GeoEval.getInstance();
        NodeEval nodeEval = NodeEval.getInstance();
        NewNodeProposal newNodeProposal = new NewNodeProposal(newNode);

        // TODO: CA-23 - renaming Loc to Node (probably will deprecate the NewLocRecBuilder)
        NewLocRecBuilder recBuilder = new NewLocRecBuilder(newNode);

        // Check if our node happens to already be on the network list of nodes
        Integer matchingNodeId = nodeEval.matchesNetworkNodeId(newNode);
        // Check if our node happens to be on the network list of edges
        Integer matchingSegmentId = geoEval.matchesSegmentId(newNode);

        if (matchingNodeId > 0) {
            // Found matching node; add as a recommendation
            newNodeProposal.add(recBuilder.onNode(matchingNodeId));
        } else if (matchingSegmentId > 0) {
            Edge matchingEdge = geoEval.getMatchingEdge(newNode);
            OnSegmentRecBuilder onSegmentRecBuilder = new OnSegmentRecBuilder(newNode, matchingEdge);
            newNodeProposal.add(onSegmentRecBuilder.build());
        } else {
            // Try a Track-based proposal
            List<TrackFeature> coveringTracks = geoEval.listCoveringTracks(newNode);
            for (TrackFeature track : coveringTracks) {
                TrackRecBuilder trackRecBuilder = new TrackRecBuilder(newNode,
                        track);
                Rec rec = trackRecBuilder.build();
                if (rec != null) {
                    newNodeProposal.add(rec);
                }
            }
        }

        logProposal(newNodeProposal);
        collapseProposal(newNodeProposal);
        logProposal(newNodeProposal);
        lastProposal = newNodeProposal;
        return newNodeProposal;
    }

    @Override
    public String getRecGeometry(Integer recId) {
        for (NetworkRecommendation rec : lastProposal.getRecommendations()) {
            if (rec.getId().equals(recId)) {
                NewNodeProposal p = new NewNodeProposal(lastProposal.getNode());
                p.add(rec);
                return  p.toJson();
            }
        }
        return lastProposal.toJson();
    }

    /**
     * When there are multi-track recommendations, it is possible and maybe even likely that there are duplicate
     * recommendations coming from different underlying tracks; this removes the duplicates and ranks the various
     * proposals.
     * @param newNodeProposal - Proposal for adding new node that can contain multiple redundant recommendations.
     */
    private void collapseProposal(NewNodeProposal newNodeProposal) {
        Map<String, NetworkRecommendation> nodeMap = new HashMap<>();
        for (NetworkRecommendation rec : newNodeProposal.getRecommendations()) {
            PointKey pointKey = new PointKey(rec.getNodeList());
            if (nodeMap.containsKey(pointKey.getKey())) {
                // Duplicate Recommendation ?
                LOGGER.info("Rec " + rec.getId() + ": already encountered Point " + pointKey.getKey());
            } else {
                LOGGER.info("Rec " + rec.getId() + " has the PointKey " + pointKey.getKey());
                nodeMap.put(pointKey.getKey(), rec);
            }
        }

        // Sort results by number of ends and distance
        List<NetworkRecommendation> doubleEndedRecs = new ArrayList<>();
        List<NetworkRecommendation> singleEndedRecs = new ArrayList<>();

        for (NetworkRecommendation rec : nodeMap.values()) {
            if (rec.isDoubleEnded()) {
                doubleEndedRecs.add(rec);
            } else {
                singleEndedRecs.add(rec);
            }
        }
        Collections.sort(doubleEndedRecs, new Comparator<NetworkRecommendation>() {
            @Override
            public int compare(NetworkRecommendation networkRecommendation, NetworkRecommendation t1) {
                return Double.compare(t1.getScore(), networkRecommendation.getScore());
            }
        });
        Collections.sort(singleEndedRecs, new Comparator<NetworkRecommendation>() {
            @Override
            public int compare(NetworkRecommendation networkRecommendation, NetworkRecommendation t1) {
                return Double.compare(t1.getScore(), networkRecommendation.getScore());
            }
        });

        // Add sorted lists back to the original proposal
        newNodeProposal.resetRecommendationList();
        for (NetworkRecommendation rec : doubleEndedRecs) {
            newNodeProposal.add(rec);
        }
        for (NetworkRecommendation rec : singleEndedRecs) {
            newNodeProposal.add(rec);
        }
    }

    private void logProposal(NewNodeProposal newNodeProposal) {
        LOGGER.info("Proposal Summary: ");
        List<NetworkRecommendation> recommendations = newNodeProposal.getRecommendations();
        LOGGER.info("Total of " + recommendations.size() + " recommendations");
        for (NetworkRecommendation rec : recommendations) {
            String fullMessage = "Rec ID: " + rec.getId() +
                            " of type " + rec.getRecType() +
                            " has " + rec.getFeatureCount() + " features";
            if (rec instanceof OnTrack) {
                fullMessage += "; based on Track " + ((OnTrack) rec).getSourceTrackId();
            }
            LOGGER.info(fullMessage);
        }
    }

    /**
     * Implementation of a request to confirm the latest proposal as the one we
     * want to add to the network.
     *
     * The NetworkProposal instance we stashed away will hold the details needed
     * to create the objects we add to the persisted network.
     *
     * @param recId - Integer representing the unique recommendation to be committed
     *              to the network.
     * @return upon success, returns JSON status:OK.
     */
    @Override
    public String confirmRecommendation(Integer recId) {
        NetworkProposal networkProposal = NetworkProposalStore
                .getLastProposal();
        Rec rec = (Rec) networkProposal.getRecommendation(recId);
        if (rec != null) {
            switch (rec.getRecType()) {
                case ON_NODE:
                    LOGGER.warn("We don't have a way to select "+rec.getRecType());
                    break;

                case ON_SEGMENT:
                    splitSegmentAtNode((OnSegment) rec);
                    break;
                case TRACK_TO_NODE:
                    addTrackToNodeRec((ToNode) rec);
                    break;
                case TRACK_TO_SEGMENT:
                    addTrackToSegmentRec((ToSegment) rec);
                    break;
                case TRACK_TO_SEGMENT_AND_NODE:
                    addTrackToSegmentAndNodeRec((ToSegmentAndNode) rec);
                    break;
                case TRACK_TO_2_SEGMENTS:
                    addTrackToTwoSegmentsRec((ToTwoSegments) rec);
                    break;
                case TRACK_TO_2_NODES:
                    addTrackToTwoNodesRec((ToTwoNodes) rec);
                    break;

                case UNDEFINED:
                case OFF_NETWORK:
                default:
                    LOGGER.warn("Rec Type: " + rec.getRecType());
            }
        }
        DefaultNetwork.getInstance().storesReadyForPublishing();
        return "{\"status\": \"OK\"}";
    }

    /**
     * Similar to splitting a Node where a track comes into the existing network:
     * Adding the new node and replacing a single segment with the two edges resulting
     * from a split at the node's location.
     * <ul>
     * <li>The Location Node itself which is also the Splitting Node.
     * <li>The existing Network Edge/Segment where the proposed Edge meets the network.</li>
     * </ul>
     * We tell the SegmentService about the existing Network Edge and the
     * location where the new node is added; it creates the two new segments from
     * the existing segment.
     * @param rec - OnSegment instance with segment and splitting node.
     */
    private void splitSegmentAtNode(OnSegment rec) {
        LOGGER.info("From this Rec: " + rec);
        LOGGER.info("Preparing the following pieces to add to the Network:");

        Integer nodeId = nodeStore.addNew(rec.getNewNode());
        LOGGER.info("Splitting Node with ID: " + nodeId);

        SegmentService.splitSegment(rec.getSegment(), rec.getNewNode());
        rec.logRecommendationSummary();
    }

    /**
     * Prepares the following items for adding to the network:
     * <ul>
     *     <li>The New Node itself.</li>
     *     <li>The Edge connecting the Node to the Network Node.</li>
     * </ul>
     *
     * These are then persisted.
     * @param rec - ToNode instance containing the New Node and the new Edge
     *            which connects the New Node to the Network Node.
     */
    private void addTrackToNodeRec(ToNode rec) {
        LOGGER.info("From this Rec: "+rec);
        LOGGER.info("Preparing the following pieces to add to the Network:");
        LOGGER.info("New Loc: " + rec.getNewNode().getName());
        rec.logRecommendationSummary();

        Integer nodeId = nodeStore.addNew(rec.getNewNode());
        LOGGER.info("New Node with ID: " + nodeId);

        SegmentService.addSegment(rec.getProposedTrack());
    }

    /**
     * Prepares these items for adding to the network:
     * <ul>
     * <li>The Location Node itself
     * <li>The Edge connecting the Location to the Segment (from the Track)
     * <li>The existing Network Edge/Segment where the proposed Edge meets the network.</li>
     * <li>The Splitting Node (also the end of the previous Edge).
     * </ul>
     * We also tell the SegmentService about the existing Network Edge and the
     * location where we join in so the Edge can be split into two Edges creating
     * a three-way intersection at that Splitting Node.
     * These are then persisted.
     * @param rec - Containing specific elements to be added to the Network.
     */
    private void addTrackToSegmentRec(ToSegment rec) {
        LOGGER.info("From this Rec: " + rec);
        LOGGER.info("Preparing the following pieces to add to the Network:");

        // Two Nodes:
        Integer nodeId = nodeStore.addNew(rec.getNewNode());
        LOGGER.info("New Node with ID: " + nodeId);
        Integer splittingNodeId = nodeStore.addNew(rec.getSplittingNode());
        LOGGER.info("Splitting Node with ID: " + splittingNodeId);

        // Accepted Proposal for Edge/Segment from new node to the existing segment
        SegmentService.addSegment(rec.getProposedTrack());

        // Split of the Existing Network Node
        SegmentService.splitSegment(rec.getSegment(), rec.getSplittingNode());
        rec.logRecommendationSummary();
    }

    /**
     * Prepares the following items for adding to the network:
     * <ul>
     *     <li>The New Node itself.</li>
     *     <li>The Edge in one direction connecting the Node to a Network Node or Edge.</li>
     *     <li>The Edge in the other direction connecting the new Node to either a Network Node or Edge.</li>
     *     <li>The existing Network Edge/Segment where the proposed Edge meets the network.</li>
     *     <li>The Splitting Node where the proposed Edge meets the Network Edge.
     * </ul>
     * We also tell the SegmentService about the existing Network Edge and the
     * location where we join in so the Edge can be split into two Edges creating
     * a three-way intersection at that Splitting Node.
     * @param rec - Containing specific elements to be added to the Network.
     */
    private void addTrackToSegmentAndNodeRec(ToSegmentAndNode rec) {
        LOGGER.info("From this Rec: "+rec);
        LOGGER.info("Preparing the following pieces to add to the Network:");

        // Two Nodes:
        Integer nodeId = nodeStore.addNew(rec.getNewNode());
        LOGGER.info("New Node with ID: " + nodeId);
        Integer splittingNodeId = nodeStore.addNew(rec.getSplittingNode());
        LOGGER.info("Splitting Node with ID: " + splittingNodeId);

        // Accepted Proposal for Edge/Segment from new node to the existing segment
        SegmentService.addSegment(rec.getProposedTracks().get(0));
        SegmentService.addSegment(rec.getProposedTracks().get(1));

        // Split of the Existing Network Node
        SegmentService.splitSegment(rec.getSegment(), rec.getSplittingNode());
        rec.logRecommendationSummary();
    }

    /**
     * Prepares these items for adding to the network:
     * <ul>
     *     <li>The New Node itself.</li>
     *     <li>A new Edge in one direction from the new Node over to a Network Edge.</li>
     *     <li>A new Edge in the other direction from the new Node over to a Network Edge.</li>
     *     <li>The Splitting Node where the first Edge hits the Network</li>
     *     <li>The Splitting Node where the second Edge hits the Network</li>
     * </ul>
     * These are then persisted.
     * We also tell the SegmentService about the two existing Network Edges and the
     * location where we join in so those Edges can be split into two Edges creating
     * a three-way intersection at that Splitting Node.
     * @param rec - ToTwoSegments recommendation containing New Node, the two Edges
     *            leading in either direction toward two other existing Network Edges
     *            and the two Splitting Nodes, one for each Network Edge to be split.
     */
    private void addTrackToTwoSegmentsRec(ToTwoSegments rec) {
        LOGGER.info("From this Rec: "+rec);
        LOGGER.info("Preparing the following pieces to add to the Network:");

        // Two Nodes:
        Integer nodeId = nodeStore.addNew(rec.getNewNode());
        LOGGER.info("New Node with ID: " + nodeId);
        Integer splittingNodeEndId = nodeStore.addNew(rec.getSplittingNodeEnd());
        LOGGER.info("Splitting Node with ID: " + splittingNodeEndId);
        Integer splittingNodeStartId = nodeStore.addNew(rec.getSplittingNodeStart());
        LOGGER.info("Splitting Node with ID: " + splittingNodeStartId);

        // Accepted Proposal for Edge/Segment from new node to the existing segment
        SegmentService.addSegment(rec.getProposedTracks().get(0));
        SegmentService.addSegment(rec.getProposedTracks().get(1));

        // Split of the Existing Network Node
        SegmentService.splitSegment(rec.getEndSegment(), rec.getSplittingNodeEnd());
        SegmentService.splitSegment(rec.getStartSegment(), rec.getSplittingNodeStart());

        rec.logRecommendationSummary();
    }

    /**
     * Prepares the following items for adding to the network:
     * <ul>
     *     <li>The New Node itself.</li>
     *     <li>The Edge in one direction connecting the Node to the Network Node.</li>
     *     <li>The Edge in the other direction connecting the Node to another Network Node.</li>
     * </ul>
     * These are then persisted.
     * @param rec - Containing specific elements to be added to the Network.
     */
    private void addTrackToTwoNodesRec(ToTwoNodes rec) {
        LOGGER.info("From this Rec: " + rec);
        LOGGER.info("Preparing the following pieces to add to the Network:");

        /* New Node: */
        Integer nodeId = nodeStore.addNew(rec.getNewNode());
        LOGGER.info("New Node with ID: " + nodeId);

        /* Accepted Proposal for Edge/Segment from new node to the existing node in each direction. */
        SegmentService.addSegment(rec.getProposedTracks().get(0));
        SegmentService.addSegment(rec.getProposedTracks().get(1));

        rec.logRecommendationSummary();
    }

    /**
     * Knows how to turn a Recommendation's list of nodes into a unique key.
     *
     * For the purposes of determining whether or not two recommendations joining a given
     * proposed node to the network are indeed the same, we look at an ordered pair of the points
     * -- or just a single point.  The String representation of those points is the Key.
     */
    public static class PointKey {
        String key;

        public PointKey(List<GeoNode> geoNodes) {
            if (geoNodes.isEmpty()) {
                key = "NO_NEW_NODES";
            } else if (geoNodes.size() == 1) {
                key = geoNodes.get(0).getPoint().toString();
            } else {
                Point point0 = geoNodes.get(0).getPoint();
                Point point1 = geoNodes.get(1).getPoint();
                switch (Double.compare(point0.getX(),point1.getX())) {
                    case -1:
                        key = point0.toString() + "-" + point1.toString();
                        break;
                    case 0:
                        switch (Double.compare(point0.getY(),point1.getY())) {
                            case -1:
                                key = point0.toString() + "-" + point1.toString();
                                break;
                            case 0:
                                LOGGER.error("Same Node found on both ends of Recommendation: " + point0.toString());
                                key = point0.toString() + "-" + point1.toString();
                                break;
                            case 1:
                                key = point1.toString() + "-" + point0.toString();
                                break;
                        }
                        break;
                    case 1:
                        key = point1.toString() + "-" + point0.toString();
                        break;
                }
            }
        }

        public String getKey() {
            return key;
        }
    }
}
