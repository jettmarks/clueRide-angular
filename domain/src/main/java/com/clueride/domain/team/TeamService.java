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
package com.clueride.domain.team;

import java.util.List;

import com.clueride.domain.account.member.Member;

public interface TeamService {

    /**
     * Returns all of our teams.
     * @return List of all Teams.
     */
    List<Team> getTeams();

    /**
     * Given the ID, return the matching Team.
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

    /**
     * Given a name for a new team, create the Team record so we can then add
     * members.
     * @param name String representing the name of the team.
     * @return Named Team ready to add members (none included).
     */
    Team newTeam(String name);

}
