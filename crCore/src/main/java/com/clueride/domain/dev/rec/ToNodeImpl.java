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

import java.util.Collections;
import java.util.List;

import com.google.common.base.Objects;

import com.clueride.domain.GeoNode;
import com.clueride.feature.LineFeature;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.TranslateUtil;
import static com.clueride.domain.dev.rec.NetworkRecType.TRACK_TO_NODE;

/**
 * Description.
 *
 * @author jett
 *
 */
public class ToNodeImpl extends OnTrackImpl implements ToNode {
    private GeoNode networkNode;

    public ToNodeImpl(GeoNode reqNode, LineFeature trackStart,
            GeoNode networkNode) {
        super(reqNode, (TrackFeature) trackStart);
        this.networkNode = networkNode;
        addFeature(TranslateUtil.geoNodeToFeature(networkNode));
    }

    /**
     * @return the networkNode
     */
    public GeoNode getNetworkNode() {
        return networkNode;
    }

    /**
     * @see com.clueride.domain.dev.rec.NetworkRecImpl#getRecType()
     */
    @Override
    public NetworkRecType getRecType() {
        return TRACK_TO_NODE;
    }

    @Override
    public List<GeoNode> getNodeList() {
        return Collections.singletonList(getNetworkNode());
    }

    @Override
    public Double getScore() {
        return super.getScore();
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("networkNode", networkNode)
                .toString();
    }
}
