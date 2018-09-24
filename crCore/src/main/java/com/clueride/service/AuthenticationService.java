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

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.clueride.domain.account.OAuthCredentials;
import com.clueride.domain.invite.InvitationFull;
import com.clueride.domain.user.Badge;
import com.clueride.rest.dto.CRCredentials;

/**
 * Mapping of the credentials to a set of authorizations (Badges).
 */
public interface AuthenticationService {
    /**
     * Given a set of credentials, find out what badges have been earned by the user.
     * @param crCredentials - Authentication credentials.
     * @return List of Badges representing capabilities (authorizations) for the account.
     */
    List<Badge> loginReturningBadges(CRCredentials crCredentials);


    /**
     * Given a validated invitation, find the badges earned by the corresponding user.
     * @param invitation - Stand-in for credentials.
     * @return List of Badges representing capabilities (authorizations) for the account.
     */
    List<Badge> loginReturningBadges(InvitationFull invitation);

    /**
     * Given an authenticated OAuth response, use this to find the account information and
     * Badges earned by the corresponding user.
     * @param oaCredentials instance of OAuthCredentials from the client.
     * @return List of Badges representing capabilities (authorizations) for the account.
     */
    List<Badge> loginReturningBadges(OAuthCredentials oaCredentials);

    /**
     * Given an HttpServletRequest and the list of appropriate badges,
     * populate a session with the badges and other details for the session.
     * @param badges - Authorizations represented as Badges.
     * @param request - HttpServletRequest which holds the session.
     */
    String establishSession(List<Badge> badges, HttpServletRequest request);

    /**
     * Based on the credentials, validate the credentials and if valid, return the principal associated
     * with those credentials.
     * @param crCredentials ClueRide specific credentials (username/password).
     */
    Principal getPrincipal(CRCredentials crCredentials);

}
