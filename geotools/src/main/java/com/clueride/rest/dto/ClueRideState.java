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
package com.clueride.rest.dto;

/**
 * Represents the state of a client as the game proceeds.
 *
 * This is pushed by the client when game state changes and pulled by the client
 * when it refreshes so game state is preserved.
 * TODO: Remove this class
 * @deprecated - Don't want the rest.dto versions.
 */
public class ClueRideState {
    public Integer teamId;
    public Integer pathIndex = -1;
    public Boolean mostRecentClueSolvedFlag = false;
    public GameState currentGameState;
    public String currentGameStateKey;
    public String historyIndex;
    public String maxVisibleLocationIndex;
}
