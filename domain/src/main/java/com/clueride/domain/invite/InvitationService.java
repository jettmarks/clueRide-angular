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
package com.clueride.domain.invite;

import java.io.IOException;
import java.util.List;

/**
 * Service supporting the Invitation Model which ties together an Outing and a Member.
 */
public interface InvitationService {

    /**
     * Given an Invitation Token, return the matching Invitation.
     * @param token Unique String identifying the Invitation.
     * @return fully populated Invitation.
     */
    InvitationFull getInvitationByToken(String token);

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
     * TODO: Inviting an entire team makes more sense.
     * @param outingId The identifier for the Outing which we're inviting people to attend.
     * @param memberId Unique identifier for the person who we invite.
     * @return Fully-populated Invitation instance.
     */
    Invitation createNew(Integer outingId, Integer memberId) throws IOException;

    /**
     * Given an Outing's unique ID, generate and send emails for each of the Guests for the Outing.
     * If the caller doesn't have the Guide badge and isn't the creator of this Outing, the
     * invitations will not be sent.
     * @param outingId Unique identifier for the Outing.
     * @return List of Invitations for the Outing with appropriate state for each Invitation.
     */
    List<Invitation> send(Integer outingId);

    /**
     * Once the session is established, we're able to tell the client what list of invitations
     * are active on that session.
     * @return List of Invitations for the Session's Principal.
     */
    List<Invitation> getInvitationsForSession();

    /**
     * Invoked when the user wants to Accept (or re-Accept) an invitation.
     * @param inviteId unique identifier for the ID; can be checked that this user is the one accepting.
     * @return The Invitation with the updated state.
     */
    Invitation accept(Integer inviteId);

    /**
     * Invoked when the user wants to Decline an invitation or Cancel a previously accepted invite.
     * @param inviteId unique identifier for the ID; can be checked that this user is the one accepting.
     * @return The Invitation with the updated state.
     */
    Invitation decline(Integer inviteId);

    /**
     * Invoked when the Outing for an invitation come to an end.
     * TODO: consider expiring an entire team's worth of Invitations all at once.
     * @param inviteId unique identifier for the ID.
     * @return The Invitation with the updated state.
     */
    Invitation expire(Integer inviteId);

}
