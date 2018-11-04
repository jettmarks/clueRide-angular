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

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.clueride.infrastructure.db.ClueRide;

/**
 * JPA implementation of {@link TeamStore}.
 */
public class TeamStoreJpa implements TeamStore {
    private static final Logger LOGGER = Logger.getLogger(TeamStoreJpa.class);
    private final EntityManager entityManager;

    @Inject
    public TeamStoreJpa(
            @Nonnull @ClueRide EntityManager entityManager
    ) {
        this.entityManager = entityManager;
    }

    @Override
    public Team.Builder addNew(Team.Builder teamBuilder) {
        LOGGER.info("Creating a new Team: " + teamBuilder.getName());
        entityManager.getTransaction().begin();
        entityManager.persist(teamBuilder);
        entityManager.getTransaction().commit();
        return teamBuilder;
    }

    @Override
    public List<Team.Builder> getTeams() {
        LOGGER.info("Retrieving full list of Teams");
        entityManager.getTransaction().begin();
        List<Team.Builder> builders = entityManager.createQuery("SELECT t FROM teamBuilder t").getResultList();
        entityManager.getTransaction().commit();
        return builders;
    }

    @Override
    public Team.Builder getTeamById(Integer teamId) {
        LOGGER.info("Retrieving Team by ID: " + teamId);
        entityManager.getTransaction().begin();
        Team.Builder builder = entityManager.find(Team.Builder.class, teamId);
        entityManager.getTransaction().commit();
        return builder;
    }

    @Override
    public Team.Builder updateTeam(Team.Builder teamBuilder) {
        LOGGER.info("Updating an existing Team: " + teamBuilder.getId());
        entityManager.getTransaction().begin();
        entityManager.merge(teamBuilder);
        entityManager.getTransaction().commit();
        return teamBuilder;
    }

}
