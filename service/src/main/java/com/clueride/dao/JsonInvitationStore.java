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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import com.clueride.domain.Invitation;
import com.clueride.io.JsonStoreType;
import com.clueride.io.PojoJsonUtil;

/**
 * Implementation of InvitationStore.
 *
 * TODO: CA-264 Need to actually persist this instead of letting it hang around in memory.
 */
public class JsonInvitationStore implements InvitationStore {
    private static List<Invitation> invitations = new ArrayList<>();

    @Inject
    public JsonInvitationStore() {
        loadAll();
    }

    private void loadAll() {
        invitations = PojoJsonUtil.loadInvitations();
    }

    @Override
    public Integer addNew(Invitation invitation) throws IOException {
        invitations.add(invitation);
        File outFile = PojoJsonUtil.getFile(invitation.getId(), JsonStoreType.INVITATION);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper
                    .writer()
                    .withDefaultPrettyPrinter()
                    .writeValue(outFile, invitation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invitation.getId();
    }

    @Override
    public List<Invitation> getInvitationsByOuting(Integer outingId) {
        return invitations;
    }

    @Override
    public Invitation getInvitationByToken(String token) {
        // TODO: CA-264 Brute force method until we build indices
        for (Invitation invitation : invitations) {
            if (invitation.getToken().endsWith(token)) {
                return invitation;
            }
        }
        return null;
    }
}
