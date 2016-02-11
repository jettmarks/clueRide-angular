/*
 * Copyright 2016 Jett Marks
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
 * Created by jett on 2/10/16.
 */
package com.clueride.service;

/**
 * Holds services useful for diagnostics on the network.
 * This class started out with accepting a few methods defined in the
 * deprecated version of the LocationService.
 */
public interface DiagnosticService {
    /**
     * Diagnostic request to show all the points on the track that covers the
     * point at this lat/lon pair.
     *
     * @param lat - Latitude of a point on the Track we want to see.
     * @param lon - Longitude of a point on the Track we want to see.
     * @return - GeoJson representation of the points which lie on the track; may be empty.
     */
    // TODO: Hook this in with a REST API.
    String showPointsOnTrack(Double lat, Double lon);
}
