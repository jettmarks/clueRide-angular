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
import java.security.Principal;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;

import com.clueride.domain.account.principal.PrincipalService;
import com.clueride.domain.account.principal.SessionPrincipal;
import com.clueride.token.TokenService;

/**
 * Allows picking up Authorization headers and extracting the Principal.
 *
 * This is also responsible for inserting the Principal into the Session.
 * The @Secured annotation on Jersey endpoints is what triggers this to be called.
 */
@Secured
// TODO: CA-306: entanglement of this and AuthServiceImpl
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    private static final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class);

    private final TokenService tokenService;
    private final Provider<SessionPrincipal> sessionPrincipalProvider;
    private final PrincipalService principalService;

    @Inject
    public AuthenticationFilter(
            TokenService tokenService,
            Provider<SessionPrincipal> sessionPrincipalProvider,
            PrincipalService principalService
    ) {
        super();
        this.tokenService = tokenService;
        this.sessionPrincipalProvider = sessionPrincipalProvider;
        this.principalService = principalService;
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
            LOGGER.warn("Login Failure: " + e.getMessage());
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());
            /* Our work is done for this request. */
            return;
        }

        final String principalName = tokenService.getNameFromToken(token);
        /* This one is used for Method Interceptors for Badge Capture. */
        setSessionPrincipal(principalName);

        /* This one is used for Jersey Calls. */
        setSecurityContextPrincipal(requestContext, principalName);
    }

    /** Add user to the Context for this invocation; used by JAX-RS & Jersey calls. */
    private void setSecurityContextPrincipal(
            ContainerRequestContext requestContext,
            final String principalName
    ) {
        final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(new SecurityContext() {

            @Override
            public Principal getUserPrincipal() {

                return new Principal() {

                    @Override
                    public String getName() {
                        return principalName;
                    }
                };
            }

            @Override
            public boolean isUserInRole(String role) {
                return true;
            }

            @Override
            public boolean isSecure() {
                return currentSecurityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return "Bearer";
            }
        });
    }

    /** Name the user's principal as the SessionPrincipal. */
    private void setSessionPrincipal(String principalName) {
        sessionPrincipalProvider.get().setSessionPrincipal(
                principalService.getPrincipalForEmailAddress(
                        principalName
                )
        );
    }

}
