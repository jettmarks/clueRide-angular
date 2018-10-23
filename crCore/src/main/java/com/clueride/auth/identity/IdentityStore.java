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
package com.clueride.auth.identity;

/**
 * Describes how we obtain Identity instances.
 *
 * Caching would be performed outside of this service.
 */
public interface IdentityStore {
    /**
     * Given an Access Token provided by the client's authentication process,
     * obtain the matching Identity from the Identity Provider.
     *
     * Returns Test Account if token matches a configurable account.
     *
     * @param accessToken String encoded such that Identity Provider recognizes it.
     * @return ClueRideIdentity populated from the Identity Provider's response.
     */
    ClueRideIdentity getIdentity(String accessToken);

}
