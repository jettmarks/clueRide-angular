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
 * Created by jett on 11/28/17.
 */
package com.clueride.infrastructure.tether;

import com.clueride.rest.dto.LatLonPair;

/**
 * Defines interactions with the Server-side Tether for supplying LatLon positioning.
 */
public interface TetherService {

    /**
     * Supplies a LatLon pair that changes over time to simulate an actual device's GPS.
     * @return LatLon representing a changing position.
     */
    LatLonPair getDevModeLatLon();

}