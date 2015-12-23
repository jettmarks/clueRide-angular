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
 * Created Sep 28, 2015
 */
package com.clueride.domain.dev.rec;

import com.clueride.domain.GeoNode;
import com.clueride.feature.Edge;

import static com.clueride.domain.dev.rec.NetworkRecType.ON_SEGMENT;

/**
 * Implementation supporting the case where we have a Node that lands in the
 * middle of an existing network segment.
 * 
 * The segment is intended to be split into two segments and the node inserted
 * should the user choose this recommendation.
 *
 * @author jett
 *
 */
public class OnSegmentImpl extends RecImpl implements OnSegment {

    private final Edge onNetworkSegment;

    /**
     * @param requestedNode
     * @param onNetworkSegment
     */
    public OnSegmentImpl(GeoNode requestedNode, Edge onNetworkSegment) {
        super(requestedNode);
        this.onNetworkSegment = onNetworkSegment;
    }

    /**
     * @see com.clueride.domain.dev.rec.OnSegment#getSegment()
     */
    @Override
    public Edge getSegment() {
        return onNetworkSegment;
    }

    /**
     * @see com.clueride.domain.dev.rec.NetworkRecImpl#getRecType()
     */
    @Override
    public NetworkRecType getRecType() {
        return ON_SEGMENT;
    }

    /**
     * @see com.clueride.domain.dev.rec.RecImpl#getScore()
     */
    @Override
    public Double getScore() {
        return null;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnSegmentImpl [getId()=").append(getId()).append(
                ", getName()=").append(getName()).append(", getRecType()=")
                .append(getRecType()).append(", getScore()=")
                .append(getScore()).append("]");
        return builder.toString();
    }

}
