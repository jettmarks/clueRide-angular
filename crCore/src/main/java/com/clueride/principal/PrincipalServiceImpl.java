/*
 * Copyright 2017 Jett Marks
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
 * Created by jett on 7/31/17.
 */
package com.clueride.principal;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.auth0.jwt.exceptions.InvalidClaimException;

import com.clueride.domain.account.member.Member;
import com.clueride.member.MemberService;

/**
 * Exploratory implementation.
 */
public class PrincipalServiceImpl implements PrincipalService {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(PrincipalServiceImpl.class);
    private static List<Principal> principals = null;
    private final MemberService memberService;

    @Inject
    public PrincipalServiceImpl(
            MemberService memberService
    ) {
        this.memberService = memberService;
    }

    @Override
    public Principal getNewPrincipal() {
        Principal newPrincipal = new EmailPrincipal("guest.dummy@clueride.com");
        principals.add(newPrincipal);
        return newPrincipal;
    }

    @Override
    public void validate(String email) {
        if (principals == null) {
            principals = new ArrayList<>();
            getCount();
        }

        try {
            Principal principal = new EmailPrincipal(email);
            /* Test existence within the database */
            Member member = memberService.getMemberByEmail(email);
            principals.add(principal);
        }  catch (Exception e) {
            throw new InvalidClaimException("Unable to verify email");
        }
    }

    public int getCount() {
        principals.clear();
        LOGGER.debug("Refreshing from DB");
        for (Member member : memberService.getAllMembers()) {
            principals.add(new EmailPrincipal(member.getEmailAddress()));
        }
        LOGGER.debug("Found " + principals.size() + " records");
        return principals.size();
    }
}
