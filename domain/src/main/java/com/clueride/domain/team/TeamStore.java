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
 * Created by jett on 10/31/18.
 */
package com.clueride.domain.team;

import java.util.List;

/**
 * How we persist Teams and their members.
 */
public interface TeamStore {

    /**
     * Persist a new Team.
     * @param builder Builder instance representing the Team including the Members of the Team.
     * @return ID of the new Team.
     */
    Integer addNew(Team.Builder builder);

    /**
     * Retrieve a list of the current Teams.
     * @return All Teams.
     */
    List<Team.Builder> getTeams();

    /**
     * REtrieve the Team matching the given ID.
     * @param teamId Unique identifier for the team.
     * @return Team.Builder instance matching the given ID.
     */
    Team.Builder getTeamById(Integer teamId);

    /**
     * Merge changes to the list of members (or name) for an existing Team.
     * @param teamBuilder Team Builder instance with updated information (members or name).
     * @return updated instance.
     */
    Team.Builder updateTeam(Team.Builder teamBuilder);

}
