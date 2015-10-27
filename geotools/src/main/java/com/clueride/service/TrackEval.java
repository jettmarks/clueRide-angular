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
 * Created Oct 25, 2015
 */
package com.clueride.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.clueride.config.GeoProperties;
import com.clueride.dao.DefaultLocationStore;
import com.clueride.dao.DefaultNetworkStore;
import com.clueride.dao.LocationStore;
import com.clueride.dao.NetworkStore;
import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.GeoNode;
import com.clueride.feature.Edge;
import com.clueride.geo.IntersectionUtil;
import com.clueride.geo.LengthToPoint;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * Class which can tell us for a given track, what is the best way to connect
 * from the start of that track to the network.
 *
 * The life-cycle for this class is to instantiate for a given LineString which
 * represents a Track passing through a new location and then poll this instance
 * to obtain the values required to build a recommendation for the overall Track
 * (both ends of the original Track).
 * 
 * This instance also collects diagnostic information derived from evaluating
 * this track's connection to the network. At some point, it probably makes
 * sense to cache this information. Knowing at least the interesting indices
 * could save a great deal of calculation.
 * 
 * @author jett
 *
 */
public class TrackEval {
    private static final Logger LOGGER = Logger.getLogger(TrackEval.class);

    /** What we're evaluating. */
    private final LineString lsTrack;

    /** Selected Node. */
    private GeoNode networkNode;
    private Double nodeDistance = Double.MAX_VALUE;

    /** Selected Edge. */
    private Edge networkEdge;
    private Double edgeDistance = Double.MAX_VALUE;
    /** Details on that Edge's splitting point. */
    private Point splittingPoint;
    private DefaultGeoNode splittingNode;

    /** Overall evaluation of what is closest. */
    private TrackEvalType trackEvalType = TrackEvalType.UNDEFINED;

    /** Diagnostic data on the evaluation. */
    private Map<GeoNode, Double> distancePerNode = new HashMap<>();
    private Map<Edge, Double> distancePerEdge = new HashMap<>();
    private Map<Edge, GeoNode> splitPerEdge = new HashMap<>();

    /** Our picture of the current network. */
    private static final LocationStore LOCATION_STORE = DefaultLocationStore
            .getInstance();
    private static final NetworkStore EDGE_STORE = DefaultNetworkStore
            .getInstance();

    public TrackEval(LineString lsTrack) {
        this.lsTrack = lsTrack;
        prepareEvaluation();
    }

    /**
     * 
     */
    private void prepareEvaluation() {
        // First, check network connections
        networkNode = getNearestNetworkNode();
        networkEdge = getNearestNetworkEdge();
        // Evaluate those connections (if any)
        setEvalType();

        // Only continue if we've got other details to share
        switch (trackEvalType) {
        case NODE:
            break;
        case EDGE:
            splittingNode = new DefaultGeoNode(splittingPoint);
            break;
        case NO_CONNECTION:
        default:
            break;
        }
        return;
    }

    /**
     * Sets the type based on the presence of connections and which one is
     * closer to the start of the track.
     */
    private void setEvalType() {

        if (networkNode == null && networkEdge == null) {
            trackEvalType = TrackEvalType.NO_CONNECTION;
        } else {
            if (nodeDistance < edgeDistance) {
                trackEvalType = TrackEvalType.NODE;
            } else {
                trackEvalType = TrackEvalType.EDGE;
            }
        }
        // Diagnostic Mode
        // trackEvalType = TrackEvalType.DIAGNOSTIC;
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
    public GeoNode getNearestNetworkNode() {
        GeoNode nearestNode = null;

        // Only need to get the envelope and buffer once
        Geometry envelope = lsTrack.getEnvelope();
        Geometry buffer = lsTrack.buffer(GeoProperties.NODE_TOLERANCE);

        // Run through all network nodes in the LOCATION_STORE
        Set<GeoNode> nodeSet = LOCATION_STORE.getLocations();
        for (GeoNode geoNode : nodeSet) {
            Point point = geoNode.getPoint();
            if (envelope.covers(point) && buffer.covers(point)) {
                LengthToPoint lengthToPoint = new LengthToPoint(lsTrack,
                        point.getCoordinate());
                Double distance = lengthToPoint.getLength();
                distancePerNode.put(geoNode, distance);
                if (distance < nodeDistance) {
                    nodeDistance = distance;
                    nearestNode = geoNode;
                    LOGGER.info("Picked up Node " + geoNode.getId()
                            + " at distance " + nodeDistance);
                }
            }
        }

        // Location Groups
        // TODO: Add these

        return nearestNode;
    }

    /**
     * @param lsEnd
     * @return
     */
    public Edge getNearestNetworkEdge() {
        Edge networkEdge = null;

        // Only need to get the envelope once
        Geometry envelope = lsTrack.getEnvelope();

        for (Edge edge : EDGE_STORE.getEdges()) {
            LineString lsNetwork = edge.getLineString();
            // Check first if the boundaries overlap at all
            if (!envelope.intersects(lsNetwork.getEnvelope())) {
                // LOGGER.debug("No overlap with " + edge.toString());
                continue;
            }

            // This is the part that could stand optimization
            Double intersectDistance = null;
            if (lsNetwork.intersects(lsTrack)
                    || lsNetwork.crosses(lsTrack)) {
                LOGGER.debug("INTERSECTION with " + edge.toString());
                Point intersection = IntersectionUtil
                        .walkToIntersectingNode(lsTrack, lsNetwork);
                // Point intersection = IntersectionUtil
                // .findFirstIntersection(lsTrack, lsNetwork);
                if (intersection == null) {
                    continue;
                } else {
                    LengthToPoint lengthToPoint = new LengthToPoint(lsTrack,
                            intersection.getCoordinate());
                    intersectDistance = lengthToPoint.getLength();
                    distancePerEdge.put(edge, intersectDistance);
                    splitPerEdge.put(edge, new DefaultGeoNode(intersection));
                    if (intersectDistance < edgeDistance) {
                        edgeDistance = intersectDistance;
                        networkEdge = edge;
                        splittingPoint = intersection;
                        LOGGER.info("Picked up Edge " + edge.getId()
                                + " at distance " + edgeDistance);
                    }
                }
            }
        }
        return networkEdge;
    }

    /**
     * @return the nodeDistance
     */
    public Double getNodeDistance() {
        return nodeDistance;
    }

    /**
     * @return the edgeDistance
     */
    public Double getEdgeDistance() {
        return edgeDistance;
    }

    /**
     * @return the networkNode
     */
    public GeoNode getNetworkNode() {
        return networkNode;
    }

    /**
     * @return the networkEdge
     */
    public Edge getNetworkEdge() {
        return networkEdge;
    }

    /**
     * @return the trackEvalType
     */
    public TrackEvalType getTrackEvalType() {
        return trackEvalType;
    }

    /**
     * @return the splittingNode
     */
    public DefaultGeoNode getSplittingNode() {
        return splittingNode;
    }

    /**
     * @return the distancePerNode
     */
    public Map<GeoNode, Double> getDistancePerNode() {
        return distancePerNode;
    }

    /**
     * @return the distancePerEdge
     */
    public Map<Edge, Double> getDistancePerEdge() {
        return distancePerEdge;
    }

    /**
     * @return the splitPerEdge
     */
    public Map<Edge, GeoNode> getSplitPerEdge() {
        return splitPerEdge;
    }

}
