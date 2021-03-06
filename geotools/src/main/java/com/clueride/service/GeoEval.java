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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import org.apache.log4j.Logger;

import com.clueride.config.GeoProperties;
import com.clueride.dao.JsonNetworkStore;
import com.clueride.dao.JsonNodeStore;
import com.clueride.dao.JsonTrackStore;
import com.clueride.dao.NetworkStore;
import com.clueride.dao.NodeStore;
import com.clueride.dao.TrackStore;
import com.clueride.domain.GeoNode;
import com.clueride.feature.Edge;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.IntersectionUtil;
import com.clueride.geo.LengthToPoint;

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
    private static final Logger LOGGER = Logger.getLogger(GeoEval.class);

    private static GeoEval instance;
    private static final NodeStore LOCATION_STORE = JsonNodeStore
            .getInstance();
    private static final NetworkStore EDGE_STORE = JsonNetworkStore
            .getInstance();
    private static final TrackStore TRACK_STORE = JsonTrackStore
            .getInstance();

    /**
     * @return an instance of ourself.
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
     * Given a node, checks to see if any network segment/edge covers the node.
     *
     * This implementation could be made more efficient by checking the bounds
     * first.
     *
     * TODO: Inconsistent Naming (Id versus no Id)
     *
     * @param geoNode
     * @return
     */
    public Integer matchesSegmentId(GeoNode geoNode) {
        List<Edge> edgeSet = EDGE_STORE.getEdges();
        for (Edge edge : edgeSet) {
            LineString lineString = edge.getLineString();
            // Perform faster envelope test before checking buffered coverage
            if (lineString.getEnvelope().covers(geoNode.getPoint())) {
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
            // Perform faster envelope test before checking buffered coverage
            if (lineString.getEnvelope().covers(geoNode.getPoint())) {
                if (lineString.buffer(GeoProperties.BUFFER_TOLERANCE).covers(
                        geoNode.getPoint())) {
                    coveringTracks.add(track);
                }
            }
        }
        return coveringTracks;
    }

    /**
     * Checks the given LineString to see if it intersects the network at one of
     * the nodes (or Location Groups).
     *
     * Algorithm is to check envelope overlap and if passes that simple test, we
     * see if the point sits within the buffer of the LineString.
     *
     * Distances along the LineString are computed for any matches and if we've
     * found the shortest distance, we record the Node to be returned.
     *
     * @param lineString
     *            - Potential Track into the Network.
     * @return null if no network node is covered by this lineString, or the
     *         closest one if there is a covered network node.
     */
    public GeoNode getNearestNetworkNode(LineString lineString) {
        GeoNode nearestNode = null;
        Double minDistance = Double.MAX_VALUE;

        // Only need to get the envelope and buffer once
        Geometry envelope = lineString.getEnvelope();
        Geometry buffer = lineString.buffer(GeoProperties.NODE_TOLERANCE);

        // Run through all network nodes in the LOCATION_STORE
        Set<GeoNode> nodeSet = LOCATION_STORE.getNodes();
        for (GeoNode geoNode : nodeSet) {
            Point point = geoNode.getPoint();
            if (envelope.covers(point) && buffer.covers(point)) {
                LengthToPoint lengthToPoint = new LengthToPoint(lineString,
                        point.getCoordinate());
                Double distance = lengthToPoint.getLength();
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestNode = geoNode;
                    LOGGER.info("Picked up Node " + geoNode.getId()
                            + " at distance " + minDistance);
                }
            }
        }

        // Location Groups
        // TODO: Add these

        return nearestNode;
    }

    /**
     * @param lineString - the string we're interested in checking for nearest Edge.
     * @return The nearest Edge for the LineString passed.
     */
    public Edge getNearestNetworkEdge(LineString lineString) {
        Edge networkEdge = null;
        Double minDistance = Double.MAX_VALUE;

        // Only need to get the envelope once
        Geometry envelope = lineString.getEnvelope();

        for (Edge edge : EDGE_STORE.getEdges()) {
            LineString lsNetwork = edge.getLineString();
            // Check first if the boundaries overlap at all
            if (!envelope.intersects(lsNetwork.getEnvelope())) {
                LOGGER.debug("No overlap with " + edge.toString());
                continue;
            }

            // This is the part that could stand optimization
            Double intersectDistance = null;
            if (lsNetwork.intersects(lineString)
                    || lsNetwork.crosses(lineString)) {
                LOGGER.debug("INTERSECTION with " + edge.toString());
                Point intersection = IntersectionUtil
                        .findFirstIntersection(lineString, lsNetwork);
                if (intersection != null) {
                    LengthToPoint lengthToPoint = new LengthToPoint(lineString,
                            intersection.getCoordinate());
                    intersectDistance = lengthToPoint.getLength();
                    if (intersectDistance < minDistance) {
                        minDistance = intersectDistance;
                        networkEdge = edge;
                        LOGGER.info("Picked up Edge " + edge.getId()
                                + " at distance " + minDistance);
                    }
                }
            }
        }
        return networkEdge;
    }

    public Edge getMatchingEdge(GeoNode newNode) {
        Integer edgeId = matchesSegmentId(newNode);
        return (EDGE_STORE.getEdgeById(edgeId));
    }
}
