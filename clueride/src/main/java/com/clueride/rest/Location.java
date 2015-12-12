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

    @POST
    @Path("uploadImage")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadImage(@FormDataParam("file") InputStream fileData ) {
        locationService.saveLocationImage(fileData);
        return "OK";
    }
}
