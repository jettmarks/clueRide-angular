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
 * Created Aug 31, 2015
 */
package com.clueride.geo.score;

import com.clueride.config.GeoProperties;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkRecommendation;
import com.clueride.domain.dev.Node;
import com.clueride.domain.factory.NodeFactory;
import com.clueride.domain.factory.PointFactory;
import com.clueride.feature.Edge;
import com.clueride.feature.LineFeature;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.LengthToPoint;
import com.clueride.geo.SplitLineString;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.clueride.geo.SplitLineString.END;
import static com.clueride.geo.SplitLineString.START;

/**
 * For a given Track, score how well it provides a connection between a proposed
 * point and the network as represented by nodes and segments that either cross
 * or intersect with this track.
 * 
 * Early version merely collected the data. Now we score the various segments to
 * determine which provides best (usually closest) connection for the track.
 * 
 * This also looks for new candidate nodes by walking the track and locating
 * spots where the track crosses or intersects the network.
 *
 * @author jett
 *
 */
public class TrackScoreImpl implements TrackScore {
    private static final Logger logger = Logger.getLogger(TrackScore.class);

    // TODO: Review this class's use of the new LineFeature hierarchy.
    /** The TrackFeature we're evaluating. */
    private TrackFeature track;
    /** Location on the track we're connecting to the network. */
    private GeoNode subjectGeoNode;

    /** Holds the two pieces from the node out to each end of the track. */
    private final SplitLineString splitLineString;

    private List<Node> networkNodes = new ArrayList<>();
    private List<Edge> crossingSegments = new ArrayList<>();
    private List<Edge> intersectingSegments = new ArrayList<>();

    /** Lowest score is best. */
    private GeoNode proposedNode = null;
    private static List<GeoNode> nodeProposals = new ArrayList<>();
    private Edge bestSegment = null;

    private static final Map<Integer, Edge> segmentIndex = new HashMap<>();
    private static final Map<Integer, SubTrackScore> scorePerSegment = new HashMap<>();

    /**
     * Constructor accepting what we need to score the track.
     * 
     * @param track
     *            - TrackFeature for which we're computing the score.
     * @param geoNode
     *            - GeoNode -- which is on the track -- that we're trying to
     *            connect to the network.
     */
    @Inject
    public TrackScoreImpl(TrackFeature track, GeoNode geoNode) {
        this.track = track;
        this.subjectGeoNode = geoNode;
        splitLineString = new SplitLineString(track, geoNode);
    }

    /**
     * Collects list of Network Nodes that this track passes through.
     * 
     * @param node
     */
    public void addNode(Node node) {
        networkNodes.add(node);
    }

    public void addCrossingSegment(Edge edge) {
        crossingSegments.add(edge);
    }

    public void addIntersectingSegments(Edge edge) {
        intersectingSegments.add(edge);
    }

    /**
     * @return the track
     */
    public LineFeature getTrack() {
        return track;
    }

    /**
     * @return the subjectGeoNode
     */
    public GeoNode getSubjectGeoNode() {
        return subjectGeoNode;
    }

    /**
     * @return the networkNodes
     */
    public List<Node> getNetworkNodes() {
        return networkNodes;
    }

    /**
     * @return the crossingSegments
     */
    public List<Edge> getCrossingSegments() {
        return crossingSegments;
    }

    /**
     * @return the intersectingSegments
     */
    public List<Edge> getIntersectingSegments() {
        return intersectingSegments;
    }

    public int getIntersectingSegmentCount() {
        return intersectingSegments.size();
    }

    public int getCrossingSegmentCount() {
        return crossingSegments.size();
    }

    /**
     * Laziness: re-index everything when the caller wants it to be re-indexed.
     * 
     * TODO: Lump the two sets of segments together for this evaluation; we only
     * need to treat them different in the case we might have trouble finding a
     * node to represent the intersection for crossing segments.
     * 
     * @return
     */
    public int refreshIndices() {
        segmentIndex.clear();
        scorePerSegment.clear();
        nodeProposals.clear();

        // Bundle all segments into a list
        List<Edge> allSegments = new ArrayList<>(intersectingSegments);
        allSegments.addAll(crossingSegments);

        // TODO: Come back to these later
        // segmentIndex.put(segment.getSegId(), segment);
        // scorePerSegment.put(segment.getSegId(), calculatedScore);

        SubTrackScore lowScoreTowardStart = proposeBestScore(
                splitLineString.getSubLineString(START), allSegments);
        SubTrackScore lowScoreTowardEnd = proposeBestScore(
                splitLineString.getSubLineString(END), allSegments);

        // Have one score if we found something on one end, but two if there
        // were segments on both ends. We choose the closer one to recommend
        // first.

        SubTrackScore selectedScore;
        // TODO: Handle returning both segments
        if (lowScoreTowardStart.hasScore() && lowScoreTowardEnd.hasScore()) {
            logger.info("Found two intersections with the network");
            // Choose between the two
            selectedScore = (lowScoreTowardStart.getScore() < lowScoreTowardEnd
                    .getScore()) ?
                    lowScoreTowardStart : lowScoreTowardEnd;
        } else if (lowScoreTowardStart.hasScore()) {
            selectedScore = lowScoreTowardStart;
        } else if (lowScoreTowardEnd.hasScore()) {
            selectedScore = lowScoreTowardEnd;
        } else {
            throw new RuntimeException(
                    "Didn't find any intersecting/crossing nodes");
        }
        logger.info("Low Score Segment: "
                + selectedScore.getBestSegment().getId());
        proposedNode = selectedScore.getBestNode();
        return allSegments.size();
    }

    /**
     * For a given subTrack, run through the list of segments to score and
     * record any nodes we might want to create.
     * 
     * @param allSegments
     *            - list of segments to run through.
     * @param
     * @return
     */
    SubTrackScore proposeBestScore(LineString subTrackLineString,
            List<Edge> allSegments) {
        GeoNode lowScoreNode = null;
        double score = Double.MAX_VALUE;
        for (Edge segment : allSegments) {
            SubTrackScore calculatedScore = score(segment, subTrackLineString);
            if (calculatedScore.getScore() < score) {
                score = calculatedScore.getScore();
                lowScoreNode = calculatedScore.getBestNode();
                bestSegment = segment;
            }
        }
        return new SubTrackScore(bestSegment, lowScoreNode, score);
    }

    /**
     * First cut at this is to follow the track from the node to the segment and
     * add up the distance.
     * 
     * Trouble spot is knowing which direction to go and whether or not the
     * track has to be reversed.
     * 
     * Another trouble spot for this method is two segments that cross, but the
     * points are not within the tolerance. The incoming subTrack should be
     * prepared to cross the segments with a coordinate within the tolerance at
     * the point of intersection. The existing segment will cover that point.
     * 
     * @param segment
     *            - the existing network segment to evaluate.
     * @param lineString
     *            - the subTrack to be scored.
     * @return SubTrackScore representing the node, segment and score.
     */
    SubTrackScore score(Edge segment,
            LineString subTrackLineString)
            throws RuntimeException {

        // Walk this subTrack up to the segment and if this isn't a
        // node, add it as a proposed node.
        LineString segmentLineString = segment.getLineString();

        Coordinate coordinateIntersection = null;
        GeoNode newGeoNode = null;
        Double distance = Double.MAX_VALUE;
        Polygon segmentWithBuffer = (Polygon) segmentLineString
                .buffer(GeoProperties.BUFFER_TOLERANCE);

        boolean foundIntersection = false;
        for (Coordinate coordinate : subTrackLineString.getCoordinates()) {
            Point point = PointFactory.getInstance(coordinate);
            if (segmentWithBuffer.covers(point)) {
                newGeoNode = NodeFactory.getInstance(point);
                coordinateIntersection = coordinate;
                nodeProposals.add(newGeoNode);
                foundIntersection = true;
                break;
            }
        }
        if (foundIntersection) {
            distance = LengthToPoint
                    .length(subTrackLineString, coordinateIntersection);
        }
        return new SubTrackScore(segment, newGeoNode, distance);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TrackScore [getIntersectingSegmentCount()="
                + getIntersectingSegmentCount()
                + ", getCrossingSegmentCount()=" + getCrossingSegmentCount()
                + "]";
    }

    public String dumpScores() {
        StringBuffer buffer = new StringBuffer();
        for (Entry<Integer, SubTrackScore> entry : scorePerSegment.entrySet()) {
            buffer.append("ID: ").append(entry.getKey())
                    .append(" Score: ").append(entry.getValue()).append("\n");
        }
        return buffer.toString();
    }

    /**
     * @return the proposedNode
     */
    public GeoNode getProposedNode() {
        return proposedNode;
    }

    /**
     * @return
     */
    public Edge getBestSegment() {
        return bestSegment;
    }

    /**
     * @see com.clueride.geo.score.TrackScore#getNetworkRecommendation()
     */
    @Override
    public NetworkRecommendation getNetworkRecommendation() {
        // TODO Auto-generated method stub
        return null;
    }
}
