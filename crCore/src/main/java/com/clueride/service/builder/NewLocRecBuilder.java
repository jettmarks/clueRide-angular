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
 * Created Oct 15, 2015
 */
package com.clueride.service.builder;

import com.clueride.dao.JsonNetworkStore;
import com.clueride.dao.JsonNodeStore;
import com.clueride.dao.NetworkStore;
import com.clueride.dao.NodeStore;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkRecommendation;
import com.clueride.domain.dev.NodeNetworkState;
import com.clueride.domain.dev.rec.OnNode;
import com.clueride.domain.dev.rec.OnNodeImpl;
import com.clueride.domain.dev.rec.OnSegment;
import com.clueride.domain.dev.rec.OnSegmentImpl;
import com.clueride.feature.Edge;

/**
 * Instantiation of Location Recommendations for NetworkProposal.
 *
 * @author jett
 *
 */
public class NewLocRecBuilder {
    private GeoNode newLoc;

    private static final NodeStore LOCATION_STORE = JsonNodeStore
            .getInstance();
    private static final NetworkStore EDGE_STORE = JsonNetworkStore
            .getInstance();

    /**
     * Create an instance of our factory given a GeoNode we're attempting to
     * bring into our network.
     * 
     * The supplied GeoNode is used to create appropriate sub-types of the
     * RecImpl class.
     * 
     * @param requestedGeoNode
     */
    public NewLocRecBuilder(GeoNode requestedGeoNode) {
        this.newLoc = requestedGeoNode;
    }

    public OnNode onNode(Integer nodeId) {
        newLoc.setState(NodeNetworkState.ON_NETWORK);
        return onNode((GeoNode) LOCATION_STORE.getNodeById(nodeId));
    }

    public OnNode onNode(GeoNode matchedNode) {
        return new OnNodeImpl(newLoc, matchedNode);
    }

    /**
     * @param matchedSegment
     *            - Edge representing where the New Location is sitting on the
     *            network.
     * @return OnSegment - specific NetworkRecommendation for adding a new
     *         location.
     */
    public OnSegment onSegment(Edge matchedSegment) {
        return new OnSegmentImpl(newLoc, matchedSegment);
    }

    /**
     * @param matchingSegmentId
     * @return OnSegment - specific NetworkRecommendation for adding a new
     *         location.
     */
    public NetworkRecommendation onSegment(Integer matchingSegmentId) {
        newLoc.setState(NodeNetworkState.ON_SEGMENT);
        return onSegment(EDGE_STORE.getEdgeById(matchingSegmentId));
    }

}
