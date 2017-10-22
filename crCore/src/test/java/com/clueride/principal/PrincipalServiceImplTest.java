package com.clueride.principal;/*
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
 * Created by jett on 8/4/17.
 */

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Provider;

import com.auth0.jwt.exceptions.InvalidClaimException;
import com.google.inject.Inject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.CoreGuiceModuleTest;
import com.clueride.domain.account.member.Member;
import com.clueride.member.MemberService;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Exercises the PrincipalServiceImpl class.
 */
@Guice(modules = CoreGuiceModuleTest.class)
public class PrincipalServiceImplTest {
    private PrincipalServiceImpl toTest;

    @Inject
    private Provider<PrincipalServiceImpl> toTestProvider;

    @Inject
    private MemberService memberService;

    @Inject
    private Member member;

    @BeforeMethod
    public void setUp() throws Exception {
        reset(memberService);
        toTest = toTestProvider.get();
    }

    @Test
    public void testGetPrincipalCount() throws Exception {
        List<Member> members = new ArrayList<>();
        members.add(member);
        members.add(Member.Builder.from(member).withEmailAddress("test.member2@clueride.com").build());

        /* train mocks */
        when(memberService.getAllMembers()).thenReturn(members);

        /* make call */
        int principalCount = toTest.getCount();

        /* verify results */
        assertTrue(principalCount == members.size());
    }

    @Test
    public void testGetNewPrincipal() throws Exception {
        Principal principal = toTest.getNewPrincipal();
        assertNotNull(principal);
    }

    @Test
    public void testValidate() throws Exception {
        Principal principal = toTest.getNewPrincipal();
        toTest.validate(principal.getName());
    }

    @Test(expectedExceptions = InvalidClaimException.class)
    public void testValidate_invalidClaim() throws Exception {
        String invalidAddress = "not.a.valid@email.address";
        doThrow(new InvalidClaimException("Oops")).when(memberService).getMemberByEmail(invalidAddress);
        toTest.validate(invalidAddress);
    }

}