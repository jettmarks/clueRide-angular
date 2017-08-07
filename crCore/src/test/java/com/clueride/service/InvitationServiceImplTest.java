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

import javax.inject.Inject;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.dao.CourseStore;
import com.clueride.dao.CourseTypeStore;
import com.clueride.dao.InvitationStore;
import com.clueride.dao.MemberStore;
import com.clueride.domain.Invitation;
import com.clueride.domain.Outing;
import com.clueride.domain.account.Member;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the InvitationServiceImplTest class.
 *
 * Main logic being tested here is the validation of the objects
 * passed into the constructor for an Invitation.
 *
 * Some of the methods are essentially pass-through calls to the underlying
 * Store and are not tested here.
 */
public class InvitationServiceImplTest {

    private InvitationService toTest;

    @Mock
    private InvitationStore invitationStore;

    @Mock
    private MemberStore memberStore;

    @Inject
    private CourseStore courseStore;

    @Mock
    private CourseTypeStore courseTypeStore;

    @Mock
    private TeamService teamService;

    private Outing outing;
    private Member member;

    private static final int TEST_COURSE_ID = 1234;
    private static final int TEST_MEMBER_ID = 123;
    private static final int TEST_TEAM_ID = 321;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        outing = createTestOuting();
        member = createTestMember();
        toTest = new InvitationServiceImpl(
                invitationStore,
                memberStore,
                courseStore,
                courseTypeStore,
                teamService
        );
    }

    private Outing createTestOuting() {
        return Outing.Builder.builder()
                .withCourseId(TEST_COURSE_ID)
                .withTeamId(TEST_TEAM_ID)
                .build();
    }

//    @Test
    /* This method is essentially a pass-through to the underlying store. */
    public void testGetInvitationByToken() throws Exception {
    }

//    @Test
    /* This method is essentially a pass-through to the underlying store. */
    public void testGetInvitationsForOuting() throws Exception {
    }

    @Test
    public void testCreateNew() throws Exception {
        when(memberStore.getMemberById(TEST_MEMBER_ID)).thenReturn(member);
        Invitation actual = toTest.createNew(outing, member.getId());
        assertNotNull(actual);
        assertNotNull(actual.getToken());
    }

    @Test
    public void testCreateNew_TokenCheck() throws Exception {
        when(memberStore.getMemberById(TEST_MEMBER_ID)).thenReturn(member);
        Invitation firstInvite = toTest.createNew(outing, member.getId());
        System.out.println("First  Token: " + firstInvite.getToken());
        Member member2 = createTestMember();
        member2.withId(TEST_MEMBER_ID + 1);
        when(memberStore.getMemberById(TEST_MEMBER_ID + 1)).thenReturn(member2);
        Invitation secondInvite = toTest.createNew(outing, member2.getId());
        System.out.println("Second Token: " + secondInvite.getToken());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreateNew_InvalidMember() throws Exception {
        member.withEmailAddress(null);
        when(memberStore.getMemberById(TEST_MEMBER_ID)).thenReturn(member);
        Invitation actual = toTest.createNew(outing, member.getId());
    }

    private Member createTestMember() {
        // TODO: Replace this with the injected instance.
        Member member = new Member();
        member.withId(TEST_MEMBER_ID);
        member.withDisplayName("testee");
        member.withEmailAddress("nowhere@bad-domain.com");
        return member;
    }
}