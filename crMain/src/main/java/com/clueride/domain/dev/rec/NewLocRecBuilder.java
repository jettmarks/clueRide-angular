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
package com.clueride.domain.dev.rec;

import com.clueride.dao.DefaultLocationStore;
import com.clueride.dao.LocationStore;
import com.clueride.domain.GeoNode;

/**
 * Instantiation of Location Recommendations for NetworkProposal.
 *
 * @author jett
 *
 */
public class NewLocRecBuilder {
    private GeoNode newLoc;
    private static LocationStore locationStore = DefaultLocationStore
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
        return onNode((GeoNode) locationStore.getNodeById(nodeId));
    }

    public OnNode onNode(GeoNode matchedNode) {
        return new OnNodeImpl(newLoc, matchedNode);
    }
}
