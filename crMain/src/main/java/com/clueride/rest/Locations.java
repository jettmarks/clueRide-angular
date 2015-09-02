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
package com.clueride.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.clueride.service.LocationService;

/**
 * Maps between the locations end point and the LocationService which knows how
 * to manage locations.
 *
 * @author jett
 *
 */
@Path("locations")
public class Locations {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("new")
    public String getNewLocation(@QueryParam("lat") Double lat,
            @QueryParam("lon") Double lon) {
        return new LocationService().addNewLocation(lat, lon);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("groups")
    public String getLocationGroups() {
        return new LocationService().getLocationGroups();
    }
}
