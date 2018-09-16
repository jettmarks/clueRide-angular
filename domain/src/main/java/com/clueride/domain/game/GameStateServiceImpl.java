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
package com.clueride.domain.game;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.clueride.domain.ssevent.SSEventService;

/**
 * Implementation of service handling Game State.
 */
public class GameStateServiceImpl implements GameStateService {
    private static final Logger LOGGER = Logger.getLogger(GameStateServiceImpl.class);
    private final SSEventService ssEventService;

    /** Cached copy of GameState TODO: Persist this via a service. */
    private static Map<Integer, GameState> gameStateMap = new HashMap<>();

    @Inject
    public GameStateServiceImpl(
            SSEventService ssEventService
    ) {
        this.ssEventService = ssEventService;
    }

    @Override
    public String getGameStateByTeam(Integer teamId) {
        LOGGER.info("Requesting Game State for Team ID " + teamId);
        return "";
    }

    @Override
    public GameState updateWithTeamAssembled(Integer outingId) {
        LOGGER.info("Opening Game State for outing " + outingId);
        GameState gameState = GameState.Builder.builder()
                .withTeamAssembled(true)
                .build();
        gameStateMap.put(outingId, gameState);
        ssEventService.sendTeamReadyEvent(outingId);
        return gameState;
    }

    @Override
    public GameState updateOutingStateWithArrival(Integer outingId) {
        LOGGER.info("Changing Game State for outing " + outingId + " to Arrival");
        GameState gameState = gameStateMap.get(outingId);
        if (!gameState.getRolling()) {
            throw new IllegalStateException("Cannot Arrive if not yet rolling");
        }

        gameState = GameState.Builder.from(gameState)
                .withTeamAssembled(true)
                .withRolling(false)
                .build();
        gameStateMap.put(outingId, gameState);
        ssEventService.sendArrivalEvent(outingId);
        return gameState;
    }

    @Override
    public GameState updateOutingStateWithDeparture(Integer outingId) {
        LOGGER.info("Changing Game State for outing " + outingId + " to Departure");
        GameState gameState = gameStateMap.get(outingId);
        if (gameState.getRolling()) {
            throw new IllegalStateException("Cannot depart if still rolling");
        }
        GameState.Builder gameStateBuilder = GameState.Builder.from(gameState);
        gameStateBuilder.withRolling(true)
                .withPathIndex(gameStateBuilder.getPathIndex() + 1);

        gameState = gameStateBuilder.build();

        gameStateMap.put(outingId, gameState);
        ssEventService.sendDepartureEvent(outingId);
        return gameState;
    }

}
