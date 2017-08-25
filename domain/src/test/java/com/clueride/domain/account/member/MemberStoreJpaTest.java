package com.clueride.domain.account.member;/*
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
 * Created by jett on 8/13/17.
 */

import javax.inject.Inject;
import javax.inject.Provider;
import javax.mail.internet.InternetAddress;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.domain.DomainGuiceModuleTest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the MemberStoreJpaTest class.
 */
@Guice(modules = DomainGuiceModuleTest.class)
public class MemberStoreJpaTest {
    private MemberStoreJpa toTest;

    private InternetAddress email;

    @Inject
    private Provider<MemberStoreJpa> toTestProvider;

    @BeforeMethod
    public void setUp() throws Exception {
        toTest = toTestProvider.get();
    }

    @AfterMethod
    public void tearDown() throws Exception {
    }

    @Test
    public void testInstantiation() {
        assertNotNull(toTest);
    }

    @Test
    public void testAddNew() throws Exception {
    }

    @Test
    public void testGetMemberById() throws Exception {
    }

    @Test
    public void testGetMemberByName() throws Exception {
    }

    @Test
    public void testGetMemberByEmail() throws Exception {
        /* expected */
        Member expected = Member.Builder.builder()
                .withEmailAddress("test.email@clueride.com")
                .withDisplayName("tester")
                .withFirstName("Test")
                .withLastName("Account")
                .withPhone("123.456.7890")
                .build();
        email = new InternetAddress("test.email@clueride.com");

        /* make call */
        Member actual = toTest.getMemberByEmail(email);
        assertNotNull(actual);

        /* verify results */
        assertEquals(actual, expected);
    }

    @Test
    public void testUpdate() throws Exception {
    }

    @Test
    public void testGetAllMembers() throws Exception {
    }

}