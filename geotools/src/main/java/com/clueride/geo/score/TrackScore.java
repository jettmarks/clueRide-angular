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
import java.util.List;

import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.Node;
import com.clueride.domain.dev.Segment;

/**
 * For a given Track, score how well it provides a connection between a proposed
 * point and the network as represented by nodes and segments that either cross
 * or intersect with this track.
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
}
