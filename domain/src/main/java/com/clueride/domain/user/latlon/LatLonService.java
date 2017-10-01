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

/**
 * Handles LatLon pairs; Nodes which are not necessarily on the Network yet.
 */
public interface LatLonService {
    /**
     * Provide a candidate LatLon and this will persist it to the store.
     * @param latLon Candidate LatLon.
     * @return the newly created LatLon complete with the ID from the DB.
     */
    LatLon addNew(LatLon latLon);

    /**
     * Given a LatLon ID, retrieve the corresponding LatLon.
     * @param id unique ID for the LatLon.
     * @return Location-supporting LatLon.
     */
    LatLon getLatLonById(Integer id);

}
