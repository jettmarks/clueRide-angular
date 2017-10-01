/*
 * Copyright 2017 Jett Marks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by jett on 9/10/17.
 */
package com.clueride.domain.user.latlon;

import java.io.IOException;

/**
 * Responsible for persistence of Location-supporting Nodes.
 */
public interface LatLonStore {
    /**
     * Adds LatLon Proposal which may not (yet) be part of the Network.
     * @param latLon Location-supporting LatLon.
     * @return unique ID across all Nodes.
     * @throws IOException from underlying implementation.
     */
    Integer addNew(LatLon latLon) throws IOException;

    /**
     * Given an ID, return the corresponding LatLon.
     * @param id unique ID for the LatLon.
     * @return matching LatLon.
     */
    LatLon getLatLonById(Integer id);

}
