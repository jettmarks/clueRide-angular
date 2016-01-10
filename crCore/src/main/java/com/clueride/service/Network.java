/*
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
package com.clueride.service;

import java.util.List;

import com.clueride.domain.GeoNode;
import com.clueride.feature.LineFeature;
import com.clueride.feature.TrackFeature;

/**
 * Abstraction of a Network - the body of connected Segments and Nodes.
 *
 * @author jett
 */
public interface Network {

    /**
     * @param trackFeature
     * @deprecated - coming out with the LoadService
     */
    void add(TrackFeature trackFeature);

    List<GeoNode> getSortedNodes(GeoNode geoNode);

    /**
     * Takes the 'published' copy of the network and turns it into a JSON string for the client to display.
     * @return JSON-formatted FeatureCollection containing all the Segments in the Network.
     */
    String getNetworkForDisplay();

    /**
     * Serves as a signal that changes have occurred to the underlying Stores and that those changes
     * may now be committed to the memory space which serves requests.
     */
    void storesReadyForPublishing();

    List<LineFeature> getLineFeaturesForNodeId(Integer pointId);
}