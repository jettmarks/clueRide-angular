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
 * Created by jett on 7/25/17.
 */
package com.clueride.infrastructure;

import java.io.IOException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.clueride.token.TokenService;

/**
 * Allows picking up Authorization headers and extracting the Principal.
 */
@Secured
//@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    private static final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class);

    private final TokenService tokenService;

    @Inject
    public AuthenticationFilter(
            TokenService tokenService
    ) {
        super();
        this.tokenService = tokenService;
        LOGGER.info("Instantiating my Auth");
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (requestContext.getMethod().equals(HttpMethod.OPTIONS)) {
            /* Free pass for OPTIONS requests? */
            LOGGER.info("Allowing OPTIONS request");
            return;
        }
        // Get the HTTP Authorization header from the request
        String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());
        }

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();

        if (token.equals("GuestToken")) {
            /* Respond with a replacement token. */
            requestContext.abortWith(
                    Response
                            .status(Response.Status.OK)
                            .header(
                                    HttpHeaders.AUTHORIZATION,
                                    "Bearer " + tokenService.generateTokenForNewPrincipal())
                            .build()
            );
            return;
        }

        try {
            tokenService.validateToken(token);
        } catch (Exception e) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
