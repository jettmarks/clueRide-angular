/*
 * Copyright 2015 Jett Marks
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
 * Created by jett on 12/12/15.
 */
package com.clueride.service;

import java.io.InputStream;

/**
 * Provides business-layer services for User's Location instances.
 *
 * This is also used by Location Editor when constructing Location records in the field.
 */
public interface LocationService {
    /**
     * Given an ID, return the JSON string representing the Location.
     * @param locationId - Unique Integer ID for the Location.
     * @return String JSON representing User's Loccation domain object.
     */
    String getLocation(Integer locationId);

    /**
     * Handles upload request for a new image at the given location.
     * @param lat - Double latitude of device location.
     * @param lon - Double longitude of device location.
     * @param locationId - Optional Integer representing an existing location (which may not have been created yet).
     * @param fileData - InputStream from which we read the image data to put into a file.
     */
    void saveLocationImage(Double lat, Double lon, Integer locationId, InputStream fileData);

    /**
     * Given the device's location -- or any relevant lat/lon pair -- return a list
     * of the top five locations in order of closeness.
     * @param lat of current location.
     * @param lon of current location.
     * @return JSON String representing the nearest five locations to select from.
     */
    String getNearestLocations(Double lat, Double lon);
}
