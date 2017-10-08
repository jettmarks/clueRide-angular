package com.clueride.member;/*
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
 * Created by jett on 10/7/17.
 */

import javax.inject.Inject;
import javax.inject.Provider;
import javax.mail.internet.InternetAddress;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.CoreGuiceModuleTest;
import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberStore;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the MemberServiceImplTest class.
 */
@Guice(modules= CoreGuiceModuleTest.class)
public class MemberServiceImplTest {
    private MemberServiceImpl toTest;
    private InternetAddress goodAddress;

    @Inject
    private Provider<MemberServiceImpl> toTestProvider;

    @Inject
    private MemberStore memberStore;

    @Inject
    private Member member;

    @BeforeMethod
    public void setUp() throws Exception {
        toTest = toTestProvider.get();
        goodAddress = new InternetAddress(member.getEmailAddress());
        assertNotNull(toTest);
    }

    @Test
    public void testGetMember() throws Exception {
    }

    @Test
    public void testGetMemberByDisplayName() throws Exception {
    }

    @Test
    public void testGetMemberByEmail() throws Exception {
        Member expected = member;
        when(memberStore.getMemberByEmail(goodAddress)).thenReturn(expected);

        Member actual = toTest.getMemberByEmail(goodAddress.getAddress());
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testGetMemberByEmail_badEmail() throws Exception {
        toTest.getMemberByEmail("");
    }

    @Test
    public void testCreateNewMemberWithEmail() throws Exception {
    }

    @Test
    public void testGetAllMembers() throws Exception {
    }

}