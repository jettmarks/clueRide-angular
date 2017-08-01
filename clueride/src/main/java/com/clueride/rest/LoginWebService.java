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
 * Created by jett on 2/6/16.
 */
package com.clueride.rest;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.clueride.domain.account.OAuthCredentials;
import com.clueride.domain.user.Badge;
import com.clueride.rest.dto.CRCredentials;
import com.clueride.service.AuthenticationService;
import com.clueride.token.TokenService;

/**
 * Rest API for Authentication and Authorization functionality.
 */
@Path("login")
public class LoginWebService {
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;

    @Context
    private HttpServletRequest request;

    @Context
    private HttpServletResponse response;

    @Inject
    public LoginWebService(
            TokenService tokenService,
            AuthenticationService authenticationService
    ) {
        this.tokenService = tokenService;
        this.authenticationService = authenticationService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Badge> getBadgesForSession() {
        return (List<Badge>) request.getSession().getAttribute("badges");
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Badge> authenticate(CRCredentials crCredentials) {
        Principal principal = authenticationService.getPrincipal(crCredentials);
        String token = tokenService.generateTokenForExistingPrincipal(principal);
        response.setHeader("Authorization", "Bearer " + token);
        return Collections.emptyList();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("oauth")
    public List<Badge> authenticateUsingOAuth(OAuthCredentials.Builder oaCredentialsBuilder) {
        List<Badge> badges = authenticationService.loginReturningBadges(oaCredentialsBuilder.build());
        String token = authenticationService.establishSession(badges, request);
        response.setHeader("Authentication-Token", token);
        return badges;
    }

    @DELETE
    public void logout() {
        /* TODO: Pull token from the session list. */
        request.getSession().invalidate();
    }
}
