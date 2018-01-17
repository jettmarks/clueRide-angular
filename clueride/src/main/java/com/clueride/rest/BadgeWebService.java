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
package com.clueride.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.clueride.domain.badge.BadgeService;
import com.clueride.domain.user.Badge;
import com.clueride.infrastructure.Secured;

/**
 * Web Service for the badges earned by the authenticated user.
 *
 * Note that awarding of badges does not occur within this API and
 * collecting badge events is performed indirectly via other API calls
 * as the user performs badge-worthy tasks.
 */
@Secured
@Path("badge")
public class BadgeWebService {
    private final BadgeService badgeService;

    @Inject
    public BadgeWebService(
            BadgeService badgeService
    ) {
        this.badgeService = badgeService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Badge> getSessionBadges() {
        return badgeService.getBadges();
    }

}
