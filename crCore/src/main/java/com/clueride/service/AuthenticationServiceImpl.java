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
 * Created by jett on 3/11/16.
 */
package com.clueride.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.clueride.domain.user.Badge;
import com.clueride.rest.dto.CRCredentials;

/**
 * Implementation of Authentication services.
 */
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger LOGGER = Logger.getLogger(AuthenticationServiceImpl.class);

    // TODO: Eventually, this will also hold the selected Team/Outing

    @Override
    public List<Badge> loginReturningBadges(CRCredentials crCredentials) {
        LOGGER.info("Retrieving badges for user " + crCredentials.name);
        List<Badge> result = new ArrayList<>();
        if ("Jett".equals(crCredentials.name) && "adfhg".equals(crCredentials.password)) {
            result.add(Badge.TEAM_LEAD);
        }
        result.add(Badge.TEAM_MEMBER);
        return result;
    }

    @Override
    public void establishSession(List<Badge> badges, HttpServletRequest request) {
        request.getSession(true);
        HttpSession session = request.getSession();
        LOGGER.debug("Session created");
        session.setMaxInactiveInterval(12 * 60 * 60);
        session.setAttribute("badges", badges);
        // TODO: This is a temporary hardcoding of the Outing ID for anyone logging in
        session.setAttribute("outingId", 2);
    }
}
