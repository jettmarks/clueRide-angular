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
 * Created by jett on 8/26/18.
 */
package com.clueride.domain.badge;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.clueride.infrastructure.db.WordPress;

/**
 * JPA implementation of Badge Store.
 */
public class BadgeStoreJpa implements BadgeStore {
    private static final Logger LOGGER = Logger.getLogger(BadgeStoreJpa.class);
    private final EntityManager entityManager;

    @Inject
    public BadgeStoreJpa(
            @Nonnull @WordPress EntityManager entityManager
    ) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Badge> getAwardedBadgesForUser() {
        List<Badge.Builder> builderList;
        entityManager.getTransaction().begin();
        builderList = entityManager.createQuery(
                    "SELECT b FROM badge_display_per_user b"
        ).getResultList();
        entityManager.getTransaction().commit();
        List<Badge> badgeList = new ArrayList<>();
        for (Badge.Builder builder : builderList) {
            try {
                badgeList.add(builder.build());
            } catch (MalformedURLException e) {
                LOGGER.error("Problem with URL: " + e);
            }
        }
        return badgeList;
    }

}
