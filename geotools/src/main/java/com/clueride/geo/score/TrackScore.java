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
    private double score = Double.MAX_VALUE;
    private GeoNode proposedNode = null;
    private static List<GeoNode> nodeProposals = new ArrayList<>();
    private Segment bestSegment = null;

    private static final Map<Integer, Segment> segmentIndex = new HashMap<>();
    private static final Map<Integer, Double> scorePerSegment = new HashMap<>();

    public TrackScore(SimpleFeature track, GeoNode geoNode) {
        this.track = track;
        this.subjectGeoNode = geoNode;
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
     * @return
     */
    public int refreshIndices() {
        segmentIndex.clear();
        scorePerSegment.clear();
        nodeProposals.clear();
        GeoNode lowScoreNode = null;
        Integer lowScoreSegId = null;

        int segCount = 0;
        Double calculatedScore = Double.MAX_VALUE;
        for (Segment segment : intersectingSegments) {
            segmentIndex.put(segment.getSegId(), segment);
            calculatedScore = score(segment);
            if (calculatedScore < score) {
                score = calculatedScore;
                lowScoreNode = proposedNode;
                lowScoreSegId = segment.getSegId();
                bestSegment = segment;
            }
            scorePerSegment.put(segment.getSegId(), calculatedScore);
            segCount++;
        }
        for (Segment segment : crossingSegments) {
            segmentIndex.put(segment.getSegId(), segment);
            calculatedScore = score(segment);
            if (calculatedScore < score) {
                score = calculatedScore;
                lowScoreNode = proposedNode;
                lowScoreSegId = segment.getSegId();
                bestSegment = segment;
            }
            scorePerSegment.put(segment.getSegId(), calculatedScore);
            segCount++;
        }
        System.out.println("Low Score Segment: " + lowScoreSegId);
        proposedNode = lowScoreNode;
        return segCount;
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
     * @return
     * @throws RuntimeException
     *             if both ends of the track connect to the network.
     * 
     *             TODO: We want to handle the situation where two end both
     *             connect.
     */
    private Double score(Segment segment) throws RuntimeException {
        Double distance = 0.0;
        // TODO: we don't need to split the track every time we score a
        // "segment"

        // Walk each end of the track up to the segment and if this isn't a
        // node, add it as a proposed node.
        LineString trackLineString = (LineString) track.getDefaultGeometry();
        SplitLineString splitLineString = new SplitLineString(trackLineString,
                subjectGeoNode);
        LineString segmentLineString = TranslateUtil
                .segmentToLineString(segment);

        // TODO: Split into pair and check each of these
        Coordinate coordinateIntersection = null;
        for (Coordinate coordinate : trackLineString.getCoordinates()) {
            Point point = PointFactory.getJtsInstance(coordinate.y,
                    coordinate.x, coordinate.z);
            if (segmentLineString.buffer(GeoProperties.BUFFER_TOLERANCE)
                    .covers(point)) {
                GeoNode newGeoNode = NodeFactory.getInstance(point);
                coordinateIntersection = coordinate;
                nodeProposals.add(newGeoNode);
                proposedNode = newGeoNode;
                break;
            }
        }
        distance = LengthToPoint
                .length(trackLineString, coordinateIntersection);
        return distance;
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
        for (Entry<Integer, Double> entry : scorePerSegment.entrySet()) {
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
