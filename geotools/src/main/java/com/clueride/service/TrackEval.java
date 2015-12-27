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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import org.apache.log4j.Logger;

import com.clueride.config.GeoProperties;
import com.clueride.dao.DefaultNetworkStore;
import com.clueride.dao.DefaultNodeStore;
import com.clueride.dao.NetworkStore;
import com.clueride.dao.NodeStore;
import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.GeoNode;
import com.clueride.feature.Edge;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.IntersectionUtil;
import com.clueride.geo.LengthToPoint;
import com.clueride.geo.SplitLineString;
import static com.clueride.geo.SplitLineString.START;

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

    /** Our picture of the current network. */
    // TODO: CA-64 Switch to Guice Dependency Injection
    private static final NodeStore NODE_STORE = DefaultNodeStore.getInstance();
    // TODO: CA-64 Switch to Guice Dependency Injection
    private static final NetworkStore EDGE_STORE = DefaultNetworkStore.getInstance();

    /** What we're evaluating. */
    private final TrackFeature sourceTrack;
    /** Selected Node. */
    private GeoNode networkNode;
    private Double nodeDistance = Double.MAX_VALUE;
    /** Selected Edge. */
    private Edge networkEdge;
    private Double edgeDistance = Double.MAX_VALUE;
    /** Details on that Edge's splitting point. */
    private Point splittingPoint;
    private DefaultGeoNode splittingNode;
    /** Portion of Track required to reach the network. */
    private TrackFeature proposedTrack;
    /** Overall evaluation of what is closest. */
    private TrackEvalType trackEvalType = TrackEvalType.UNDEFINED;
    /** Diagnostic data on the evaluation. */
    private Map<GeoNode, Double> distancePerNode = new HashMap<>();
    private Map<Edge, Double> distancePerEdge = new HashMap<>();
    private Map<Edge, GeoNode> splitPerEdge = new HashMap<>();

    /**
     * Accepts a given track and uses the existing Network to determine where and
     * how the track might be connected.
     * Incoming TrackFeature is typically one of the split tracks that comes from
     * a track the new location is sitting upon.
     * @param sourceTrack
     */
    public TrackEval(TrackFeature sourceTrack) {
        this.sourceTrack = sourceTrack;
        prepareEvaluation();
    }

    /**
     * 
     */
    private void prepareEvaluation() {
        // First, check network connections
        networkNode = findNearestNetworkNode();
        networkEdge = findNearestNetworkEdge();
        // Evaluate those connections (if any)
        setEvalType();

        // Only continue if we've got other details to share
        switch (trackEvalType) {
        case NODE:
            break;
        case EDGE:
            splittingNode = new DefaultGeoNode(splittingPoint);
            splittingNode.setName("split");
            break;
        case NO_CONNECTION:
        default:
            break;
        }
    }

    /**
     * Sets the type based on the presence of connections and which one is
     * closer to the start of the track.
     */
    private void setEvalType() {

        if (networkNode == null && networkEdge == null) {
            trackEvalType = TrackEvalType.NO_CONNECTION;
            proposedTrack = null;
        } else {
            if (nodeDistance < (edgeDistance+GeoProperties.BUFFER_TOLERANCE)) {
                trackEvalType = TrackEvalType.NODE;
                proposedTrack = getProposedTrackToNode();
            } else {
                trackEvalType = TrackEvalType.EDGE;
                proposedTrack = getProposedTrackToEdge();
            }
        }
        // Diagnostic Mode
        // trackEvalType = TrackEvalType.DIAGNOSTIC;
    }

    /**
     * Trims down the evaluated Track to the portion required to reach the network Node.
     * @return LineString representing the portion of the Track which reached the network.
     */
    private TrackFeature getProposedTrackToNode() {
        return getTrackToNode(networkNode);
    }

    /**
     * Trims down the evaluated Track to the portion required to reach the network Edge
     * as it intersects at the Splitting Node.
     * @return LineString representing the portion of the Track which reached the network.
     */
    private TrackFeature getProposedTrackToEdge() {
        return getTrackToNode(splittingNode);
    }

    private TrackFeature getTrackToNode(GeoNode endingNode) {
        if (endingNode == null) {
            return null;
        } else {
            SplitLineString trackPairRequired = new SplitLineString(sourceTrack, endingNode);
            trackPairRequired.setMaintainStartOrder(true);
            return trackPairRequired.getSubTrackFeature(START);
        }
    }

    public GeoNode getNearestNetworkNode() {
        return networkNode;
    }

    public Edge getNearestNetworkEdge() {
        return networkEdge;
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
     * @return null if no network node is covered by this lineString, or the
     *         closest one if there is a covered network node.
     */
    private GeoNode findNearestNetworkNode() {
        LOGGER.debug("Finding Node nearest to start of track: " + sourceTrack.getNodeList().get(0));
        GeoNode nearestNode = null;
        LineString lsSource = sourceTrack.getLineString();

        // Only need to get the envelope and buffer once
        Geometry envelope = lsSource.getEnvelope();
        Geometry buffer = lsSource.buffer(GeoProperties.NODE_TOLERANCE);

        // Run through all network nodes in the NODE_STORE
        Set<GeoNode> nodeSet = NODE_STORE.getNodes();
        for (GeoNode geoNode : nodeSet) {
            Point point = geoNode.getPoint();
            if (envelope.covers(point) && buffer.covers(point)) {
                LengthToPoint lengthToPoint = new LengthToPoint(lsSource,
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

        if (nearestNode != null) {
            nearestNode.setName("nearest");
        }
        return nearestNode;
    }

    /**
     * Find where this Track hits the network while looking for Edges already on
     * the network.
     * @return Edge representing the closest point at which this Track crosses
     * a Network Edge.
     */
    public Edge findNearestNetworkEdge() {
        LOGGER.debug("Finding Edge nearest to start of track: " + sourceTrack.getNodeList().get(0));
        Edge networkEdge = null;
        boolean envelopeFound = false;
        LineString lsSource = sourceTrack.getLineString();

        // Only need to get the envelope once
        Geometry envelope = lsSource.getEnvelope();
        Geometry bufferedSource = lsSource.buffer(GeoProperties.BUFFER_TOLERANCE);

        for (Edge edge : EDGE_STORE.getEdges()) {
            LineString lsNetwork = edge.getLineString();
            // Check first if the boundaries overlap at all
            if (!envelope.intersects(lsNetwork.getEnvelope())) {
                continue;
            }
            envelopeFound = true;

            // This is the part that could stand optimization
            Double intersectDistance;
            if (lsNetwork.intersects(bufferedSource)
                    || lsNetwork.crosses(bufferedSource)) {
                LOGGER.debug("INTERSECTION with " + edge.toString());
                Point intersection = IntersectionUtil
                        .walkToIntersectingNode(lsSource, lsNetwork);
                if (intersection != null) {
                    LengthToPoint lengthToPoint = new LengthToPoint(lsSource,
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
                    } else {
                        LOGGER.info("Edge " + networkEdge.getId() + " remains closer than " + edge.getId());
                    }
                }
            }
        }
        if (!envelopeFound) {
            LOGGER.info("No Envelope overlapping this Track: " + sourceTrack.getId());
        }
        splittingNode = new DefaultGeoNode(splittingPoint);
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

    /**
     * @return the portion of the evaluated Track which reaches the network, either Node or Edge.
     */
    public TrackFeature getProposedTrack() {
        return proposedTrack;
    }
}
