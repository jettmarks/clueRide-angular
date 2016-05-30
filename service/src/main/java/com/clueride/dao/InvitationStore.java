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
 * Created by jett on 5/30/16.
 */
package com.clueride.dao;

import java.io.IOException;
import java.util.List;

import com.clueride.domain.Invitation;

/**
 * Persistence interface for {@link Invitation} instances.
 */
public interface InvitationStore {
    /**
     * Accepts fully-populated Invitation and returns the Integer ID; the token should already have been populated.
     * Integer ID is the DB unique identifier; the token is a hashed value serving as a public ID.
     * @param invitation - Instance to be persisted.
     * @return Integer DB-generated ID.
     * @throws IOException if unable to access underlying backing store.
     */
    Integer addNew(Invitation invitation) throws IOException;

    /**
     * Returns a list of fully-populated Invitations for the given Outing.
     * @param outingId - Unique ID of the Outing which may or may not have Invitations yet.
     * @return List of Invitations or empty list if none created yet.
     */
    List<Invitation> getInvitationsByOuting(Integer outingId);

    /**
     * Given a specific token identifying a specific Invitation, return the Invitation.
     * @param token - Unique identifier for an Invitation.
     * @return Matching Invitation.
     */
    Invitation getInvitationByToken(String token);
}
