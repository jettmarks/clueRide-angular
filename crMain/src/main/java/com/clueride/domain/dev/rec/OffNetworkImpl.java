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

import static com.clueride.domain.dev.rec.NetworkRecType.OFF_NETWORK;

import com.clueride.domain.GeoNode;

/**
 * Implementation which records that the requested node is not on the network
 * and furthermore, we don't have a suitable Track in our catalog that is able
 * to connect the node to the network.
 * 
 * @author jett
 */
public class OffNetworkImpl extends RecImpl implements OffNetwork {

    /**
     * @param requestedNode
     */
    public OffNetworkImpl(GeoNode requestedNode) {
        super(requestedNode);
    }

    /**
     * @see com.clueride.domain.dev.rec.NetworkRecImpl#getRecType()
     */
    @Override
    public NetworkRecType getRecType() {
        return OFF_NETWORK;
    }

    /**
     * @see com.clueride.domain.dev.rec.RecImpl#getScore()
     */
    @Override
    public Double getScore() {
        return null;
    }

}
