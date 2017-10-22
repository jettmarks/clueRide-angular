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
 * Created by jett on 10/22/17.
 */
package com.clueride.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.clueride.domain.user.puzzle.Puzzle;
import com.clueride.domain.user.puzzle.PuzzleService;
import com.clueride.infrastructure.Secured;

/**
 * REST API for the Puzzles.
 *
 * These are currently tied to a given location, but want to allow
 * the Puzzles to stand on their own without being tied to a location.
 */
@Secured
@Path("puzzle")
public class PuzzleWebService {
    private final PuzzleService puzzleService;
    @Inject
    public PuzzleWebService(
            PuzzleService puzzleService
    ) {
        this.puzzleService = puzzleService;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Puzzle getById(@PathParam("id") String id) {
        return puzzleService.getById(Integer.parseInt(id));
    }

    @GET
    @Path("location/{locId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Puzzle> getByLocation(@PathParam("locId") String locationId) {
        return puzzleService.getByLocation(Integer.parseInt(locationId));
    }

}
