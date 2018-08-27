/*
 * Copyright 2017 Jett Marks
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
 * Created by jett on 11/25/17.
 */
package com.clueride.domain.badge.event;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.clueride.infrastructure.db.ClueRide;

/**
 * Implementation of Badge Event Store for JPA.
 */
public class BadgeEventStoreJpa implements BadgeEventStore {

    private final EntityManager entityManager;

    @Inject
    public BadgeEventStoreJpa(
            @Nonnull @ClueRide EntityManager entityManager
    ) {
        this.entityManager = entityManager;
    }

    @Override
    public Integer add(BadgeEvent.Builder badgeEventBuilder) {
        entityManager.getTransaction().begin();
        entityManager.persist(badgeEventBuilder);
        entityManager.getTransaction().commit();
        return badgeEventBuilder.getId();
    }

    @Override
    public BadgeEvent.Builder getById(Integer badgeEventId) {
        BadgeEvent.Builder badgeEventBuilder;
        entityManager.getTransaction().begin();
        badgeEventBuilder = entityManager.find(BadgeEvent.Builder.class, badgeEventId);
        entityManager.getTransaction().commit();
        return badgeEventBuilder;
    }

}
