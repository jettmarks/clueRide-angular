package com.clueride.service;/*
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
 * Created by jett on 10/17/17.
 */

import java.security.Principal;

import javax.inject.Provider;

import com.google.inject.Inject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.CoreGuiceModuleTest;
import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberService;
import com.clueride.rest.dto.CRCredentials;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the AuthenticationServiceImplTest class.
 */
@Guice(modules=CoreGuiceModuleTest.class)
public class AuthenticationServiceImplTest {
    private AuthenticationServiceImpl toTest;

    @Inject
    private Provider<AuthenticationServiceImpl> toTestProvider;

    @Inject
    private Member member;

    @Inject
    private MemberService memberService;

    @BeforeMethod
    public void setUp() throws Exception {
        reset(
                memberService
        );
        toTest = toTestProvider.get();
        assertNotNull(toTest);
        assertNotNull(member);
    }

    @Test
    public void testEstablishSession() throws Exception {
    }

    @Test
    public void testGetPrincipal() throws Exception {
        /* setup test */
        String emailWithUpper = "Guest.Dummy@ClueRide.com";
        String expected = emailWithUpper.toLowerCase();
        CRCredentials crCredentials = new CRCredentials();
        crCredentials.name = emailWithUpper;
        crCredentials.password = "password";

        /* train mocks */
        when(memberService.getMemberByEmail(expected)).thenReturn(member);

        /* make call */
        Principal principal = toTest.getPrincipal(crCredentials);
        assertNotNull(principal);

        /* verify results */
        verify(memberService).getMemberByEmail(expected);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testGetPrincipal_notFound() {
        /* setup test */
        String emailWithUpper = "Guest.Dummy@ClueRide.com";
        CRCredentials crCredentials = new CRCredentials();
        crCredentials.name = emailWithUpper;
        crCredentials.password = "password";

        /* train mocks */
        doThrow(RuntimeException.class).when(memberService).getMemberByEmail(anyString());

        /* make call */
        toTest.getPrincipal(crCredentials);

        /* verify results */
    }
}