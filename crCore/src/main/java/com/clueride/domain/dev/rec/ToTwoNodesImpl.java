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

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Objects;

import com.clueride.domain.GeoNode;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.TranslateUtil;
import static com.clueride.domain.dev.rec.NetworkRecType.TRACK_TO_2_NODES;

/**
 * When a proposed Track runs from a new Node and meets the Network at a Node on
 * each end, this class is used to carry that extra information about the two Nodes.
 *
 * @author jett
 */
public class ToTwoNodesImpl extends OnTrackImpl implements ToTwoNodes {
    private GeoNode startNode;
    private GeoNode endNode;

    public ToTwoNodesImpl(GeoNode reqNode, TrackFeature track,
            GeoNode node1, GeoNode node2) {
        super(reqNode, track);
        this.startNode = node1;
        addFeature(TranslateUtil.geoNodeToFeature(node1));
        this.endNode = node2;
        addFeature(TranslateUtil.geoNodeToFeature(node2));
    }

    @Override
    public boolean isDoubleEnded() {
        return true;
    }

    /**
     * @return the startNode
     */
    public GeoNode getStartNode() {
        return startNode;
    }

    /**
     * @return the endNode
     */
    public GeoNode getEndNode() {
        return endNode;
    }

    /**
     * @see com.clueride.domain.dev.rec.NetworkRecImpl#getRecType()
     */
    @Override
    public NetworkRecType getRecType() {
        return TRACK_TO_2_NODES;
    }

    @Override
    public List<GeoNode> getNodeList() {
        return Arrays.asList(getStartNode(), getEndNode());
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("startNode", startNode)
                .add("endNode", endNode)
                .toString();
    }
}
