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
 * Created Sep 29, 2015
 */
package com.clueride.domain.dev.rec;

import static com.clueride.domain.dev.rec.NetworkRecType.TRACK_TO_NODE;

import com.clueride.domain.GeoNode;
import com.clueride.feature.LineFeature;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.TranslateUtil;

/**
 * Description.
 *
 * @author jett
 *
 */
public class ToNodeImpl extends OnTrackImpl implements ToNode {
    private GeoNode node;

    public ToNodeImpl(GeoNode reqNode, LineFeature trackStart,
            GeoNode node) {
        super(reqNode, (TrackFeature) trackStart);
        this.node = node;
        addFeature(TranslateUtil.geoNodeToFeature(node));
    }

    /**
     * @return the node
     */
    public GeoNode getNode() {
        return node;
    }

    /**
     * @see com.clueride.domain.dev.rec.NetworkRecImpl#getRecType()
     */
    @Override
    public NetworkRecType getRecType() {
        return TRACK_TO_NODE;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ToNodeImpl [getId()=")
                .append(getId()).append(", getName()=").append(getName())
                .append(", getScore()=").append(getScore()).append(
                        ", getRecType()=").append(getRecType()).append("]");
        return builder.toString();
    }
}
