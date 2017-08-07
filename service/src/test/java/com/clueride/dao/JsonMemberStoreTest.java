package com.clueride.dao;/*
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
 * Created by jett on 8/6/17.
 */

import javax.inject.Inject;
import javax.inject.Provider;
import javax.mail.internet.InternetAddress;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.domain.account.Member;
import com.clueride.exc.RecordNotFoundException;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the JsonMemberStoreTest class.
 */
@Guice(modules = ServiceGuiceModuleTest.class)
public class JsonMemberStoreTest {
    private JsonMemberStore toTest;
    private Member testMember;

    @Inject
    private Provider<JsonMemberStore> toTestProvider;

    @Inject
    private Member member;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        toTest = toTestProvider.get();
        testMember = Member.Builder.from(member)
                .withEmailAddress("test.member@clueride.com")
                .build();
    }

    @Test
    public void testAddNew() throws Exception {
        /* make call */
        toTest.addNew(testMember);
    }

    @Test
    public void testGetMemberById() throws Exception {
    }

    @Test
    public void testGetMemberByName() throws Exception {
    }

    @Test
    public void testGetMemberByEmail() throws Exception {
        /* set up */
        Member expected = testMember;
        toTest.addNew(testMember);

        /* make call */
        Member actual = toTest.getMemberByEmail(new InternetAddress(testMember.getEmailAddress()));

        /* verify results */
        assertNotNull(actual);
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = RecordNotFoundException.class)
    public void testGetMemberByEmail_recordNotFound() throws Exception {
        toTest.getMemberByEmail(new InternetAddress("cant.find@clueride.com"));
    }

    @Test
    public void testUpdate() throws Exception {
    }

}