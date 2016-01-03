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

import com.clueride.dao.NodeStore;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkProposal;
import com.clueride.domain.dev.NetworkRecommendation;
import com.clueride.domain.dev.NewNodeProposal;
import com.clueride.domain.dev.rec.OnTrack;
import com.clueride.domain.dev.rec.Rec;
import com.clueride.feature.TrackFeature;
import com.clueride.service.builder.NewLocRecBuilder;
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
        // TODO: CA-23 - renaming Loc to Node
        NewNodeProposal newNodeProposal = new NewNodeProposal(newNode);
        NewLocRecBuilder recBuilder = new NewLocRecBuilder(newNode);

        // Check if our node happens to already be on the network list of nodes
        Integer matchingNodeId = geoEval.matchesNetworkNode(newNode);
        if (matchingNodeId > 0) {
            // Found matching node; add as a recommendation
            newNodeProposal.add(recBuilder.onNode(matchingNodeId));
            return newNodeProposal;
        }

        // Check if our node happens to be on the network list of edges
        Integer matchingSegmentId = geoEval.matchesSegmentId(newNode);
        if (matchingSegmentId > 0) {
            // TODO: Missing from here: splitting the segment on the node
            newNodeProposal.add(recBuilder.onSegment(matchingSegmentId));
            return newNodeProposal;
        }

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
            LOGGER.info( fullMessage );
        }
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
