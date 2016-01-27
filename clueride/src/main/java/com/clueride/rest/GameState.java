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
 * Created by jett on 1/24/16.
 */
package com.clueride.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.clueride.rest.dto.ClueRideState;
import com.clueride.service.GameStateService;

/**
 * API for working with Game State both for User Session and per Team.
 *
 */
@Path("gameState")
public class GameState {
    private GameStateService gameStateService;

    @Inject
    public GameState(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("team")
    public String getGameStateForTeam(@QueryParam("teamId") Integer teamId) {
        return gameStateService.getGameStateByTeam(teamId);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("team")
    public String updateGameStateForTeam(
            ClueRideState clueRideState
    ) {
        return gameStateService.updateGameStateByTeam(clueRideState);
    }


}
