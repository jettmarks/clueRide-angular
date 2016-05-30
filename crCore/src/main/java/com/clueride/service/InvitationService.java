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
package com.clueride.service;

import java.io.IOException;
import java.util.List;

import com.clueride.domain.Invitation;
import com.clueride.domain.Outing;
import com.clueride.domain.account.Member;

/**
 * Service supporting the Invitation Model which ties together an Outing and a Member.
 */
public interface InvitationService {

    /**
     * Given an Invitation Token, return the matching Invitation.
     * @param token Unique String identifying the Invitation.
     * @return fully populated Invitation.
     */
    Invitation getInvitationByToken(String token);

    /**
     * Given an outingId, return all the active Invitations.
     * @param outingId Unique identifier for the Outing.
     * @return List of Invitations (which may be empty) for the Outing.
     */
    List<Invitation> getInvitationsForOuting(Integer outingId);

    /**
     * Given an Outing instance and a Member instance, create an instance of
     * an Invitation.
     * Both the Outing and the Member are validated to make sure we have what
     * is needed.
     * @param outing The Outing which we're inviting people to attend.
     * @param member The person who we invite.
     * @return Fully-populated Invitation instance.
     */
    Invitation createNew(Outing outing, Member member) throws IOException;

}
