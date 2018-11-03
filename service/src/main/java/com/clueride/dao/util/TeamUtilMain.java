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
 * Created by jett on 11/1/18.
 */
package com.clueride.dao.util;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberStoreJpa;
import com.clueride.domain.team.Team;
import com.clueride.domain.team.TeamService;
import com.clueride.domain.team.TeamServiceImpl;
import com.clueride.domain.team.TeamStore;
import com.clueride.domain.team.TeamStoreJpa;
import com.clueride.infrastructure.JpaUtil;

/**
 * Dumps records from Team database.
 */
public class TeamUtilMain {
    private static final Logger LOGGER = Logger.getLogger(TeamUtilMain.class);
    private static TeamService teamService;
    private static EntityManager entityManager;
    private static TeamStore teamStore;

    public static void main(String[] args) {
        instantiateServices();
        List<Team> teams = teamService.getTeams();
        LOGGER.info("Teams Before:");
        for (Team team : teams) {
            System.out.println(team.toString());
        }

        Team firstTeam = teams.get(0);
        Member existingMemberToAdd = Member.Builder.builder()
                .withId(7)  // Bike Angel
                .build();

        teamService.addMember(firstTeam.getId(), existingMemberToAdd);

        teams = teamService.getTeams();
        LOGGER.info("Teams After:");
        for (Team team : teams) {
            System.out.println(team.toString());
        }

        entityManager.close();

        System.exit(0);
    }

    private static void instantiateServices() {
        entityManager = JpaUtil.getClueRideEntityManagerFactory().createEntityManager();
        teamStore = new TeamStoreJpa(entityManager);
        MemberStoreJpa memberStore = new MemberStoreJpa(entityManager);
        teamService = new TeamServiceImpl(teamStore, memberStore, memberService);
    }

}
