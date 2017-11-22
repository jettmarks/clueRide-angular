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
 * Created by jett on 7/31/17.
 */
package com.clueride.domain.account.principal;

import java.security.Principal;

/**
 * Logic behind the obtaining of Principals.
 *
 * This is a rather naive interface at this time since I'm still exploring how to best use these.
 */
public interface PrincipalService {
    /**
     * Creates a temporary Principal for Guests until they choose to identify themselves.
     * @return Principal instance with unique generated ID.
     */
    Principal getNewPrincipal();

    /**
     * Throws {@link com.auth0.jwt.exceptions.InvalidClaimException} if the provided string isn't found in our
     * database.
     * @param email string in a format that can be recognized as a valid email address.
     */
    void validate(String email);

    /**
     * Given an email address, return the matching Principal.
     * @param emailAddress
     * @return Unique Principal representing the holder of the given Email Address.
     */
    Principal getPrincipalForEmailAddress(String emailAddress);

}
