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

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.clueride.domain.Invitation;
import com.clueride.domain.InvitationFull;
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

    @GET
    @Path("/outing/{outingId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Invitation> getActiveInvitations(@PathParam("outingId") Integer outingId) {
        return invitationService.getInvitationsForOuting(outingId);
    }

    @GET
    @Path("{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public InvitationFull getInvitation(@PathParam("token") String token) {
        return invitationService.getInvitationByToken(token);
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Invitation createInvitation(Invitation.Builder invitationBuilder)
            throws IOException {
        return invitationService.createNew(
                invitationBuilder.getOuting(),
                invitationBuilder.getMemberId()
        );
    }

    @POST
    @Path("/send/{outingId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Invitation> sendInvitations(Integer outingId) {
        return invitationService.send(outingId);
    }
}
