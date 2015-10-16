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
 * Created Oct 14, 2015
 */
package com.clueride.geo.score;

import java.util.List;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkRecommendation;
import com.clueride.domain.dev.Node;
import com.clueride.feature.Edge;

/**
 * Given a Track and a Location -- and the existing network -- determines the
 * NetworkRecommendation along with its score.
 *
 * @author jett
 */
public interface TrackScore {
    NetworkRecommendation getNetworkRecommendation();

    /**
     * @return
     */
    String dumpScores();

    /**
     * TODO: Revist whether we need this method to be exposed.
     */
    int refreshIndices();

    /**
     * TODO: Expect this will get folded into the NetworkRecommendation.
     * 
     * @return
     */
    GeoNode getProposedNode();

    /**
     * TODO: Expect this will get folded into the NetworkRecommendation.
     * 
     * @param node
     */
    void addNode(Node node);

    /**
     * TODO: Expect this will get folded into the NetworkRecommendation.
     * 
     * @param edge
     */
    void addIntersectingSegments(Edge edge);

    /**
     * TODO: Expect this will get folded into the NetworkRecommendation.
     * 
     * @param segment
     */
    void addCrossingSegment(Edge segment);

    /**
     * TODO: Expect this will get folded into the NetworkRecommendation.
     * 
     * @return
     */
    List<Edge> getIntersectingSegments();

    /**
     * TODO: Expect this will get folded into the NetworkRecommendation.
     * 
     * @return
     */
    Edge getBestSegment();
}
