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
 * Created Aug 27, 2015
 */
package com.clueride.domain.dev;

/**
 * Represents a collection of geographically proximate Nodes.
 * 
 * One possible real world criteria for two Nodes belonging to the same group is
 * whether or not you would use the same Bike Rack for both Nodes.
 *
 * @author jett
 *
 */
public interface NodeGroup extends Node {

    // Start out with a definition of the radius and the location as a node

    Double getRadius();

    /**
     * @param lat
     */
    void setLat(Double lat);

    /**
     * @param lon
     */
    void setLon(Double lon);

}
