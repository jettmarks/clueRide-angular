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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.Node;
import com.clueride.domain.dev.Segment;

/**
 * Ways of measuring potential new connections to an existing network.
 * 
 * Given a network, a point off the network, and a set of tracks that may or may
 * not pass through the point and other nodes on the network, figure out the
 * best candidates for connecting the point to the network.
 * 
 * This is just one aspect of the evaluation of a new Nodes state, but an
 * important one since it bridges the gap between on-network and off-network.
 * 
 * This may become the object passed back for rendering on the Network Editing
 * map.
 *
 * @author jett
 *
 */
public class IntersectionScore {

    private Integer id;
    private GeoNode subjectGeoNode;
    private Map<SimpleFeature, Node> tracksWithNetworkNodes = new HashMap<>();
    private Map<SimpleFeature, Segment> tracksIntersectingSegments = new HashMap<>();
    private Map<SimpleFeature, Segment> tracksCrossingSegments = new HashMap<>();
    private Map<SimpleFeature, TrackScore> allTracks = new HashMap<>();

    /**
     * @param geoNode
     */
    public IntersectionScore(GeoNode geoNode) {
        this.subjectGeoNode = geoNode;
    }

    /**
     * Collect list of tracks that also intersect with network nodes.
     * 
     * @param track
     * @param node
     */
    public void addTrackConnectingNode(SimpleFeature track, Node node) {
        TrackScore trackScore = fetchTrackScore(track);
        trackScore.addNode(node);
    }

    /**
     * Collect list of tracks that intersect (overlap) with network segments.
     * 
     * @param track
     * @param segment
     */
    public void addIntersectingTrack(SimpleFeature track, Segment segment) {
        TrackScore trackScore = fetchTrackScore(track);
        trackScore.addIntersectingSegments(segment);
    }

    /**
     * Collect list of tracks that cross (without overlap) network segments.
     * 
     * @param track
     * @param segment
     */
    public void addCrossingTrack(SimpleFeature track, Segment segment) {
        TrackScore trackScore = fetchTrackScore(track);
        trackScore.addCrossingSegment(segment);
    }

    /**
     * @param track
     * @return
     */
    private TrackScore fetchTrackScore(SimpleFeature track) {
        if (!allTracks.containsKey(track)) {
            allTracks.put(track, new TrackScore(track, subjectGeoNode));
        }
        return allTracks.get(track);
    }

    public int getTrackCount() {
        return allTracks.size();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IntersectionScore [id=").append(id).append(
                ", trackScore=").append(fetchBestTrackScore())
                .append(
                        ", subjectGeoNode=").append(subjectGeoNode).append(
                        ", tracksWithNetworkNodesCount=").append(
                        tracksWithNetworkNodes.size())
                .append(", tracksIntersectingSegmentsCount=").append(
                        tracksIntersectingSegments.size())
                .append(", tracksCrossingSegmentsCount=").append(
                        tracksCrossingSegments.size())
                .append(", allTracksCount=").append(allTracks.size())
                .append("]");
        return builder.toString();
    }

    /**
     * Temporary method to provide a summary TrackScore for the Intersections.
     * 
     * Just grabs the first one or returns null -- we're only paying attention
     * to single track recommendations at this time (Sept 2015).
     * 
     * @return
     */
    public TrackScore fetchBestTrackScore() {
        for (Entry<SimpleFeature, TrackScore> entry : allTracks.entrySet()) {
            return fetchTrackScore(entry.getKey());
        }
        return null;
    }

    /**
     * @return
     */
    public List<Segment> getIntersectingSegments(SimpleFeature track) {
        return fetchTrackScore(track).getIntersectingSegments();
    }

    /**
     * @return
     */
    public Segment getBestSegment() {
        return fetchBestTrackScore().getBestSegment();
    }
}
