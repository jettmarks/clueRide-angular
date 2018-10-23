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
 * Created by jett on 10/13/18.
 */
package com.clueride.auth.access;

import java.util.concurrent.ExecutionException;

import com.clueride.auth.identity.ClueRideIdentity;

/**
 * Describes the features provided for Access Tokens.
 */
public interface AccessTokenService {
    /**
     * Given an Access Token as provided by the client, return
     * the Identity attributes matching that token.
     *
     * This will perform the following services on a token before granting access:
     * <ol>
     *     <li>Check to see if it is in Cache. If so, it is already validated.</li>
     *     <li>Request the Identity Server to tell us whether the access token matches a known user and cache those
     *     results if they are found.</li>
     *     <li>Provide a rejection if the access token is invalid, can't be found, or is otherwise rejected.</li>
     * </ol>
     * @param token token as provided by the client requesting access.
     * @return ClueRideIdentity containing identifying details for this user -- particularly the email address.
     */
    ClueRideIdentity getIdentity(String token);

    /**
     * Convenience method for working with the more complete AccessToken instance.
     *
     * @param accessToken instance of type {@link AccessToken}.
     * @return ClueRideIdentity containing identifying details for this user -- particularly the email address.
     */
    ClueRideIdentity getIdentity(AccessToken accessToken) throws ExecutionException;
    /**
     * Given an Access Token as provided by the client, return the Principal string matching that token (generally,
     * the email address).
     *
     * Values may be cached.
     *
     * @param token Raw String as provided by the Authorization Header.
     * @return String representing the principal (unique email address identifying the user).
     */
    String getPrincipalString(String token);

    /**
     * Clears the cache of all data.
     */
    void emptyCache();

}
