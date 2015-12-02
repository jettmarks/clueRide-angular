/**
 *   Copyright 2015 Jett Marks
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created Aug 27, 2015
 */
package com.clueride.service;

import com.clueride.dao.DefaultNodeStore;
import com.clueride.dao.NetworkProposalStore;
import com.clueride.dao.NodeStore;
import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.DefaultNodeGroup;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkProposal;
import com.clueride.domain.dev.NetworkRecommendation;
import com.clueride.domain.dev.NewLocProposal;
import com.clueride.domain.dev.NodeGroup;
import com.clueride.domain.dev.rec.*;
import com.clueride.domain.factory.PointFactory;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.DefaultNetwork;
import com.clueride.geo.Network;
import com.clueride.geo.TranslateUtil;
import com.clueride.io.GeoJsonUtil;
import com.clueride.io.JsonStoreType;
import com.clueride.service.builder.NewLocRecBuilder;
import com.clueride.service.builder.TrackRecBuilder;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import org.apache.log4j.Logger;
import org.opengis.feature.simple.SimpleFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles requests for Locations.
 *
 * @author jett
 */
public class LocationService {

    private static final Logger LOGGER = Logger
            .getLogger(LocationService.class);

    // TODO: Add Dependency Injection for the NetworkService
    private Network network = DefaultNetwork.getInstance();

    private static int countBuildNewLocRequests;
    private static NodeStore nodeStore = DefaultNodeStore
            .getInstance();

    /**
     * Currently creates a brand new GeoNode instance which is then evaluated by
     * the Network; the Network "decorates" the GeoNode with features that are
     * then returned to the Map.
     * 
     * Moving toward use of the NetworkProposal to collect that recommendation
     * and the data required to present it to the user, then later persist the
     * user's choices for blessing that recommendation.
     * 
     * @param lat
     * @param lon
     * @return
     */
    public String addNewLocation(Double lat, Double lon) {
        String result = "";

        GeoNode newLocation;
        newLocation = getCandidateLocation(lat, lon);
        NetworkProposal networkProposal = buildProposalForNewLoc(newLocation);
        NetworkProposalStore.add(networkProposal);
        result = networkProposal.toJson();
        return result;
    }

    /**
     * Diagnostic request to show all the points on the track that covers the
     * point at this lat/lon pair.
     * 
     * @param lat
     * @param lon
     * @return
     */
    public String showPointsOnTrack(Double lat, Double lon) {
        String result = "";

        GeoNode newLocation;
        newLocation = getCandidateLocation(lat, lon);
        NetworkProposal networkProposal = buildPointsOnTrackProposal(newLocation);
        result = networkProposal.toJson();
        return result;
    }

    /**
     * @param lat
     * @param lon
     * @return
     */
    private GeoNode getCandidateLocation(Double lat, Double lon) {
        GeoNode newLocation;
        Point point = PointFactory.getJtsInstance(lat, lon, 0.0);
        newLocation = new DefaultGeoNode(point);
        newLocation.setName("candidate");
        return newLocation;
    }

    /**
     * @param newLoc
     * @return
     */
    private NetworkProposal buildPointsOnTrackProposal(GeoNode newLoc) {
        GeoEval geoEval = GeoEval.getInstance();
        NewLocProposal newLocProposal = new NewLocProposal(newLoc);
        List<TrackFeature> coveringTracks = geoEval.listCoveringTracks(newLoc);
        DiagnosticRec diagRec = new DiagnosticRec(newLoc);
        if (coveringTracks.size() == 1) {
            TrackFeature track = coveringTracks.get(0);
            LineString lineString = track.getLineString();
            for (int i = 0; i < lineString.getNumPoints(); i++) {
                diagRec.addFeature(TranslateUtil
                        .geoNodeToFeature(new DefaultGeoNode(lineString
                                .getPointN(i))));
            }
        }
        newLocProposal.add(diagRec);
        return newLocProposal;
    }

    /**
     * Given a proposed New Location, find out how it connects to the network
     * and recommend a connection if one doesn't already exist.
     * 
     * Replacing the Network.evaluatNodeState() method.
     * 
     * @return
     */
    protected NetworkProposal buildProposalForNewLoc(GeoNode newLoc) {
        LOGGER.debug("start - buildProposalForNewLoc(): "
                + countBuildNewLocRequests++);
        GeoEval geoEval = GeoEval.getInstance();
        NewLocProposal newLocProposal = new NewLocProposal(newLoc);
        NewLocRecBuilder recBuilder = new NewLocRecBuilder(newLoc);

        // Check if our node happens to already be on the network list of nodes
        Integer matchingNodeId = geoEval.matchesNetworkNode(newLoc);
        if (matchingNodeId > 0) {
            // Found matching node; add as a recommendation
            newLocProposal.add(recBuilder.onNode(matchingNodeId));
            return newLocProposal;
        }

        // Check if our node happens to be on the network list of edges
        Integer matchingSegmentId = geoEval.matchesSegmentId(newLoc);
        if (matchingSegmentId > 0) {
            // TODO: Missing from here: splitting the segment on the node
            newLocProposal.add(recBuilder.onSegment(matchingSegmentId));
            return newLocProposal;
        }

        // Try a Track-based proposal
        List<TrackFeature> coveringTracks = geoEval.listCoveringTracks(newLoc);
        for (TrackFeature track : coveringTracks) {
            TrackRecBuilder trackRecBuilder = new TrackRecBuilder(newLoc,
                    track);
            Rec rec = trackRecBuilder.build();
            if (rec != null) {
                newLocProposal.add(rec);
            }
        }
        return newLocProposal;
    }

    /**
     * @return
     */
    public String getLocationGroups() {
        List<SimpleFeature> featureList = new ArrayList<>();

        // Make up our location groups for now
        // double lat = 33.75;
        // double lon = -84.4;
        // double elevation = 300.0;
        // double radius = (double) GeoProperties.getInstance()
        // .get("group.radius");
        //
        // DefaultNodeGroup nodeGroup = (DefaultNodeGroup) NodeFactory
        // .getGroupInstance(lat, lon, elevation, radius);
        // SimpleFeature feature = TranslateUtil.groupNodeToFeature(nodeGroup);
        // featureList.add(feature);
        //
        // lon = -84.35;
        // nodeGroup = (DefaultNodeGroup) NodeFactory.getGroupInstance(lat, lon,
        // elevation, radius);
        // feature = TranslateUtil.groupNodeToFeature(nodeGroup);
        // featureList.add(feature);

        Set<NodeGroup> nodeGroups = nodeStore.getLocationGroups();
        for (NodeGroup nodeGroup : nodeGroups) {
            featureList.add(TranslateUtil
                    .groupNodeToFeature((DefaultNodeGroup) nodeGroup));
        }

        GeoJsonUtil geoJsonUtil = new GeoJsonUtil(JsonStoreType.LOCATION);
        // return (geoJsonUtil.toString(featureList));
        return (geoJsonUtil.toString(featureList));
    }

    /**
     * @param id
     * @param lat
     * @param lon
     * @return
     */
    public String setLocationGroup(Integer id, Double lat, Double lon) {
        NodeGroup nodeGroup = (NodeGroup) nodeStore.getNodeById(id);
        nodeGroup.setLat(lat);
        nodeGroup.setLon(lon);
        try {
            nodeStore.persistAndReload();
        } catch (IOException e) {
            e.printStackTrace();
            return "{status: failed}";
        }
        return "{status: ok}";
    }

    /**
     * Implementation of a request to confirm the latest proposal as the one we
     * want to add to the network.
     * 
     * The NetworkProposal instance we stashed away will hold the details needed
     * to create the objects we add to the persisted network.
     * 
     * @return
     */
    public static String confirmNewLocation() {
        NetworkProposal networkProposal = NetworkProposalStore
                .getLastProposal();
        List<NetworkRecommendation> recs = networkProposal.getRecommendations();
        if (networkProposal.hasMultipleRecommendations()) {
            LOGGER.warn("Not handling Multiple Rec proposals yet.");
        } else {
            Rec rec = (Rec) recs.get(0);
            switch (rec.getRecType()) {
                case ON_SEGMENT:
                case ON_NODE:
                    LOGGER.warn("We don't have a way to select "+rec.getRecType());
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
                case TRACK_TO_2_NODES:
                    addTrackToTwoNodesRec((ToTwoNodes) rec);
                    break;
                case TRACK_TO_2_SEGMENTS:
                    addTrackToTwoSegmentsRec((ToTwoSegments) rec);
                    break;

                case UNDEFINED:
                case OFF_NETWORK:
                default:
                    LOGGER.warn("Rec Type: " + rec.getRecType());
            }
        }
        return "{\"status\": \"OK\"}";
    }

    private static void addTrackToTwoSegmentsRec(ToTwoSegments rec) {
        LOGGER.info("From this Rec: "+rec);

    }

    private static void addTrackToTwoNodesRec(ToTwoNodes rec) {
        LOGGER.info("From this Rec: "+rec);

    }

    private static void addTrackToSegmentAndNodeRec(ToSegmentAndNode rec) {
        LOGGER.info("From this Rec: "+rec);

    }

    private static void addTrackToNodeRec(ToNode rec) {
        LOGGER.info("From this Rec: "+rec);
        LOGGER.info("Preparing the following pieces to add to the Network:");
        LOGGER.info("New Loc: " + rec.getNewLoc().getName());
        rec.logRecommendationSummary();

        SegmentService.addSegment(rec.getProposedTrack());
    }

    /**
     * Prepares these items for adding to the network:
     * <UL>
     * <LI>The Location Node itself
     * <LI>The Edge connecting the Location to the Segment (from the Track)
     * <LI>The Splitting Node (also the end of the previous Edge)
     * <LI>Two new Edges that result from splitting the original Segment.
     * <LI>Removal of the original Segment.
     * </UL>
     * 
     * And then ask these be persisted.
     * 
     * @param rec
     */
    private static void addTrackToSegmentRec(ToSegment rec) {
        LOGGER.info("From this Rec: "+rec);
        LOGGER.info("Preparing the following pieces to add to the Network:");
        LOGGER.info("New Loc: " + rec.getNewLoc().getName());
        rec.logRecommendationSummary();

        SegmentService.addSegment(rec.getProposedTrack());

//        for (SimpleFeature feature : rec.getFeatureCollection()) {
//            feature.getFeatureType().getTypeName();
//        }

    }

    public static String showAllNodes() {
        NetworkProposal networkProposal = buildAllNodesProposal();
        String result = networkProposal.toJson();
        return result;
    }

    /** Builds a proposal that contains all the points on our Network. */
    private static NetworkProposal buildAllNodesProposal() {
        NewLocProposal newLocProposal = new NewLocProposal(new DefaultGeoNode());
        DiagnosticRec diagRec = new DiagnosticRec(null);
        for (GeoNode geoNode : nodeStore.getLocations()) {
            diagRec.addFeature(TranslateUtil.geoNodeToFeature(geoNode));
        }
        newLocProposal.add(diagRec);
        return newLocProposal;
    }
}
