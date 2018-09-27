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

import com.clueride.domain.invite.Invitation;
import com.clueride.domain.invite.InvitationStore;
import com.clueride.io.JsonStoreType;
import com.clueride.io.PojoJsonUtil;

/**
 * Implementation of InvitationStore.
 */
public class JsonInvitationStore implements InvitationStore {
    private static List<Invitation.Builder> builderList = new ArrayList<>();

    @Inject
    public JsonInvitationStore() {
        loadAll();
    }

    private void loadAll() {
        List<Invitation> invitations = PojoJsonUtil.loadInvitations();
        for (Invitation invitation : invitations) {
            builderList.add(Invitation.Builder.from(invitation));
        }

    }

    @Override
    public Integer addNew(Invitation.Builder builder) throws IOException {
        builderList.add(builder);
        File outFile = PojoJsonUtil.getFile(builder.getId(), JsonStoreType.INVITATION);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper
                    .writer()
                    .withDefaultPrettyPrinter()
                    .writeValue(outFile, builder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.getId();
    }

    @Override
    public List<Invitation.Builder> getInvitationsByOuting(Integer outingId) {
        return builderList;
    }

    @Override
    public Invitation.Builder getInvitationByToken(String token) {
        // TODO: unclear if we'll need the token
        for (Invitation.Builder invitation : builderList) {
            if (invitation.getToken().endsWith(token)) {
                return invitation;
            }
        }
        return null;
    }

    /* May never implement since we're moving toward JPA implementation. */
    @Override
    public List<Invitation.Builder> getUpcomingInvitationsByMemberId(Integer memberId) {
        return null;
    }

    /* May never implement since we're moving toward JPA implementation. */
    @Override
    public Invitation.Builder getInvitationById(Integer inviteId) {
        return null;
    }

    /* May never implement since we're moving toward JPA implementation. */
    @Override
    public Invitation.Builder save(Invitation.Builder builder) {
        return null;
    }

}
