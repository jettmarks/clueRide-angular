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
package com.clueride.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.log4j.Logger;

import com.clueride.io.PojoJsonUtil;
import com.clueride.rest.dto.ClueRideState;

/**
 * Implementation of service handling Game State.
 */
public class GameStateServiceImpl implements GameStateService {
    private static final Logger LOGGER = Logger.getLogger(GameStateServiceImpl.class);

    // TODO: Push this simple implementation into a Store.
    private static ClueRideState clueRideState = new ClueRideState();

    @Override
    public String getGameStateByTeam(Integer teamId) {
        LOGGER.info("Requesting Game State for Team ID " + teamId);
        try {
            return PojoJsonUtil.generateClueRideState(clueRideState);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{\"status\": \"Failed\"}";
        }
    }

    @Override
    public String updateGameStateByTeam(ClueRideState clueRideState) {
        LOGGER.info("Updating Game State for Team ID " + clueRideState.teamId);
        this.clueRideState = clueRideState;
        return "{\"status\": \"OK\"}";
    }

    @Override
    public void updateOutingStateWithArrival(Integer outingId) {
        LOGGER.info("Changing Game State for outing " + outingId + " to Arrival");
    }

    @Override
    public void updateOutingStateWithDeparture(Integer outingId) {
        LOGGER.info("Changing Game State for outing " + outingId + " to Departure");
    }

}
