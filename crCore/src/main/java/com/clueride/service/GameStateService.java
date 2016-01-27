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

import com.clueride.rest.dto.ClueRideState;

/**
 * Manages an in-memory copy of the State per Team and per User Session.
 */
public interface GameStateService {
    /**
     * Given a Team's ID, return the most recently stored State for that Team.
     * @param teamId - Unique Integer identifying the Team whose State to be retrieved.
     * @return - JSON String representing the entire state object.
     */
    String getGameStateByTeam(Integer teamId);

    /**
     * Update the stored Game State for a team using the given instance.
     * @param clueRideState - State values to be used for the update.
     * @return - JSON object indicating success of update.
     */
    String updateGameStateByTeam(ClueRideState clueRideState);
}
