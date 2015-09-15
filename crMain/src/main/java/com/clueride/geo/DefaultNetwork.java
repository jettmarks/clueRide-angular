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
 * Created Aug 18, 2015
 */
package com.clueride.geo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.geotools.feature.DefaultFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.config.GeoProperties;
import com.clueride.dao.DefaultLocationStore;
import com.clueride.dao.DefaultNetworkStore;
import com.clueride.dao.LoadService;
import com.clueride.dao.LocationStore;
import com.clueride.dao.NetworkStore;
import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.DefaultNodeGroup;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NodeGroup;
import com.clueride.domain.dev.NodeNetworkState;
import com.clueride.domain.dev.Segment;
import com.clueride.domain.factory.NodeFactory;
import com.clueride.geo.score.IntersectionScore;
import com.clueride.io.JsonStoreType;
import com.clueride.io.JsonUtil;
import com.clueride.poc.geotools.TrackStore;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * Holds collection of segments that make up the full set of connected Features
 * that we know about for a given area.
 *
 * Memory copy is a FeatureCollection of LineString (Segments).
 * 
 * @author jett
 *
 */
public class DefaultNetwork implements Network {
    private static final Double LOC_GROUP_RADIUS_DEG = (Double) GeoProperties
            .getInstance().get("group.radius.degrees");
    private static final double BUFFER_TOLERANCE = 0.00007;
    private static DefaultNetwork instance = null;
    private DefaultFeatureCollection featureCollection;
    private Set<Segment> allSegments;
    private List<LineString> allLineStrings = new ArrayList<>();
    private Set<GeoNode> nodeSet;
    private NetworkStore networkStore;
    private LocationStore locationStore;
    private TrackStore trackStore;

    private static Logger logger = Logger.getLogger(DefaultNetwork.class);

    /**
     * @param defaultFeatureCollection
     */
    @Inject
    DefaultNetwork(DefaultFeatureCollection defaultFeatureCollection) {
        featureCollection = defaultFeatureCollection;
        try {
            trackStore = LoadService.getInstance().loadTrackStore();
            locationStore = DefaultLocationStore.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
        init();
    }

    public static DefaultNetwork getInstance() {
        synchronized (DefaultNetwork.class) {
            if (instance == null) {
                instance = new DefaultNetwork();
            }
        }
        return (instance);
    }

    /**
     * Constructor that loads itself from stored location.
     */
    private DefaultNetwork() {
        networkStore = DefaultNetworkStore.getInstance();
        try {
            allSegments = networkStore.getSegments();
            featureCollection = TranslateUtil
                    .segmentsToFeatureCollection(allSegments);
            trackStore = LoadService.getInstance().loadTrackStore();
            locationStore = DefaultLocationStore.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
        init();
    }

    /**
     * Currently sets up - List of Nodes (but not those with IDs) - List of Line
     * Strings (but Segments would be better) - Dumps a string summarizing what
     * we've got.
     * 
     * Moving toward nodes with IDs, it would be good to persist the segments
     * that way, but we're not quite there yet. Dropping in a verification
     * process that checks the endpoints against the LocationStore's idea of our
     * node set.
     * 
     * TODO: Persist the segments with at least the Node IDs so they can be more
     * easily verified. This is a move toward how we'll be persisting in the DB.
     */
    private void init() {
        nodeSet = locationStore.getLocations();
        allLineStrings = new ArrayList<>();

        for (SimpleFeature feature : featureCollection) {
            LineString lineString = (LineString) feature.getDefaultGeometry();
            if (lineString != null) {
                allLineStrings.add(lineString);
            }
        }
        for (Segment segment : allSegments) {
            verifyNodeIdAssignment((GeoNode) segment.getStart());
            verifyNodeIdAssignment((GeoNode) segment.getEnd());
        }
        logger.debug("Initialized : " + this);
    }

    /**
     * @param segment
     */
    public void verifyNodeIdAssignment(GeoNode endPointNode) {
        Integer nodeId = matchesNetworkNode(endPointNode);
        if (nodeId > 0) {
            endPointNode.setId(nodeId);
        } else {
            logger.error("Node " + endPointNode
                    + " doesn't match stored list of nodes");
        }
    }

    private void refresh() {
        nodeSet.clear();
        nodeSet = null;
        allLineStrings.clear();
        allLineStrings = null;
        init();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.clueride.poc.Network#canReach(com.clueride.domain.DefaultGeoNode)
     */
    public boolean canReach(GeoNode connectedNode) {
        for (LineString lineString : allLineStrings) {
            if (lineString.buffer(0.00001).covers(connectedNode.getPoint())) {
                return true;
            }
        }
        return false;
    }

    public Integer matchesNetworkNode(GeoNode geoNode) {
        for (GeoNode node : nodeSet) {
            if (node.matchesLocation(geoNode)) {
                return node.getId();
            }
        }
        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.clueride.poc.Network#add(org.opengis.feature.simple.SimpleFeature)
     * 
     * @deprecated - deal in segments rather than features.
     */
    @Override
    @Deprecated
    public void add(SimpleFeature simpleFeature) {
        featureCollection.add(simpleFeature);
        refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.clueride.poc.Network#evaluateState(com.clueride.domain.Node)
     */
    @Override
    public NodeNetworkState evaluateNodeState(GeoNode geoNode) {
        logger.debug("start - evaluateNodeState()");
        // First Step is to clear any previous state that may have been
        // recorded.
        clearEvaluationState();

        // Now for an evaluation
        NodeNetworkState result = NodeNetworkState.UNDEFINED;
        if (matchesNetworkNode(geoNode) > 0) {
            return recordState(geoNode, NodeNetworkState.ON_NETWORK);
        } else if (withinLocationGroup(geoNode)) {
            return recordState(geoNode, NodeNetworkState.WITHIN_GROUP);
        } else if (addCoveringSegments(geoNode)) {
            return recordState(geoNode, NodeNetworkState.ON_SEGMENT);
        } else {
            addNearestNodes(geoNode);
            IntersectionScore intersectionScore = findMatchingTracks(geoNode);
            int tracksFound = intersectionScore.getTrackCount();
            if (tracksFound == 1) {
                geoNode.setState(NodeNetworkState.ON_SINGLE_TRACK);
                logger.info(geoNode);
                proposeSegmentFromTrack(geoNode, intersectionScore);
            } else if (tracksFound > 1) {
                geoNode.setState(NodeNetworkState.ON_MULTI_TRACK);
                logger.info(geoNode);
            } else {
                geoNode.setState(NodeNetworkState.OFF_NETWORK);
                logger.info(geoNode);
            }
        }
        return geoNode.getState();
    }

    /**
     * Prepare the network state for a new round of evaluations against a new
     * lat/lon location.
     * 
     * At this time, we only clear the selected nodes. There may be more later.
     */
    private void clearEvaluationState() {
        for (GeoNode node : nodeSet) {
            node.setSelected(false);
        }
    }

    /**
     * @param geoNode
     * @param state
     * @return
     */
    private NodeNetworkState recordState(GeoNode geoNode,
            NodeNetworkState state) {
        geoNode.setState(state);
        logger.info(geoNode);
        return state;
    }

    /**
     * @param geoNode
     * @return
     */
    private boolean withinLocationGroup(GeoNode geoNode) {
        boolean isWithin = false;
        Set<NodeGroup> locGroups = DefaultLocationStore.getInstance()
                .getLocationGroups();
        for (NodeGroup nodeGroup : locGroups) {
            Point point = ((DefaultNodeGroup) nodeGroup).getPoint();
            if (point.buffer(LOC_GROUP_RADIUS_DEG).covers(geoNode.getPoint())) {
                isWithin = true;
            }
        }
        return isWithin;
    }

    /**
     * @param geoNode
     */
    private void proposeSegmentFromTrack(GeoNode geoNode,
            IntersectionScore intersectionScore) {
        // Validate we've just got a single track
        if (geoNode.getTrackCount() != 1) {
            throw new IllegalArgumentException(
                    "Expected Node to have exactly one candidate Track");
        }

        // Pick out that track
        SimpleFeature candidateTrackFeature = geoNode.getTracks().get(0);
        logger.info("Candidate Track: "
                + candidateTrackFeature.getAttribute("name"));
        LineString startingTrack = (LineString) geoNode.getTracks().get(0)
                .getDefaultGeometry();

        // Break the track into two pieces, each running away from the geoNode
        // toward either endpoint
        LineString[] splitPair = TranslateUtil.split(startingTrack, geoNode
                .getPoint().getCoordinate(), true);
        LineString lineStringToStart = (LineString) splitPair[0].reverse();
        LineString lineStringToEnd = splitPair[1];

        // Finds intersecting node out of the network's set of nodes plus any
        // candidate nodes we may come up with via intersections and crossings
        List<GeoNode> nodesToMeasure = new ArrayList<>(geoNode.getNearByNodes());

        // Now to see if we intersect the network, and if so, at what point
        // intersectionScore will hold the interesting segment for us to search.
        List<Segment> networkSegment = intersectionScore
                .getIntersectingSegments(candidateTrackFeature);
        if (networkSegment.isEmpty()) {
            throw new IllegalArgumentException(
                    "Expected to have intersected the network");
        }
        LineString lineString = (LineString) TranslateUtil.segmentToFeature(
                networkSegment.get(0)).getDefaultGeometry();
        findIntersectingNode(lineStringToStart, nodesToMeasure, networkSegment,
                lineString);
        findIntersectingNode(lineStringToEnd, nodesToMeasure, networkSegment,
                lineString);

        Map<GeoNode, Double> distanceToNode = evaluateDistanceMapPerNode(
                lineStringToStart, lineStringToEnd, nodesToMeasure);

        for (Entry<GeoNode, Double> entry : distanceToNode.entrySet()) {
            logger.debug(entry.getKey().getId() + ": " + entry.getValue());
        }

        GeoNode selectedNode = null;

        if (distanceToNode.size() == 1) {
            selectedNode = distanceToNode.keySet().iterator().next();
        } else if (distanceToNode.size() > 1) {
            double closest = Double.MAX_VALUE;
            System.out.println("Have more than one matching Node");
            for (Iterator<GeoNode> iter = distanceToNode.keySet().iterator(); iter
                    .hasNext();) {
                GeoNode testNode = iter.next();
                if (closest > distanceToNode.get(testNode)) {
                    closest = distanceToNode.get(testNode);
                    selectedNode = testNode;
                }
            }
        } else {
            logger.error("Unexpected that the track would find no overlapping points");
            return;
        }
        geoNode.setSelectedNode(selectedNode);

        // Node is now chosen; see which part of track to recommend
        LineString lsProposalForSegment;
        if (lineStringToStart.buffer(BUFFER_TOLERANCE).covers(
                selectedNode.getPoint())) {
            lsProposalForSegment = TranslateUtil.split(lineStringToStart,
                    selectedNode.getPoint().getCoordinate(), true)[0];
        } else if (lineStringToEnd.buffer(BUFFER_TOLERANCE).covers(
                selectedNode.getPoint())) {
            lsProposalForSegment = TranslateUtil.split(lineStringToEnd,
                    selectedNode.getPoint().getCoordinate(), true)[0];
        } else {
            logger.error("Did not expect that neither segment was suitable");
            return;
        }

        geoNode.setProposedSegment(TranslateUtil
                .lineStringToSegment(lsProposalForSegment));
        geoNode.getProposedSegment().setName("Proposed");
    }

    /**
     * @param splitPair
     * @param nodesToMeasure
     * @param networkSegment
     * @param lineString
     * @param i
     */
    public void findIntersectingNode(LineString subLineString,
            List<GeoNode> nodesToMeasure, List<Segment> networkSegment,
            LineString lineString) {
        if (subLineString.buffer(BUFFER_TOLERANCE).intersects(lineString)) {
            logger.info("This side of the track intersects with segment "
                    + networkSegment.get(0).getSegId());
            // We do intersect and can walk the lineString to find the node
            int numberPoints = subLineString.getNumPoints();
            for (int p = 0; p < numberPoints; p++) {
                Point walkingPoint = subLineString.getPointN(p);
                if (lineString.buffer(BUFFER_TOLERANCE).covers(walkingPoint)) {
                    logger.info("We like point " + walkingPoint);
                    nodesToMeasure.add(NodeFactory
                            .getInstance(walkingPoint));
                    // Stop as soon as we find something interesting
                    break;
                    // geoNode.setSelectedNode(selectedNode);
                    // return;
                }
            }
        }
    }

    /**
     * @param lineStringToStartsplitPair
     * @param nodesToMeasure
     * @return
     */
    public Map<GeoNode, Double> evaluateDistanceMapPerNode(
            LineString lineStringToStart, LineString lineStringToEnd,
            List<GeoNode> nodesToMeasure) {
        Map<GeoNode, Double> distanceToNode = new HashMap<>();
        for (GeoNode node : nodesToMeasure) {
            determineDistance(lineStringToStart, distanceToNode, node);
            determineDistance(lineStringToEnd, distanceToNode, node);
        }
        return distanceToNode;
    }

    /**
     * @param lineStringToStart
     * @param distanceToNode
     * @param node
     */
    public void determineDistance(LineString lineStringToStart,
            Map<GeoNode, Double> distanceToNode, GeoNode node) {
        if (lineStringToStart.buffer(BUFFER_TOLERANCE).covers(node.getPoint())) {
            double distance = LengthToPoint.length(lineStringToStart, node
                    .getPoint().getCoordinate());
            logger.debug("Node: " + node.getName()
                    + " at a distance of "
                    + distance);
            distanceToNode.put(node, distance);
        }
    }

    /**
     * After having found this node is not on the network or a segment, we check
     * the TrackStore for any tracks that cover both this point and the nearest
     * nodes on the network.
     * 
     * Those are then added to the node.
     * 
     * If we find matching tracks, record the segments/nodes where the
     * intersection occurs to avoid having to perform another search later.
     * 
     * @param geoNode
     */
    private IntersectionScore findMatchingTracks(GeoNode geoNode) {
        IntersectionScore score = new IntersectionScore(geoNode);
        // Find tracks covering the input node
        List<SimpleFeature> candidateTracks = new ArrayList<>();
        for (SimpleFeature track : trackStore.getFeatures()) {
            LineString lineString = (LineString) track.getDefaultGeometry();
            if (lineString.buffer(BUFFER_TOLERANCE).covers(geoNode.getPoint())) {
                candidateTracks.add(track);
            }
        }

        // Out of the covering tracks, which pass through our nearest nodes?
        for (SimpleFeature track : candidateTracks) {
            boolean keepTrack = false;
            LineString lineString = (LineString) track.getDefaultGeometry();
            // Check our list of nodes
            for (GeoNode node : geoNode.getNearByNodes()) {
                if (lineString.buffer(0.00001).covers(node.getPoint())) {
                    score.addTrackConnectingNode(track, node);
                    keepTrack = true;
                }
            }

            // Check our list of Location Groups
            for (NodeGroup nodeGroup : DefaultLocationStore.getInstance()
                    .getLocationGroups()) {
                Point point = ((DefaultGeoNode) nodeGroup).getPoint();
                if (point.buffer(LOC_GROUP_RADIUS_DEG).intersects(lineString)) {
                    score.addTrackConnectingNode(track, nodeGroup);
                    keepTrack = true;
                }
            }

            // Check our list of Segments for crossings and intersections
            for (SimpleFeature segmentFeature : featureCollection) {
                LineString segmentLineString = (LineString) segmentFeature
                        .getDefaultGeometry();
                Segment segment = TranslateUtil
                        .featureToSegment(segmentFeature);
                System.out.println("Checking " + track.getAttribute("url")
                        + " against Segment of length "
                        + segmentLineString.getNumPoints());
                if (segmentLineString.crosses(lineString)) {
                    score.addCrossingTrack(track, segment);
                    keepTrack = true;
                } else if (segmentLineString.intersects(lineString)) {
                    score.addIntersectingTrack(track, segment);
                    keepTrack = true;
                }
            }

            if (keepTrack) {
                geoNode.addTrack(track);
            }
        }
        logger.info(score);
        return score;
    }

    /**
     * Adds a "handful" of the nearest nodes currently on the network.
     * 
     * Here's where some exploring will occur:
     * 
     * <UL>
     * <LI>Do we sort the list of nodes and then pick a few from the top?
     * <LI>Do we apply a filter with a given distance?
     * <LI>Other options?
     * </UL>
     * 
     * We'll also be interested in a similar algorithm with Tracks and the
     * intersection of those tracks with nearby segments containing the nodes.
     * 
     * @param geoNode
     */
    private void addNearestNodes(GeoNode geoNode) {
        List<GeoNode> nearestNodes = new ArrayList<>();
        // Trying to sort the nodes
        List<GeoNode> orderedNodes = getSortedNodes(geoNode);
        geoNode.setNearByNodes(orderedNodes);
    }

    /**
     * @param geoNode
     * @return
     */
    public List<GeoNode> getSortedNodes(GeoNode geoNode) {
        final Geometry referencePoint = geoNode.getPoint();
        List<GeoNode> sorted = new ArrayList<>();
        sorted.addAll(nodeSet);
        Collections.sort(sorted, new Comparator<GeoNode>() {

            @Override
            public int compare(GeoNode o1, GeoNode o2) {
                double dist1 = o1.getPoint().distance(referencePoint);
                double dist2 = o2.getPoint().distance(referencePoint);
                return (int) Math.signum(dist1 - dist2);
            }

        });

        return sorted;
    }

    /**
     * After checking that this node is not already in the Network, we check
     * existing segments to see if any of them cover the node.
     * 
     * @param geoNode
     * @return true if we were able to find suitable segments.
     */
    private boolean addCoveringSegments(GeoNode geoNode) {
        boolean result = false;
        for (SimpleFeature feature : featureCollection) {

            LineString lineString = (LineString) feature.getDefaultGeometry();
            if (lineString.buffer(BUFFER_TOLERANCE).covers(geoNode.getPoint())) {
                geoNode.addSegment(TranslateUtil.featureToSegment(feature));
                result = true;
            }
        }
        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DefaultNetwork [allSegmentsCount=" + featureCollection.size()
                + ", allLineStringsCount=" + allLineStrings.size()
                + ", nodeSetCount=" + nodeSet.size() + ", trackStoreCount="
                + trackStore.getFeatures().size() + "]";
    }

    /**
     * @return
     */
    public String getNetworkForDisplay() {
        String result = "";
        JsonUtil jsonUtil = new JsonUtil(JsonStoreType.LOCATION);
        result = jsonUtil.toString(featureCollection);
        return result;
    }
}
