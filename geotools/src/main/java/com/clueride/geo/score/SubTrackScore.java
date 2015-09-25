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
 * Created Sep 24, 2015
 */
package com.clueride.geo.score;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.Segment;

class SubTrackScore {
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

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SubTrackScore [bestSegment=").append(bestSegment)
                .append(", bestNode=").append(bestNode).append(", score=")
                .append(score).append("]");
        return builder.toString();
    }

}