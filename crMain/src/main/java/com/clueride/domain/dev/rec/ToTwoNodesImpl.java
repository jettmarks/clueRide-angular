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

import static com.clueride.domain.dev.rec.NetworkRecType.TRACK_TO_2_NODES;

import com.clueride.domain.GeoNode;
import com.clueride.feature.TrackFeature;

/**
 * Description.
 *
 * @author jett
 *
 */
public class ToTwoNodesImpl extends TrackImpl implements ToTwoNodes {
    private GeoNode node1;
    private GeoNode node2;

    public ToTwoNodesImpl(GeoNode reqNode, TrackFeature track,
            GeoNode node1, GeoNode node2) {
        super(reqNode, track);
        this.node1 = node1;
        this.node2 = node2;
    }

    /**
     * @return the node1
     */
    public GeoNode getNode1() {
        return node1;
    }

    /**
     * @return the node2
     */
    public GeoNode getNode2() {
        return node2;
    }

    /**
     * @see com.clueride.domain.dev.rec.NetworkRecImpl#getRecType()
     */
    @Override
    public NetworkRecType getRecType() {
        return TRACK_TO_2_NODES;
    }

}
