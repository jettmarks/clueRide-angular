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
 * Created by jett on 1/16/18.
 */
package com.clueride.domain.badge;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.clueride.domain.account.principal.BadgeOsPrincipal;
import com.clueride.domain.session.SessionPrincipal;

/**
 * Default Implementation of BadgeService.
 */
public class BadgeServiceImpl implements BadgeService {
    private static final Logger LOGGER = Logger.getLogger(BadgeServiceImpl.class);

    private final BadgeStore badgeStore;
    private final BadgeTypeService badgeTypeService;
    private final SessionPrincipal sessionPrincipal;

    @Inject
    public BadgeServiceImpl(
            BadgeStore badgeStore,
            BadgeTypeService badgeTypeService,
            SessionPrincipal sessionPrincipal
    ) {
        this.badgeStore = badgeStore;
        this.badgeTypeService = badgeTypeService;
        this.sessionPrincipal = sessionPrincipal;
    }

    @Override
    public List<Badge> getBadges() {
        BadgeOsPrincipal badgeOsPrincipal = (BadgeOsPrincipal) sessionPrincipal.getSessionPrincipal();
        Integer userId = badgeOsPrincipal.getBadgeOsUserId();
        List<Badge> badgeList = new ArrayList<>();
        LOGGER.info("Looking up Badges for User ID " + userId);
        List<Badge.Builder> builderList = badgeStore.getAwardedBadgesForUser(userId);
        for (Badge.Builder builder : builderList) {
            builder.withBadgeType(badgeTypeService.getTypeOfBadge(builder));
            badgeList.add(builder.build());
        }
        return badgeList;
    }

}
