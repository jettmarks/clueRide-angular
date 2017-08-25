/**
 * Copyright 2015 Jett Marks
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Created 11/17/15.
 */
package com.clueride.service;

import java.util.List;

import com.clueride.domain.Team;
import com.clueride.domain.account.member.Member;

public interface TeamService {

    /**
     * At this time, there is only a single team, but eventually, we'll have a number
     * of teams managed by the store.
     * @return List of all Teams.
     */
    List<Team> getTeams();

    /**
     *
     * @param teamId unique identifier for the Team.
     * @return Matching Team.
     */
    Team getTeam(Integer teamId);

    /**
     * Given the Team ID and a new Member instance, add the player to the team and
     * return the updated team.
     * @param teamId - Unique ID of the Team.
     * @param newMember - New fully-populated instance of a Member object.
     * @return the updated Team.
     */
    Team addMember(Integer teamId, Member newMember);

}
