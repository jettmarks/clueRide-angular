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
 * Created by jett on 6/25/16.
 */
package com.clueride.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import com.google.inject.Inject;
import org.apache.log4j.Logger;

import com.clueride.domain.account.Member;
import com.clueride.io.PojoJsonUtil;

/**
 * TODO: Description.
 */
public class JsonMemberStore implements MemberStore {
    private static final Logger LOGGER = Logger.getLogger(JsonMemberStore.class);

    /** In-memory references to the full set of Members indexed by ID. */
    private static Map<Integer,Member> membersById = new HashMap<>();
    /** In-memory references to the full set of Members indexed by Email Address. */
    private static Map<InternetAddress,Member> membersByEmail = new HashMap<>();
    /** In-memory references to the full set of Members indexed by Display Name. */
    private static Map<String,List<Member>> membersByName = new HashMap<>();

    private static Collection<Member> members;

    private static boolean needsReload = true;

    @Inject
    public JsonMemberStore() {
        if (needsReload) {
            loadAll();
        }
    }

    /**
     * Reads through all the Members on disk to bring in and then index the Members
     * by Email Address and ID.
     */
    private void loadAll() {
        members = PojoJsonUtil.loadMembers();
        reIndex();
        needsReload = false;
    }

    private void reIndex() {
        for (Member member : members) {
            membersById.put(member.getId(), member);
//            membersByEmail(member.getEmailAddress(), member);
            String displayName = member.getDisplayName();
            if (!membersByName.containsKey(displayName)) {
                membersByName.put(displayName, new ArrayList<Member>());
            }
            membersByName.get(displayName).add(member);
        }
    }

    @Override
    public Integer addNew(Member member) throws IOException {
        return null;
    }

    @Override
    public Member getMemberById(Integer id) {
        return membersById.get(id);
    }

    @Override
    public List<Member> getMemberByName(String displayName) {
        return membersByName.get(displayName);
    }

    @Override
    public Member getMemberByEmail(InternetAddress emailAddress) {
        return null;
    }

    @Override
    public void update(Member member) {
        // TODO: Build this impl

    }
}