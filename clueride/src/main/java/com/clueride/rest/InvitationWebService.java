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
 * Created by jett on 5/29/16.
 */
package com.clueride.rest;

import javax.ws.rs.Path;

import com.google.inject.Inject;

import com.clueride.service.InvitationService;

/**
 * REST API for working with Invitations, a mapping between an Outing and a Member.
 */
@Path("invite")
public class InvitationWebService {
    private final InvitationService invitationService;

    @Inject
    public InvitationWebService(InvitationService invitationService) {
        this.invitationService = invitationService;
    }
}
