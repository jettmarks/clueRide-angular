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
 * Created by jett on 3/5/16.
 */
package com.clueride.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.clueride.domain.outing.Outing;
import com.clueride.service.OutingService;

/**
 * REST API for working with Outings, a particular Team on a particular Course at
 * a particular time.
 */
@Path("outing")
public class OutingWebService {
    private final OutingService outingService;

    @Inject
    public OutingWebService(OutingService outingService) {
        this.outingService = outingService;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Outing getOutingById(@PathParam("id") Integer outingId) {
        return outingService.getByOutingId(outingId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Outing createOuting(Outing.Builder outingBuilder) {
        return outingService.createOuting(outingBuilder.build());
    }

}
