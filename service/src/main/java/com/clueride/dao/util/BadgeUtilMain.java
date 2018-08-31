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
 * Created by jett on 8/27/18.
 */
package com.clueride.dao.util;

import java.util.List;

import javax.persistence.EntityManager;

import com.clueride.domain.account.principal.SessionPrincipal;
import com.clueride.domain.account.principal.SessionPrincipalImpl;
import com.clueride.domain.badge.Badge;
import com.clueride.domain.badge.BadgeService;
import com.clueride.domain.badge.BadgeServiceImpl;
import com.clueride.domain.badge.BadgeStore;
import com.clueride.domain.badge.BadgeStoreJpa;
import com.clueride.domain.badge.BadgeTypeService;
import com.clueride.domain.badge.BadgeTypeServiceMappedImpl;
import com.clueride.infrastructure.JpaUtil;

/**
 * Dumps records from BadgeOS database.
 */
public class BadgeUtilMain {
    private static BadgeService badgeService;
    private static EntityManager entityManager;
    private static BadgeStore badgeStore;

    public static void main(String[] args) {
        try {
            instantiateServices();
            List<Badge> badges = badgeService.getBadges();
            for (Badge badge : badges) {
                System.out.println(badge.toString());
            }
        } finally {
            entityManager.close();
        }

        System.exit(0);
    }

    private static void instantiateServices() {
        entityManager = JpaUtil.getWordPressEntityManagerFactory().createEntityManager();
        badgeStore = new BadgeStoreJpa(entityManager);
        BadgeTypeService badgeTypeService = new BadgeTypeServiceMappedImpl();
        SessionPrincipal sessionPrincipal = new SessionPrincipalImpl();
        badgeService = new BadgeServiceImpl(badgeStore, badgeTypeService, sessionPrincipal);
    }
}
