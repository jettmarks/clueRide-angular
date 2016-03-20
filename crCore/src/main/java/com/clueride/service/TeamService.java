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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.clueride.domain.Player;
import com.clueride.domain.Team;

public class TeamService {

    private static TeamService instance = new TeamService();
    private static Team team;

    static
    {
        // TODO: Move this over to using a Store to hold onto the Teams and their members.
        team = new Team("Spokes Folks");
        team.add(new Player("Team Leader (Uncle Albert)"));
        for (int i=1; i<=3; i++) {
            team.add(new Player("Member "+i));
        }
    }

    public static TeamService getInstance() {
        return instance;
    }

    // TODO: Move this over to letting Jersey/Jackson perform the serialization to JSON.
    public String getTeamAsJson() {
        ObjectMapper mapper = new ObjectMapper();
        String result = null;
        try {
            result = mapper.writeValueAsString(team);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
