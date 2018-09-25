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
 * Created by jett on 3/20/16.
 */
package com.clueride.domain.team;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.clueride.domain.Team;
import com.clueride.domain.account.member.Member;

/**
 * Implementation of the Team Interface for maintaining the list of Teams and their members.
 */
public class TeamServiceImpl implements TeamService {
    private static final Logger LOGGER = Logger.getLogger(TeamServiceImpl.class);
    private static TeamService instance = new TeamServiceImpl();
    private static Team team;

    static
    {
        // TODO: Move this over to using a Store to hold onto the Teams and their members.
        team = new Team("Spokes Folks");
        team.add(new Member("Jett"));
    }

    @Override
    public List<Team> getTeams() {
        return Collections.singletonList(team);
    }

    @Override
    public Team getTeam(Integer teamId) {
        // TODO: tap into the store
        return team;
    }

    @Override
    public Team addMember(Integer teamId, Member newMember) {
        LOGGER.info("Adding Member " + newMember.getDisplayName() + " to team " + teamId);
        team.add(newMember);
        return team;
    }

}
