/*
 * Copyright 2018 Jett Marks
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
 * Created by jett on 9/14/18.
 */
package com.clueride.domain.game;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Defines what we need to track the state of the Game for the clients.
 */
@Immutable
public class GameState {
    private final Boolean teamAssembled;
    private final Boolean rolling;
    private final String nextLocation;
    private final Integer pathIndex;

    private GameState(Builder builder) {
        this.teamAssembled = builder.getTeamAssembled();
        this.rolling = builder.getRolling();
        this.nextLocation = builder.getNextLocation();
        this.pathIndex = builder.getPathIndex();
    }

    public Boolean getTeamAssembled() {
        return teamAssembled;
    }

    public Boolean getRolling() {
        return rolling;
    }

    public String getNextLocation() {
        return nextLocation;
    }

    public Integer getPathIndex() {
        return pathIndex;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static class Builder {
        /* Initial Game State */
        private Boolean teamAssembled = false;
        private Boolean rolling = false;
        private String nextLocation = "Meeting Location";
        private Integer pathIndex = 0;

        public GameState build() {
            return new GameState(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static Builder from(GameState gameState) {
            return builder()
                    .withTeamAssembled(gameState.getTeamAssembled())
                    .withRolling(gameState.getRolling())
                    .withNextLocation(gameState.getNextLocation())
                    .withPathIndex(gameState.getPathIndex());
        }

        public Boolean getTeamAssembled() {
            return teamAssembled;
        }

        public Builder withTeamAssembled(Boolean teamAssembled) {
            this.teamAssembled = teamAssembled;
            return this;
        }

        public Boolean getRolling() {
            return rolling;
        }

        public Builder withRolling(Boolean rolling) {
            this.rolling = rolling;
            return this;
        }

        public String getNextLocation() {
            return nextLocation;
        }

        public Builder withNextLocation(String nextLocation) {
            this.nextLocation = nextLocation;
            return this;
        }

        public Integer getPathIndex() {
            return pathIndex;
        }

        public Builder withPathIndex(Integer pathIndex) {
            this.pathIndex = pathIndex;
            return this;
        }
    }

}
