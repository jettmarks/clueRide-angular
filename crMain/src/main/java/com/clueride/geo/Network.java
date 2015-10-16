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
 * Created Aug 21, 2015
 */
package com.clueride.geo;

import java.util.List;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkProposal;
import com.clueride.feature.TrackFeature;

/**
 * Abstraction of a Network - the body of connected Segments and Nodes.
 *
 * @author jett
 *
 */
public interface Network {

    /**
     * @param connectedNode
     * @return
     */
    public abstract boolean canReach(GeoNode connectedNode);

    /**
     * @param trackFeature
     */
    public abstract void add(TrackFeature trackFeature);

    /**
     * @param connectedNode
     * @return
     */
    public abstract NetworkProposal evaluateNodeState(GeoNode connectedNode);

    public abstract List<GeoNode> getSortedNodes(GeoNode geoNode);

}