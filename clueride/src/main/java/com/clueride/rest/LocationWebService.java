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

import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataParam;

import com.clueride.domain.user.latlon.LatLon;
import com.clueride.domain.user.loctype.LocationType;
import com.clueride.domain.user.loctype.LocationTypeService;
import com.clueride.infrastructure.Secured;
import com.clueride.service.LocationService;

/**
 * Rest API for the Locations.
 *
 * GET - given an ID, returns the specific location matching that ID.
 * PUT - Creates a new Location from the submitted JSON string, ID is assigned.
 * POST - Changes to a Location given by the ID.
 * DELETE - Not supported by this API - must be removed from the persistence layer using other means.
 *
 * There is a separate GET (path location/nearest) for the nearest locations, an array of a set of
 * nearest locations.
 */
@Secured
@Path("location")
public class LocationWebService {
    private final LocationService locationService;
    private final LocationTypeService locationTypeService;

    @Inject
    public LocationWebService(
            LocationService locationService,
            LocationTypeService locationTypeService
    ) {
        this.locationService = locationService;
        this.locationTypeService = locationTypeService;
    }

    /**
     * Handles GET requests for the data supporting specific Locations.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getLocation(@QueryParam("id") Integer locationId) {
        return locationService.getLocation(locationId);
    }

    @GET
    @Path("types")
    @Produces(MediaType.APPLICATION_JSON)
    public List<LocationType> getTypes() {
        return locationTypeService.getLocationTypes();
    }

    /**
     * Handles GET requests for a location's Map.
     */
    @GET
    @Path("map")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLocationGeometry(@QueryParam("locationId") Integer locationId) {
        return locationService.getLocationGeometry(locationId);
    }

    @POST
    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateLocation(com.clueride.domain.user.location.Location.Builder location) {
        locationService.updateLocation(location);
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
            @FormDataParam("lat") Double lat,
            @FormDataParam("lon") Double lon,
            @FormDataParam("locId") Integer locationId,
            @FormDataParam("file") InputStream fileData
    ) {
        locationService.saveLocationImage(
                lat, lon,
                locationId,
                fileData);
        return "OK";
    }

    /**
     * Endpoint for requesting the nearest locations to offer the user for selection.
     * @param lat - Latitude for current location.
     * @param lon - Longitude for current location.
     * @return JSON array containing Location objects for the nearest locations.
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

    /**
     * Endpoint for requesting the nearest Marker locations for editing.
     * @param lat - Latitude for current location.
     * @param lon - Longitude for current location.
     * @return JSON array containing Location with node objects for the nearest locations.
     */
    @GET
    @Path("nearest-marker")
    @Produces(MediaType.TEXT_PLAIN)
    public String getNearestMarkerLocations(
            @QueryParam("lat") Double lat,
            @QueryParam("lon") Double lon
    ) {
        return locationService.getNearestMarkerLocations(lat, lon);
    }

    @GET
    @Path("course")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCourseLocations(@QueryParam("courseId") Integer courseId) {
        return locationService.getCourseLocations(courseId);
    }

    @POST
    @Path("propose")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public com.clueride.domain.user.location.Location proposeLocation(
            @QueryParam("lat") Double lat,
            @QueryParam("lon") Double lon,
            @QueryParam("locType") String locationType
    ) {
        LatLon latLon = new LatLon(lat, lon);
        return locationService.proposeLocation(latLon, locationType);
    }

}
