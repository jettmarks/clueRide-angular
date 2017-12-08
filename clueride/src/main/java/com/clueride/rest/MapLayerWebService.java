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
 * Created by jett on 12/7/17.
 */
package com.clueride.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.clueride.domain.user.layer.Layer;
import com.clueride.domain.user.layer.MapLayerService;

/**
 * Service to provide meta-data for the available Map Layers.
 */
@Path("map-layer")
public class MapLayerWebService {

    private final MapLayerService mapLayerService;

    @Inject
    public MapLayerWebService(
            MapLayerService mapLayerService
    ) {
        this.mapLayerService = mapLayerService;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Layer> getLayers() {
        return mapLayerService.getLayers();
    }

}
