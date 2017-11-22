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

import com.google.inject.servlet.RequestScoped;

/**
 * Session Scoped implementation of Principal retrieval.
 *
 * This expects to be populated as soon as the credentials are verified (usually by token) and then picked up
 * by the services requiring the current user.
 */
@RequestScoped
public class SessionPrincipalImpl implements SessionPrincipal {

    private Principal sessionPrincipal;

    @Override
    public Principal getSessionPrincipal() {
        return sessionPrincipal;
    }

    @Override
    public void setSessionPrincipal(Principal sessionPrincipal) {
        this.sessionPrincipal = sessionPrincipal;
    }

}
