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

import java.util.List;

import com.google.common.base.Strings;

import com.clueride.domain.Invitation;
import com.clueride.domain.Outing;
import com.clueride.domain.account.Member;

/**
 * Default implementation of InvitationService.
 */
public class InvitationServiceImpl implements InvitationService {
    @Override
    public Invitation getInvitationByToken(String token) {
        return null;
    }

    @Override
    public List<Invitation> getInvitationsForOuting(Integer outingId) {
        return null;
    }

    @Override
    public Invitation createNew(Outing outing, Member member) {
        validateMember(member);
        validateOuting(outing);

        Invitation invitation = new Invitation(outing, member);
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
