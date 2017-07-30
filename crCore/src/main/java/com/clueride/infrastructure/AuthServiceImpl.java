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
 * Created by jett on 7/30/17.
 */
package com.clueride.infrastructure;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;

import com.google.inject.Inject;

import com.clueride.service.AuthenticationService;
import com.clueride.service.InvitationService;

/**
 * Implementation of AuthService.
 */
public class AuthServiceImpl implements AuthService {//    @Inject
    private final AuthenticationService authenticationService;
    private final InvitationService invitationService;
    private FilterChain filterChain = null;
    private HttpServletRequest request = null;
    private HttpServletResponse response = null;

    @Inject
    public AuthServiceImpl (
            AuthenticationService authenticationService,
            InvitationService invitationService
    ) {
        this.authenticationService = authenticationService;
        this.invitationService = invitationService;
    }


    @Override
    public void withChain(FilterChain filterChain) {
        this.filterChain = filterChain;
    }

    @Override
    public boolean isOptionsRequest(HttpServletRequest request) throws IOException, ServletException {
        isValid();
        if (request.getMethod().equals(HttpMethod.OPTIONS)) {
            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }

    @Override
    public void isValid() {
        if (filterChain == null) {
            throw new IllegalStateException("Filter Chain must be added before this service can operate");
        }
        if (response == null) {
            throw new IllegalStateException("Response must be added before this service can operate");
        }
    }

    @Override
    public void withResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public void addToken() {
        // Undetermined yet what happens here.
    }
}
