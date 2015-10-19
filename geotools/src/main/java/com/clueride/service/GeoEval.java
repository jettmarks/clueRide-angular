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
 * Created Oct 18, 2015
 */
package com.clueride.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.clueride.config.GeoProperties;
import com.clueride.dao.DefaultLocationStore;
import com.clueride.dao.DefaultNetworkStore;
import com.clueride.dao.DefaultTrackStore;
import com.clueride.dao.LocationStore;
import com.clueride.dao.NetworkStore;
import com.clueride.dao.TrackStore;
import com.clueride.domain.DefaultNodeGroup;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NodeGroup;
import com.clueride.feature.Edge;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.score.TrackConnection;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * Given a set of Geo Elements provide a set of evaluations against the
 * Network/Location/Edge stores.
 *
 * These evaluations are independent of any scoring that is performed; the
 * methods provided here evaluate Topology questions in the context of our
 * Geography without scoring or ranking.
 * 
 * @author jett
 */
public class GeoEval {

    private static GeoEval instance;
    private static final LocationStore LOCATION_STORE = DefaultLocationStore
            .getInstance();
    private static final Double LOC_GROUP_RADIUS_DEG = (Double) GeoProperties
            .getInstance().get("group.radius.degrees");
    private static final NetworkStore EDGE_STORE = DefaultNetworkStore
            .getInstance();
    private static final TrackStore TRACK_STORE = DefaultTrackStore
            .getInstance();

    /**
     * @return
     */
    public static GeoEval getInstance() {
        if (instance == null) {
            instance = new GeoEval();
        }
        return instance;
    }

    private GeoEval() {
    }

    /**
     * 
     * @param geoNode
     * @return
     */
    public Integer matchesNetworkNode(GeoNode geoNode) {
        // Check Network Nodes first
        Set<GeoNode> nodeSet = LOCATION_STORE.getLocations();
        for (GeoNode node : nodeSet) {
            if (node.matchesLocation(geoNode)) {
                return node.getId();
            }
        }

        // Exhausted the Network Nodes, check the location groups
        Set<NodeGroup> locGroups = LOCATION_STORE.getLocationGroups();
        for (NodeGroup nodeGroup : locGroups) {
            Point point = ((DefaultNodeGroup) nodeGroup).getPoint();
            if (point.buffer(LOC_GROUP_RADIUS_DEG).covers(geoNode.getPoint())) {
                return nodeGroup.getId();
            }
        }

        // Nothing matched; give up and return Node ID indicating no match
        return -1;
    }

    /**
     * Given a node, checks to see if any network segment/edge covers the node.
     * 
     * This implementation could be made more efficient by checking the bounds
     * first.
     * 
     * @param geoNode
     * @return
     */
    public Integer matchesSegmentId(GeoNode geoNode) {
        Set<Edge> edgeSet = EDGE_STORE.getEdges();
        for (Edge edge : edgeSet) {
            LineString lineString = edge.getLineString();
            // Perform faster boundary test before checking buffered coverage
            if (lineString.getBoundary().covers(geoNode.getPoint())) {
                if (lineString.buffer(GeoProperties.BUFFER_TOLERANCE).covers(
                        geoNode.getPoint())) {
                    return edge.getId();
                }
            }
        }
        // Nothing matched; give up and return Edge ID indicating no match
        return -1;
    }

    /**
     * @param geoNode
     * @return
     */
    public List<TrackFeature> listCoveringTracks(GeoNode geoNode) {
        List<TrackFeature> coveringTracks = new ArrayList<>();
        for (TrackFeature track : TRACK_STORE.getTrackFeatures()) {
            LineString lineString = track.getLineString();
            // Perform faster boundary test before checking buffered coverage
            if (lineString.getBoundary().covers(geoNode.getPoint())) {
                if (lineString.buffer(GeoProperties.BUFFER_TOLERANCE).covers(
                        geoNode.getPoint())) {
                    coveringTracks.add(track);
                }
            }
        }
        return coveringTracks;
    }

    /**
     * This determines the best connection for a lineString that extends from
     * its start to potential Edges and Nodes on the network.
     * 
     * Since multiple connections are possible, this retains the closest
     * TrackConnection found so far and then returns that one, or an empty one
     * if no connection is found.
     * 
     * @param lineStringToEnd
     * @return
     */
    public TrackConnection getTrackConnection(LineString lineString) {
        Double minDistance = Double.MAX_VALUE;
        TrackConnection closestConnection = new TrackConnection();

        // Only need to get the boundary and buffer once
        Geometry boundary = lineString.getBoundary();
        Geometry buffer = lineString.buffer(GeoProperties.NODE_TOLERANCE);

        // Check Network Nodes first
        Set<GeoNode> nodeSet = LOCATION_STORE.getLocations();
        for (GeoNode geoNode : nodeSet) {
            Point point = geoNode.getPoint();
            if (boundary.covers(point) && buffer.covers(point)) {
                Double distance = lineString.distance(point);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestConnection = new TrackConnection(geoNode);
                }
            }
        }

        // Location Groups
        // TODO: Add these

        // Check segments for intersection/crossing with lineString
        // TODO: Add these

        return closestConnection;
    }
}
