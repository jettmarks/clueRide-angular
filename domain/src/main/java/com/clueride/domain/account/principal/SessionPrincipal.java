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
 * Created by jett on 11/19/17.
 */
package com.clueride.domain.account.principal;

import java.security.Principal;

/**
 * Defines how to retrieve a principal from the session.
 */
public interface SessionPrincipal {
    /**
     * Retrieves the Principal associated with the current request/session.
     * @return Principal representing the current user.
     */
    Principal getSessionPrincipal();

    /**
     * Populates the Principal for the current request/session.
     * @param sessionPrincipal Principal representing the current user.
     */
    void setSessionPrincipal(Principal sessionPrincipal);

}
