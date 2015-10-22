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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.dao.DefaultLocationStore;
import com.clueride.dao.LocationStore;
import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.DefaultNodeGroup;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkProposal;
import com.clueride.domain.dev.NewLocProposal;
import com.clueride.domain.dev.NodeGroup;
import com.clueride.domain.dev.NodeNetworkState;
import com.clueride.domain.dev.rec.Rec;
import com.clueride.domain.factory.PointFactory;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.DefaultNetwork;
import com.clueride.geo.Network;
import com.clueride.geo.TranslateUtil;
import com.clueride.io.JsonStoreType;
import com.clueride.io.JsonUtil;
import com.clueride.service.builder.NewLocRecBuilder;
import com.vividsolutions.jts.geom.Point;

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

    private int countBuildNewLocRequests;
    private static LocationStore locationStore = DefaultLocationStore
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

        // NetworkProposal networkProposal = network
        // .evaluateNodeState(newLocation);
        NetworkProposal networkProposal = buildProposalForNewLoc(newLocation);

        NodeNetworkState nodeEvaluation = networkProposal.getNodeNetworkState();

        JsonUtil jsonUtil = new JsonUtil(JsonStoreType.LOCATION);
        // result = jsonUtil.toString(newLocation);
        result = networkProposal.toJson();
        LOGGER.debug("At the requested Location: " + newLocation);
        return result;
    }

    /**
     * @param lat
     * @param lon
     * @return
     */
    private GeoNode getCandidateLocation(Double lat, Double lon) {
        GeoNode newLocation;
        newLocation = new DefaultGeoNode();
        Point point = PointFactory.getJtsInstance(lat, lon, 0.0);
        newLocation.setPoint(point);
        newLocation.setName("candidate");
        return newLocation;
    }

    /**
     * Given a proposed New Location, find out how it connects to the network
     * and recommend a connection if one doesn't already exist.
     * 
     * Replacing the Network.evaluatNodeState() method.
     * 
     * @return
     */
    protected NetworkProposal buildProposalForNewLoc(GeoNode geoNode) {
        LOGGER.debug("start - evaluateNodeState(): "
                + countBuildNewLocRequests++);
        GeoEval geoEval = GeoEval.getInstance();
        NewLocProposal newLocProposal = new NewLocProposal();
        NewLocRecBuilder recBuilder = new NewLocRecBuilder(geoNode);

        // Check if our node happens to already be on the network list of nodes
        Integer matchingNodeId = geoEval.matchesNetworkNode(geoNode);
        if (matchingNodeId > 0) {
            // Found matching node; add as a recommendation
            newLocProposal.add(recBuilder.onNode(matchingNodeId));
            return newLocProposal;
        }

        // Check if our node happens to be on the network list of edges
        Integer matchingSegmentId = geoEval.matchesSegmentId(geoNode);
        if (matchingSegmentId > 0) {
            // TODO: Missing from here: splitting the segment on the node
            newLocProposal.add(recBuilder.onSegment(matchingSegmentId));
            return newLocProposal;
        }

        // Try a Track-based proposal
        List<TrackFeature> coveringTracks = geoEval.listCoveringTracks(geoNode);
        for (TrackFeature track : coveringTracks) {
            Rec rec = recBuilder.getTrackRec(track, geoNode);
            if (rec != null) {
                newLocProposal.add(rec);
            }
        }

        // if (coveringTracks.size() > 0) {
        // TrackRecBuilder trackRecBuilder = new TrackRecBuilder(geoNode,
        // coveringTracks);
        // newLocProposal.addAll(trackRecBuilder.build());
        // }

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

        Set<NodeGroup> nodeGroups = locationStore.getLocationGroups();
        for (NodeGroup nodeGroup : nodeGroups) {
            featureList.add(TranslateUtil
                    .groupNodeToFeature((DefaultNodeGroup) nodeGroup));
        }

        JsonUtil jsonUtil = new JsonUtil(JsonStoreType.LOCATION);
        // return (jsonUtil.toString(featureList));
        return (jsonUtil.toString(featureList));
    }

    /**
     * @param id
     * @param lat
     * @param lon
     * @return
     */
    public String setLocationGroup(Integer id, Double lat, Double lon) {
        NodeGroup nodeGroup = (NodeGroup) locationStore.getNodeById(id);
        nodeGroup.setLat(lat);
        nodeGroup.setLon(lon);
        try {
            locationStore.persistAndReload();
        } catch (IOException e) {
            e.printStackTrace();
            return "{status: failed}";
        }
        return "{status: ok}";
    }
}
