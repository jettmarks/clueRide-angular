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

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.clueride.domain.game.GameState;
import com.clueride.domain.game.GameStateService;
import com.clueride.rest.dto.ClueRideState;
import com.clueride.rest.dto.OutingState;
import com.clueride.service.OutingService;
import static java.util.Objects.requireNonNull;

/**
 * API for working with Game State both for User Session and per Team.
 *
 */
@Path("game-state")
public class GameStateWebService {
    private static final Logger LOGGER = Logger.getLogger(GameStateWebService.class);

    @Context
    private HttpServletRequest request;

    private GameStateService gameStateService;
    private OutingService outingService;

    @Inject
    public GameStateWebService(
            @Nonnull GameStateService gameStateService,
            @Nonnull OutingService outingService
    ) {
        this.gameStateService = requireNonNull(gameStateService);
        this.outingService = requireNonNull(outingService);
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
        // No longer supports the new API for Ionic clients
//        return gameStateService.updateGameStateByTeam(clueRideState);
        return "OK";
    }

    /* Outing API uses bare path and the Outing ID to identify which Outing State is requested. */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{outingId}")
    public OutingState getOutingState(@PathParam("outingId") Integer outingId) {
        return outingService.getState(outingId);
    }

    /** This could be confusing: this returns Outing State for the outing stuffed into the session. */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public OutingState getOutingState() {
        Integer outingId = getOutingFromSession();
        return outingService.getState(outingId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Long updateOutingState(OutingState outingState) {
        return outingService.updateOutingState(outingState);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("team-assembled")
    public GameState updateOutingWithTeamAssembled() {
        Integer outingId = getOutingFromSession();
        return gameStateService.updateWithTeamAssembled(outingId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("arrival")
    public GameState updateOutingWithArrival() {
        Integer outingId = getOutingFromSession();
        return gameStateService.updateOutingStateWithArrival(outingId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("departure")
    public GameState updateOutingWithDeparture() {
        Integer outingId = getOutingFromSession();
        return gameStateService.updateOutingStateWithDeparture(outingId);
    }

    private Integer getOutingFromSession() {
        HttpSession session = request.getSession();
        Integer outingId = (Integer) session.getAttribute("outingId");
        // TODO: CA-376
        if (outingId == null) {
            outingId = 3;
            LOGGER.info("Not finding the Outing in the Session; setting to " + outingId);
        }
        return outingId;
    }

}
