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
import java.util.ArrayList;
import java.util.List;

import com.clueride.domain.Invitation;

/**
 * Implementation of InvitationStore.
 *
 * TODO: Need to actually persist this instead of letting it hang around in memory.
 */
public class InvitationStoreImpl implements InvitationStore {
    private static List<Invitation> invitations = new ArrayList<>();

    @Override
    public Integer addNew(Invitation invitation) throws IOException {
        invitations.add(invitation);
        // TODO: Hook this up with an ID provider
        return null;
    }

    @Override
    public List<Invitation> getInvitationsByOuting(Integer outingId) {
        return invitations;
    }

    @Override
    public Invitation getInvitationByToken(String token) {
        // TODO: Brute force method until we build indices
        for (Invitation invitation : invitations) {
            if (invitation.getToken().endsWith(token)) {
                return invitation;
            }
        }
        return null;
    }
}
