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

import com.clueride.config.GeoProperties;
import com.clueride.dao.*;
import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.DefaultNodeGroup;
import com.clueride.domain.EdgeImpl;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.*;
import com.clueride.domain.factory.LineFeatureFactory;
import com.clueride.domain.factory.NodeFactory;
import com.clueride.feature.Edge;
import com.clueride.feature.LineFeature;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.score.IntersectionScore;
import com.clueride.geo.score.TrackScore;
import com.clueride.io.GeoJsonUtil;
import com.clueride.io.JsonStoreType;
import com.clueride.poc.geotools.TrackStore;
import com.clueride.service.SegmentService;
import com.clueride.service.builder.NewLocRecBuilder;
import com.vividsolutions.jts.algorithm.LineIntersector;
import com.vividsolutions.jts.algorithm.RobustLineIntersector;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import org.apache.log4j.Logger;
import org.geotools.feature.DefaultFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import static com.clueride.geo.SplitLineString.END;
import static com.clueride.geo.SplitLineString.START;

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
    // private static final double BUFFER_TOLERANCE = 0.00007;
    private static DefaultNetwork instance = null;
    // TODO: Move from featureCollection to reliance on the Stores
    private DefaultFeatureCollection featureCollection;
    private Set<LineFeature> allLineFeatures;
    private List<LineString> allLineStrings = new ArrayList<>();
    private Set<GeoNode> nodeSet;
    private NetworkStore networkStore;
    private NodeStore nodeStore;
    private TrackStore trackStore;
    private int count;

    private static final Logger LOGGER = Logger.getLogger(DefaultNetwork.class);

    public static DefaultNetwork getInstance() {
        // TODO: This is a big chunk of work to bite off within a synchronized block
        // TODO: Move to Guice Dependency injection
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
            allLineFeatures = networkStore.getLineFeatures();
            // TODO: Move from featureCollection to reliance on the Stores
            featureCollection = TranslateUtil
                    .lineFeatureSetToFeatureCollection(allLineFeatures);

            trackStore = LoadService.getInstance().loadTrackStore();
            nodeStore = DefaultNodeStore.getInstance();
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
     * process that checks the endpoints against the NodeStore's idea of our
     * node set.
     *
     */
    private void init() {
        nodeSet = nodeStore.getNodes();
        allLineStrings = new ArrayList<>();

        // TODO: Move from featureCollection to reliance on the Stores
        for (SimpleFeature feature : featureCollection) {
            LineString lineString = (LineString) feature.getDefaultGeometry();
            if (lineString != null) {
                allLineStrings.add(lineString);
            }
        }
        for (LineFeature lineFeature : allLineFeatures) {
            // TODO: Put this back in.
            // verifyNodeIdAssignment((GeoNode) lineFeature.getStart());
            // verifyNodeIdAssignment((GeoNode) lineFeature.getEnd());
        }
        LOGGER.debug("Initialized : " + this);
    }

    /**
     * @param defaultFeatureCollection
     * @deprecated Use the getInstance method instead.
     */
    @Inject
    DefaultNetwork(DefaultFeatureCollection defaultFeatureCollection) {
        // TODO: Move from featureCollection to reliance on the Stores
        featureCollection = defaultFeatureCollection;
        try {
            trackStore = LoadService.getInstance().loadTrackStore();
            nodeStore = DefaultNodeStore.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
        init();
    }

    /**
     * TODO: Turn this into a method that accepts Edge.
     * TODO: Factor this into a separate class.
     * 
     * @param endPointNode
     */
    public void verifyNodeIdAssignment(GeoNode endPointNode) {
        Integer nodeId = matchesNetworkNode(endPointNode);
        if (nodeId > 0) {
            endPointNode.setId(nodeId);
        } else {
            LOGGER.error("Node " + endPointNode
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
     * @deprecated - deal in edges and segmentFeatures rather than
     * simpleFeatures.
     */
    @Override
    @Deprecated
    public void add(TrackFeature trackFeature) {
        // TODO: Move from featureCollection to reliance on the Stores
        featureCollection.add(trackFeature.getFeature());
        refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.clueride.poc.Network#evaluateState(com.clueride.domain.Node)
     * 
     * @deprecated - using LocationService.buildProposalForNewNode instead.
     */
    @Override
    public NetworkProposal evaluateNodeState(GeoNode geoNode) {
        LOGGER.debug("start - evaluateNodeState()" + count++);
        NewLocProposal newLocProposal = new NewLocProposal(geoNode);
        NewLocRecBuilder recBuilder = new NewLocRecBuilder(geoNode);

        // Check if our node happens to already be on the network list of nodes
        Integer matchingNodeId = matchesNetworkNode(geoNode);
        if (matchingNodeId > 0) {
            // Found matching node; add as a recommendation
            newLocProposal.add(recBuilder.onNode(matchingNodeId));
            return recordState(newLocProposal, NodeNetworkState.ON_NETWORK);
        }

        // Check if our node happens to be on the network list of node groups
        Integer matchingNodeGroupId = withinLocationGroup(geoNode);
        if (matchingNodeGroupId > 0) {
            newLocProposal.add(recBuilder.onNode(matchingNodeGroupId));
            return recordState(newLocProposal, NodeNetworkState.WITHIN_GROUP);
        } else if (addCoveringSegments(geoNode)) {
            return recordState(newLocProposal, NodeNetworkState.ON_SEGMENT);
        } else {
            // TODO: Nodes probably should be picked up inside the
            // track/intersection evaluation.
            // TODO: NearByNodes
            addNearestNodes(geoNode);

            // TODO: Move each of these toward NetworkRecommendation proposals
            IntersectionScore intersectionScore = findMatchingTracks(geoNode);
            int tracksFound = intersectionScore.getTrackCount();
            if (tracksFound == 1) {
                geoNode.setState(NodeNetworkState.ON_SINGLE_TRACK);
                LOGGER.info(geoNode);
                proposeSegmentFromTrack(geoNode, intersectionScore);
            } else if (tracksFound > 1) {
                geoNode.setState(NodeNetworkState.ON_MULTI_TRACK);
                LOGGER.info(geoNode);
                proposeSegmentFromTrack(geoNode, intersectionScore);
            } else {
                geoNode.setState(NodeNetworkState.OFF_NETWORK);
                LOGGER.info(geoNode);
            }
        }
        // return geoNode.getState();
        return newLocProposal;
    }

    /**
     * Prepare the network state for a new round of evaluations against a new
     * lat/lon location.
     * 
     * At this time, we only clear the selected nodes. There may be more later.
     * private void clearEvaluationState() { for (GeoNode node : nodeSet) {
     * node.setSelected(false); } }
     */

    /**
     * @param newLocProposal
     * @param state
     * @return
     * @deprecated - Because it is setting the nodeNetworkState
     */
    private NetworkProposal recordState(NewLocProposal newLocProposal,
            NodeNetworkState state) {
        // newLocProposal.setNodeNetworkState(state);
        LOGGER.info(newLocProposal);
        return newLocProposal;
    }

    /**
     * @param geoNode
     * @return
     */
    private Integer withinLocationGroup(GeoNode geoNode) {
        Integer matchingId = -1;
        Set<NodeGroup> locGroups = DefaultNodeStore.getInstance()
                .getLocationGroups();
        for (NodeGroup nodeGroup : locGroups) {
            Point point = ((DefaultNodeGroup) nodeGroup).getPoint();
            if (point.buffer(LOC_GROUP_RADIUS_DEG).covers(geoNode.getPoint())) {
                matchingId = nodeGroup.getId();
            }
        }
        return matchingId;
    }

    /**
     * @param geoNode
     */
    private void proposeSegmentFromTrack(GeoNode geoNode,
            IntersectionScore intersectionScore) {
        LOGGER.info("start - proposeSegmentFromTrack()");

        // Pick out best track and best node
        TrackScore trackScore = intersectionScore.fetchBestTrackScore();
        trackScore.refreshIndices();
        System.out.println(trackScore.dumpScores());
        geoNode.addScoredNode(trackScore.getProposedNode());
        SimpleFeature candidateTrackFeature = geoNode.getTracks().get(0);
        LOGGER.info("Candidate Track: "
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
        Edge bestSegment = intersectionScore.getBestSegment();
        LineString lineString = (LineString) bestSegment.getFeature()
                .getDefaultGeometry();
        findIntersectingNode(lineStringToStart, nodesToMeasure, bestSegment,
                lineString);
        findIntersectingNode(lineStringToEnd, nodesToMeasure, bestSegment,
                lineString);

        Map<GeoNode, Double> distanceToNode = evaluateDistanceMapPerNode(
                lineStringToStart, lineStringToEnd, nodesToMeasure);

        for (Entry<GeoNode, Double> entry : distanceToNode.entrySet()) {
            LOGGER.debug(entry.getKey().getId() + ": " + entry.getValue());
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
            LOGGER.error("Unexpected that the track would find no overlapping points");
            return;
        }
        geoNode.setSelectedNode(selectedNode);

        // Node is now chosen; see which part of track to recommend
        LineString lsProposalForSegment;
        if (lineStringToStart.buffer(GeoProperties.BUFFER_TOLERANCE).covers(
                selectedNode.getPoint())) {
            lsProposalForSegment = TranslateUtil.split(lineStringToStart,
                    selectedNode.getPoint().getCoordinate(), true)[0];
        } else if (lineStringToEnd.buffer(GeoProperties.BUFFER_TOLERANCE)
                .covers(
                        selectedNode.getPoint())) {
            lsProposalForSegment = TranslateUtil.split(lineStringToEnd,
                    selectedNode.getPoint().getCoordinate(), true)[0];
        } else {
            LOGGER.error("Did not expect that neither segment was suitable");
            return;
        }

        geoNode.setProposedTrack(LineFeatureFactory
                .getProposal(lsProposalForSegment));
        geoNode.getProposedTrack().setDisplayName("Proposed");

        // Before we head back, let's build out the NodeEvaluationStatus object
        NodeEvaluationStatus nodeEvaluation = NetworkState
                .getNodeEvaluationInstance();
        nodeEvaluation
                .setProposedSegmentFromTrack(geoNode.getProposedTrack());
        nodeEvaluation.setIntersectedSegment(bestSegment);
    }

    /**
     * @param splitPair
     * @param nodesToMeasure
     * @param networkSegment
     * @param lineString
     * @param i
     */
    public void findIntersectingNode(LineString subLineString,
            List<GeoNode> nodesToMeasure, Edge bestSegment,
            LineString lineString) {
        if (subLineString.buffer(GeoProperties.BUFFER_TOLERANCE).intersects(
                lineString)) {
            LOGGER.info("This side of the track intersects with segment "
                    + bestSegment.getId());
            // We do intersect and can walk the lineString to find the node
            int numberPoints = subLineString.getNumPoints();
            for (int p = 0; p < numberPoints; p++) {
                Point walkingPoint = subLineString.getPointN(p);
                if (lineString.buffer(GeoProperties.BUFFER_TOLERANCE).covers(
                        walkingPoint)) {
                    LOGGER.info("We like point " + walkingPoint);
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
        if (lineStringToStart.buffer(GeoProperties.BUFFER_TOLERANCE).covers(
                node.getPoint())) {
            double distance = LengthToPoint.length(lineStringToStart, node
                    .getPoint().getCoordinate());
            LOGGER.debug("Node: " + node.getName()
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
     * TODO: Add them to the NetworkRecommendation instead of the node.
     * 
     * If we find matching tracks, record the segments/nodes where the
     * intersection occurs to avoid having to perform another search later.
     * 
     * If the track is a crossing track, modify the track so it becomes an
     * intersecting track -- insert the point into the track.
     * 
     * @param geoNode
     */
    private IntersectionScore findMatchingTracks(GeoNode geoNode) {
        LOGGER.info("start - findMatchingTracks");
        IntersectionScore score = new IntersectionScore(geoNode);
        // Find tracks covering the input node
        List<TrackFeature> candidateTracks = findTracksThroughNode(geoNode);

        // Out of the covering tracks, which pass through our nearest nodes?
        for (TrackFeature trackFeature : candidateTracks) {
            // TODO: turn keepTrack into a method off the eval/builder.
            boolean keepTrack = false;
            LineString trackLineString = trackFeature.getLineString();

            // Check our list of nodes
            // TODO: NearByNodes
            for (GeoNode node : geoNode.getNearByNodes()) {
                if (trackLineString.buffer(GeoProperties.NODE_TOLERANCE)
                        .covers(node.getPoint())) {
                    score.addTrackConnectingNode(trackFeature, node);
                    keepTrack = true;
                }
            }

            // Check our list of Location Groups
            for (NodeGroup nodeGroup : DefaultNodeStore.getInstance()
                    .getLocationGroups()) {
                Point point = ((DefaultGeoNode) nodeGroup).getPoint();
                if (point.buffer(LOC_GROUP_RADIUS_DEG).intersects(
                        trackLineString)) {
                    score.addTrackConnectingNode(trackFeature, nodeGroup);
                    keepTrack = true;
                }
            }

            // Check our list of Segments for crossings and intersections
            // TODO: SimpleFeature -> Edge
            // TODO: This only handles the edges and ignores the segments;
            // Segments as edges?
            // TODO: Move from featureCollection to reliance on the Stores
            for (Edge edge : DefaultNetworkStore.getInstance()
                    .getEdges()) {
                // SegmentFeature segmentFeature = new SegmentFeatureImpl(
                // edge);
                LineString segmentLineString = edge.getLineString();
                System.out.print("Checking " + trackFeature.getUrl()
                        + " against Edge " + edge.getId()
                        + " of length "
                        + segmentLineString.getNumPoints());

                // Test for Intersect (more restrictive) first - CRANG-13
                if (segmentLineString.intersects(trackLineString)) {
                    System.out.println(" Intersects");
                    score.addIntersectingTrack(trackFeature, edge);
                    keepTrack = true;
                } else if (segmentLineString.crosses(trackLineString)) {
                    System.out.println(" Crosses");
                    LineString intersectingTrackLineString = crossingTrackToIntersectingTrack(
                            geoNode, trackLineString, segmentLineString);
                    score.addIntersectingTrack(LineFeatureFactory
                            .getProposal(intersectingTrackLineString),
                            edge);
                    keepTrack = true;
                } else {
                    System.out.println();
                }
            }

            if (keepTrack) {
                geoNode.addTrack(trackFeature);
            }
        }
        LOGGER.info(score);
        return score;
    }

    /**
     * Accepts the business objects SimpleFeature (wrapping LineString Geometry)
     * and a Segment (also wrapping a LineString Geometry) which have been
     * confirmed to *cross* each other, and will insert the intersecting point
     * into the SimpleFeature's geometry to come up with an intersecting pair.
     * 
     * There are a number of ways to do this. The approach taken here is to walk
     * the track and chop off leading points until we find a remaining
     * LineString that no longer crosses the segment. That last pair of
     * coordinates will be one of the line segments that we hold onto for a
     * subsequent step of determining the crossing point.
     * 
     * Then, based on the boundary of that line segment, we look at points on
     * the Segment which lie within the boundary. If we don't have at least two,
     * we increase the size of the boundary until we do have two. If we have
     * more than two, we walk those points using the same crossing algorithm.
     * 
     * At this point, we should have two line segments, each consisting of a
     * single pair of coordinates. Those four coordinates are what is passed to
     * the LineIntesector tool. That tool can provide us with the point where
     * the two line segments cross, and thus the point that we're looking for.
     * 
     * Once we have that point, we insert it into the track at the appropriate
     * point, and then test that the track and segment are intersecting.
     * 
     * @param geoNode
     *            - Location which we're attempting to connect to the network
     * @param track
     *            - SimpleFeature representing the track we've already
     *            determined crosses the network segment.
     * @param segment
     *            - Segment which is already part of the network.
     * @return SimpleFeature - wrapping a LineString that holds the original
     *         track with the intersecting point inserted.
     */
    private LineString crossingTrackToIntersectingTrack(GeoNode geoNode,
            LineString trackLineString,
            LineString segmentLineString) {

        // Obtain the underlying LineStrings
        SplitLineString splitPair = new SplitLineString(trackLineString,
                geoNode);

        // LineString segmentCrossingSubLineString;

        LineString workingLineString;
        if (splitPair.getSubLineString(START).crosses(segmentLineString)) {
            workingLineString = splitPair.getSubLineString(START);
        } else if (splitPair.getSubLineString(END).crosses(segmentLineString)) {
            workingLineString = splitPair.getSubLineString(END);
        } else {
            throw new RuntimeException("Unexpected that track doesn't cross");
        }

        int trackIndex = IntersectionUtil.findCrossingIndex(workingLineString,
                segmentLineString);
        LineString trackPiece = IntersectionUtil.retrieveCrossingPair(
                workingLineString, trackIndex);
        LineString segmentPiece = IntersectionUtil.findCrossingPair(
                segmentLineString,
                trackPiece);

        // Now we have two 2-point LineStrings which we can pass to the
        // LineIntersector

        LineIntersector lineIntersector = new RobustLineIntersector();
        lineIntersector.computeIntersection(
                trackPiece.getStartPoint().getCoordinate(),
                trackPiece.getEndPoint().getCoordinate(),
                segmentPiece.getStartPoint().getCoordinate(),
                segmentPiece.getEndPoint().getCoordinate()
                );
        System.out.println(trackPiece);
        System.out.println(segmentPiece);

        Coordinate intersect = lineIntersector.getIntersection(0);
        System.out.println("Intersection at " + intersect);

        Coordinate[] originalCoordinates = workingLineString.getCoordinates();
        List<Coordinate> reconstructionCoordinates = new ArrayList<>();
        reconstructionCoordinates.addAll(Arrays.asList(Arrays.copyOfRange(
                originalCoordinates, 0, trackIndex)));
        reconstructionCoordinates.add(intersect);
        reconstructionCoordinates.addAll(Arrays.asList(Arrays.copyOfRange(
                originalCoordinates, trackIndex, originalCoordinates.length)));

        // Check lengths; should be one point longer than before
        int lengthDiff = reconstructionCoordinates.size()
                - originalCoordinates.length;
        System.out.println("Difference in length is " + lengthDiff);

        Coordinate[] coord = new Coordinate[reconstructionCoordinates.size()];
        reconstructionCoordinates.toArray(coord);

        LineString reconstructedLineString = segmentLineString.getFactory()
                .createLineString(coord);

        // Check that the intersection now occurs

        return reconstructedLineString;
    }

    /**
     * TODO: Move to separate class.
     * 
     * @param reconstructionCoordinates
     */
    @SuppressWarnings("unused")
    private void dumpCoordinates(List<Coordinate> coordinates) {
        int i = 0;
        for (Coordinate coordinate : coordinates) {
            System.out.println(i++ + ": " + coordinate);
        }
    }

    /**
     * For a given location -- not on the existing network -- search for a list
     * of {@link TrackFeature}s that "cover" the node, tracks that could serve
     * as potential new {@link Edge}s for the {@link Network}.
     * 
     * NOTE: THIS IS PROBABLY BETTER OFF BEING MOVED TO A DIFFERENT CLASS that
     * is responsible for building track-based recommendations.
     * 
     * @param geoNode
     * @return
     * @deprecated - Using {@link GeoEval.listCoveringTracks()} instead.
     */
    private List<TrackFeature> findTracksThroughNode(GeoNode geoNode) {
        List<TrackFeature> candidateTracks = new ArrayList<>();
        for (TrackFeature track : trackStore.getFeatures()) {
            LineString lineString = (LineString) track.getFeature()
                    .getDefaultGeometry();
            if (lineString.buffer(GeoProperties.BUFFER_TOLERANCE).covers(
                    geoNode.getPoint())) {
                candidateTracks.add(track);
            }
        }
        return candidateTracks;
    }

    /**
     * User has indicated that our last Node is the one we want to connect.
     * 
     * Bring in the last evaluation we made and use that data to construct the
     * new Network objects.
     * 
     * Invokes the Segment service as needed to add any new segments that are
     * involved, but we handle our Nodes here.
     * 
     * @deprecated - in favor of LocationService.confirmNewLocation().
     * @return
     */
    public String confirmNewLocation() {
        NodeEvaluationStatus nodeEvaluation = NetworkState
                .getLastNodeEvaluation();

        // As a first cut, we're assuming a new segment, two new nodes, and that
        // we're splitting the original segment at the end node of the first
        // segment.

        Edge brandNewSegment = nodeEvaluation
                .getProposedSegmentFromTrack();
        GeoNode startNode = (GeoNode) brandNewSegment.getStart();
        GeoNode endNode = (GeoNode) brandNewSegment.getEnd();

        // Save our new nodes
        nodeStore.addNew(startNode);
        nodeStore.addNew(endNode);
        try {
            nodeStore.persistAndReload();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add and save our new segments (using SegmentService)
        brandNewSegment.setDisplayName("unnamed");
        SegmentService.addSegment(brandNewSegment);
        Edge existingSegmentToSplit = nodeEvaluation.getIntersectedSegment();
        SegmentService.splitSegment(existingSegmentToSplit, endNode);
        SegmentService.saveChanges();
        allLineFeatures = networkStore.getLineFeatures();
        // TODO: Move from featureCollection to reliance on the Stores
        featureCollection = TranslateUtil
                .lineFeatureSetToFeatureCollection(allLineFeatures);
        refresh();
        return "{\"status\": \"OK\"}";
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
     * TODO: Prepare nearest nodes instead of all of them.
     * 
     * @param geoNode
     */
    private void addNearestNodes(GeoNode geoNode) {
        // List<GeoNode> nearestNodes = new ArrayList<>();
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
     * @deprecated - Using {@link GeoEval.matchesSegmentId()} instead.
     */
    private boolean addCoveringSegments(GeoNode geoNode) {
        boolean result = false;
        for (SimpleFeature feature : featureCollection) {

            LineString lineString = (LineString) feature.getDefaultGeometry();
            if (lineString.buffer(GeoProperties.BUFFER_TOLERANCE).covers(
                    geoNode.getPoint())) {
                geoNode.addSegment(new EdgeImpl(feature));
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
        LOGGER.debug("Requesting network for display");
        String result = "";
        GeoJsonUtil geoJsonUtil = new GeoJsonUtil(JsonStoreType.LOCATION);
        // TODO: Move from featureCollection to reliance on the Stores
        result = geoJsonUtil.toString(featureCollection);
        return result;
    }
}
