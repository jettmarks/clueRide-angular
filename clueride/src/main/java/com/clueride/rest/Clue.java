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
 * Created by jett on 2/25/16.
 */
package com.clueride.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.clueride.service.ClueService;

/**
 * API for working with Clues; part of Location, but Location only references by IDs.
 */
@Path("clue")
public class Clue {
    private ClueService clueService;

    @Inject
    public Clue(ClueService clueService) {
        this.clueService = clueService;
    }

    @GET
    @Path("{clueId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getClue(@PathParam("clueId") Integer clueId) {
        return clueService.getClue(clueId);
    }

}
