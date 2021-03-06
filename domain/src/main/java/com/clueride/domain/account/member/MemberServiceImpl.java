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
 * Created by jett on 6/26/16.
 */
package com.clueride.domain.account.member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.NoResultException;

import com.google.inject.Inject;
import org.apache.log4j.Logger;

import com.clueride.domain.user.Badge;

/**
 * Implementation of MemberService.
 */
public class MemberServiceImpl implements MemberService {
    private static final Logger LOGGER = Logger.getLogger(MemberServiceImpl.class);
    private final MemberStore memberStore;

    @Inject
    public MemberServiceImpl(MemberStore memberStore) {
        this.memberStore = memberStore;
    }

    @Override
    public Member getMember(Integer id) {
        return memberStore.getMemberById(id);
    }

    @Override
    public List<Member> getMemberByDisplayName(String displayName) {
        return memberStore.getMemberByName(displayName);
    }

    @Override
    public Member getMemberByEmail(String email) {
        LOGGER.debug("Looking up account from database: " + email);
        InternetAddress internetAddress;
        try {
            internetAddress = new InternetAddress(email);
        } catch (AddressException e) {
            throw new RuntimeException("Invalid format for Email", e);
        }

        Member member;
        try {
            synchronized (memberStore) {
                member = memberStore.getMemberByEmail(internetAddress);
            }
        } catch (NoResultException e) {
            String message = "Unable to find member with account " + email;
            LOGGER.warn(message);
            throw new RuntimeException(message, e);
        }
        return member;
    }

    @Override
    public Member createNewMemberWithEmail(String email) {
        Member newMember = Member.Builder.builder()
                .withEmailAddress(email)
                .withBadges(Collections.singletonList(Badge.LOCATION_EDITOR))
                .build();

        try {
            memberStore.addNew(newMember);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newMember;
    }

    @Override
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        List<Member.Builder> builders = memberStore.getAllMembers();
        for (Member.Builder builder : builders) {
            members.add(builder.build());
        }
        return members;
    }
}
