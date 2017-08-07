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
package com.clueride.token;

import java.security.Principal;

import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Generates Authorization tokens based on Principal, Badges, and Session.
 */
public interface TokenService {
    /**
     *
     * @return String representing an authenticated user along with their badges.
     */
    String generateSignedToken();

    /**
     * Throws exception if the token is invalid and decodes the passed token.
     */
    DecodedJWT verifyToken(String token);

    /**
     * Throws exception if the token isn't acceptable by this application.
     * @param token representing a given principal; should be issued by this app.
     */
    void validateToken(String token);

    /**
     * Creates (and persists) a new Principal and then generates a token for that new principal.
     */
    String generateTokenForNewPrincipal();

    /**
     * Creates token for an existing Principal.
     * @param principal unique identifier indicating specific access to this system.
     * @return String token representing the Principal, valid for a fixed period of time.
     */
    String generateTokenForExistingPrincipal(Principal principal);
}
