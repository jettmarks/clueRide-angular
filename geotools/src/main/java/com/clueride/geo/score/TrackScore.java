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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.opengis.feature.simple.SimpleFeature;

import com.clueride.config.GeoProperties;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.Node;
import com.clueride.domain.dev.Segment;
import com.clueride.domain.factory.NodeFactory;
import com.clueride.domain.factory.PointFactory;
import com.clueride.geo.LengthToPoint;
import com.clueride.geo.SplitLineString;
import com.clueride.geo.TranslateUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

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
public class TrackScore {
    private SimpleFeature track;
    private GeoNode subjectGeoNode;

    private List<Node> networkNodes = new ArrayList<>();
    private List<Segment> crossingSegments = new ArrayList<>();
    private List<Segment> intersectingSegments = new ArrayList<>();

    /** Lowest score is best. */
    private double scoreOverall = Double.MAX_VALUE;
    private double scoreTowardStart = Double.MAX_VALUE;
    private double scoreTowardEnd = Double.MAX_VALUE;
    private GeoNode proposedNode = null;
    private static List<GeoNode> nodeProposals = new ArrayList<>();
    private Segment bestSegment = null;

    // Holds the two pieces from the node out to each end of the track
    private final SplitLineString splitLineString;
    private static final Map<Integer, Segment> segmentIndex = new HashMap<>();
    private static final Map<Integer, SubTrackScore> scorePerSegment = new HashMap<>();

    public TrackScore(SimpleFeature track, GeoNode geoNode) {
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

    public void addCrossingSegment(Segment segment) {
        crossingSegments.add(segment);
    }

    public void addIntersectingSegments(Segment segment) {
        intersectingSegments.add(segment);
    }

    /**
     * @return the track
     */
    public SimpleFeature getTrack() {
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
    public List<Segment> getCrossingSegments() {
        return crossingSegments;
    }

    /**
     * @return the intersectingSegments
     */
    public List<Segment> getIntersectingSegments() {
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
        List<Segment> allSegments = new ArrayList<>(intersectingSegments);
        allSegments.addAll(crossingSegments);

        // TODO: Come back to these later
        // segmentIndex.put(segment.getSegId(), segment);
        // scorePerSegment.put(segment.getSegId(), calculatedScore);

        SubTrackScore lowScoreTowardStart = proposeBestScore(
                splitLineString.getLineStringToStart(), allSegments);
        SubTrackScore lowScoreTowardEnd = proposeBestScore(
                splitLineString.getLineStringToEnd(), allSegments);

        // Have one score if we found something on one end, but two if there
        // were segments on both ends. We choose the closer one to recommend
        // first.

        SubTrackScore selectedScore;
        // TODO: Handle returning both segments
        if (lowScoreTowardStart.hasScore() && lowScoreTowardEnd.hasScore()) {
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
        System.out.println("Low Score Segment: "
                + selectedScore.getBestSegment().getSegId());
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
    public SubTrackScore proposeBestScore(LineString subTrackLineString,
            List<Segment> allSegments) {
        GeoNode lowScoreNode = null;
        double score = Double.MAX_VALUE;
        for (Segment segment : allSegments) {
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
     * More trouble occurs when each end of the track intersects the network; we
     * only return a single score at this time; tossing a RuntimeException until
     * I can get my head wrapped around this.
     * 
     * @param segment
     *            - the existing network segment to evaluate.
     * @param lineString
     *            - the subTrack to be scored.
     * @return SubTrackScore representing the node, segment and score.
     */
    private SubTrackScore score(Segment segment, LineString subTrackLineString)
            throws RuntimeException {

        // Walk this subTrack up to the segment and if this isn't a
        // node, add it as a proposed node.
        LineString segmentLineString = TranslateUtil
                .segmentToLineString(segment);

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

    private class SubTrackScore {
        private final Segment bestSegment;
        private GeoNode bestNode = null;
        private final double score;

        public SubTrackScore(Segment bestSegment, GeoNode bestNode, double score) {
            this.bestSegment = bestSegment;
            this.bestNode = bestNode;
            this.score = score;
        }

        /**
         * @return
         */
        public boolean hasScore() {
            // TODO: handle this better - null vs. final
            return (bestNode != null);
        }

        /**
         * @return the bestSegment
         */
        public Segment getBestSegment() {
            return bestSegment;
        }

        /**
         * @return the bestNode
         */
        public GeoNode getBestNode() {
            return bestNode;
        }

        /**
         * @return the score
         */
        public double getScore() {
            return score;
        }

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
    public Segment getBestSegment() {
        return bestSegment;
    }
}
