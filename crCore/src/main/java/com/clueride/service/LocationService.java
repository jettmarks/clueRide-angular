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

import com.clueride.domain.user.latlon.LatLon;
import com.clueride.domain.user.location.Location;

/**
 * Provides business-layer services for User's Location instances.
 *
 * This is also used by Location Editor when constructing Location records in the field.
 */
public interface LocationService {
    /**
     * Given an ID, return the JSON string representing the Location.
     * @param locationId - Unique Integer ID for the Location.
     * @return String JSON representing User's Location domain object.
     */
    String getLocation(Integer locationId);

    /**
     * Given an ID, return the GeoJSON Feature Collection which places the location
     * on a map.
     * @param locationId - ID of the location which tells us the Node and its position.
     * @return GeoJSON Feature Collection with a single Point and its properties.
     */
    String getLocationGeometry(Integer locationId);

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
     * @return JSON String representing the nearest locations to select from.
     */
    String getNearestLocations(Double lat, Double lon);

    /**
     * Nearest locations to the given location for the purposes of editing from a map; includes Node info.
     * @param lat of current location.
     * @param lon of current location.
     * @return JSON String, array of node-populated Locations.
     */
    String getNearestMarkerLocations(Double lat, Double lon);

    /**
     * Given a Course ID, return an array of ordered locations.
     * NOTE: the list of locations may not be static.  If choices are provided along the way, this list of Locations
     * could change (further thought, what is the identity of a course that changes over time?).
     * @param courseId - Unique identifier representing the course.
     * @return Ordered array of Locations for the course.
     */
    String getCourseLocations(Integer courseId);

    /**
     * Accept Location instance from JSON and update the existing record with the
     * matching ID.
     * @param location - com.clueride.rest.dto.Location instance from JSON.
     */
    void updateLocation(Location.Builder location);

    /**
     * Most Locations will be against a Node that has been confirmed to be on the network, but
     * this function allows proposing a Node that may not be on the network yet.
     * @param latLon coordinates of the proposed location.
     * @param locationType of the proposed Location.
     * @return JSON String representing the new location.
     */
    Location proposeLocation(LatLon latLon, String locationType);

}
