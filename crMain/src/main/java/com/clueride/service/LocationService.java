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
import com.clueride.domain.dev.NetworkState;
import com.clueride.domain.dev.NodeEvaluationStatus;
import com.clueride.domain.dev.NodeGroup;
import com.clueride.domain.dev.NodeNetworkState;
import com.clueride.domain.dev.Segment;
import com.clueride.domain.factory.PointFactory;
import com.clueride.geo.DefaultNetwork;
import com.clueride.geo.Network;
import com.clueride.geo.TranslateUtil;
import com.clueride.io.JsonStoreType;
import com.clueride.io.JsonUtil;
import com.vividsolutions.jts.geom.Point;

/**
 * Handles requests for Locations.
 *
 * @author jett
 */
public class LocationService {

    private static final Logger logger = Logger
            .getLogger(LocationService.class);

    // TODO: Add Dependency Injection for the NetworkService
    private Network network = DefaultNetwork.getInstance();
    private static LocationStore locationStore = DefaultLocationStore
            .getInstance();

    /**
     * Currently creates a brand new GeoNode instance which is then evaluated by
     * the Network; the Network "decorates" the GeoNode with features that are
     * then returned to the Map.
     * 
     * @param lat
     * @param lon
     * @return
     */
    public String addNewLocation(Double lat, Double lon) {
        String result = "";
        JsonUtil jsonUtil = new JsonUtil(JsonStoreType.LOCATION);
        Point point = PointFactory.getJtsInstance(lat, lon, 0.0);
        GeoNode geoNode = new DefaultGeoNode();
        geoNode.setPoint(point);
        geoNode.setName("candidate");
        // TODO: Move toward use of the nodeEvaluation instance
        NodeNetworkState nodeEvaluation = network.evaluateNodeState(geoNode);
        result = jsonUtil.toString(geoNode);
        logger.debug("At the requested Location: " + geoNode);
        return result;
    }

    /**
     * User has indicated that our last Node is the one we want to connect.
     * 
     * Bring in the last evaluation we made and use that data to construct the
     * new Network objects.
     * 
     * Invokes the Segment service as needed to add any new segments that are
     * involved, but we handle our Nodes here.
     */
    public void confirmNewLocation() {
        NodeEvaluationStatus nodeEvaluation = NetworkState
                .getLastNodeEvaluation();

        // As a first cut, we're assuming a new segment, two new nodes, and that
        // we're splitting the original segment at the end node of the first
        // segment.

        Segment brandNewSegment = nodeEvaluation.getProposedSegmentFromTrack();
        GeoNode startNode = (GeoNode) brandNewSegment.getStart();
        GeoNode endNode = (GeoNode) brandNewSegment.getEnd();

        // Save our new nodes
        locationStore.addNew(startNode);
        locationStore.addNew(endNode);
        try {
            locationStore.persistAndReload();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add and save our new segments (using SegmentService)
        brandNewSegment.setName("unnamed");
        SegmentService.addSegment(brandNewSegment);
        Segment existingSegmentToSplit = nodeEvaluation.getIntersectedSegment();
        SegmentService.splitSegment(existingSegmentToSplit, endNode);
        SegmentService.saveChanges();
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
