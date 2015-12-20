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
 * Created by jett on 12/3/15.
 */
package com.clueride.rest;

import com.clueride.service.LocationService;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * Rest API for the Locations.
 *
 * GET - given an ID, returns the specific location matching that ID.
 * PUT - Creates a new Location from the submitted JSON string, ID is assigned.
 * POST - Changes to a Location given by the ID.
 * DELETE - Not supported by this API - must be removed from the persistence layer using other means.
 *
 * There is a separate GET (path location/nearest) for the nearest locations, an array of the five
 * nearest locations for selection.
 */
@Path("location")
public class Location {
    private LocationService locationService;

    @Inject
    public Location(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Handles GET requests for specific Locations.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getLocation(@QueryParam("id") Integer locationId) {
        return locationService.getLocation(locationId);
    }

    /**
     * Handles upload request for a new image at the given location.
     * @param lat - Double latitude of device location.
     * @param lon - Double longitude of device location.
     * @param locationId - Optional Integer representing an existing location (which may not have been created yet).
     * @param fileData - InputStream from which we read the image data to put into a file.
     * @return "OK" confirming success.
     */
    @POST
    @Path("uploadImage")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadImage(
            @QueryParam("lat") Double lat,
            @QueryParam("lon") Double lon,
            @QueryParam("locId") Integer locationId,
            @FormDataParam("file") InputStream fileData
    ) {
        locationService.saveLocationImage(
                lat, lon,
                locationId,
                fileData);
        return "OK";
    }

    /**
     * Endpoint for requesting the nearest 5 locations to offer the user for selection.
     * @param lat
     * @param lon
     * @return JSON array containing Location objects for the five nearest locations.
     */
    @GET
    @Path("nearest")
    @Produces(MediaType.TEXT_PLAIN)
    public String getNearestLocations(
            @QueryParam("lat") Double lat,
            @QueryParam("lon") Double lon
    ) {
        return locationService.getNearestLocations(lat, lon);
    }
}
