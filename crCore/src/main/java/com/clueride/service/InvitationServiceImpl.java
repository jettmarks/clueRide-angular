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

import com.google.common.base.Strings;
import com.google.inject.Inject;
import org.apache.log4j.Logger;

import com.clueride.dao.InvitationStore;
import com.clueride.domain.Invitation;
import com.clueride.domain.Outing;
import com.clueride.domain.account.Member;

/**
 * Default implementation of InvitationService.
 */
public class InvitationServiceImpl implements InvitationService {
    private static final Logger LOGGER = Logger.getLogger(InvitationServiceImpl.class);

    private final InvitationStore invitationStore;

    /**
     * Injectable constructor.
     * @param invitationStore - Persistence layer for Invitations.
     */
    @Inject
    public InvitationServiceImpl(InvitationStore invitationStore) {
        this.invitationStore = invitationStore;
    }

    @Override
    public Invitation getInvitationByToken(String token) {
        return invitationStore.getInvitationByToken(token);
    }

    @Override
    public List<Invitation> getInvitationsForOuting(Integer outingId) {
        return invitationStore.getInvitationsByOuting(outingId);
    }

    @Override
    public Invitation createNew(Outing outing, Member member) throws IOException {
        LOGGER.info("Creating new instance");
        validateMember(member);
        validateOuting(outing);

        Invitation invitation = Invitation.Builder.builder()
                .setMember(member)
                .setBuiltOuting(outing)
                .build();

        invitationStore.addNew(invitation);
        return invitation;
    }

    private void validateOuting(Outing outing) {
        // Placeholder
    }

    private void validateMember(Member member) {
        if (Strings.isNullOrEmpty(member.getName())) {
            throw new IllegalAccessError("Expected Member Name to be non-empty");
        }

        if (Strings.isNullOrEmpty(member.getEmailAddress())) {
            throw new IllegalArgumentException("Expected Email Address to be non-empty");
        }
    }
}
