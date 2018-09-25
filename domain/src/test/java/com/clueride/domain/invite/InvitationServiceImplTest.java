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
package com.clueride.domain.invite;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.domain.DomainGuiceModuleTest;
import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberStore;
import com.clueride.domain.account.principal.BadgeOsPrincipal;
import com.clueride.domain.account.principal.SessionPrincipal;
import com.clueride.domain.outing.Outing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
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
@Guice(modules = DomainGuiceModuleTest.class)
public class InvitationServiceImplTest {

    private static final int TEST_COURSE_ID = 1234;
    private static final int TEST_MEMBER_ID = 123;
    private static final int TEST_TEAM_ID = 321;

    private InvitationService toTest;

    @Inject
    private InvitationStore invitationStore;

    @Inject
    private Provider<InvitationService> toTestProvider;

    @Inject
    private Member member;

    @Inject
    private MemberStore memberStore;

    private Outing outing;

    @Inject
    private SessionPrincipal sessionPrincipal;
    @Inject
    private BadgeOsPrincipal principal;

    @Inject
    private Invitation invitation;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        outing = createTestOuting();
        toTest = toTestProvider.get();
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
        /* train mocks */
        when(memberStore.getMemberById(member.getId())).thenReturn(member);
        /* make call */
        Invitation actual = toTest.createNew(outing.getId(), member.getId());
        /* verify results */
        assertNotNull(actual);
        assertNotNull(actual.getToken());
    }

//    @Test     TODO unsure if Tokens have a role to play
    public void testCreateNew_TokenCheck() throws Exception {
        when(memberStore.getMemberById(TEST_MEMBER_ID)).thenReturn(member);
        Invitation firstInvite = toTest.createNew(outing.getId(), member.getId());
        System.out.println("First  Token: " + firstInvite.getToken());
        Member member2 = Member.Builder.from(member)
                .withId(TEST_MEMBER_ID + 1)
                .build();
        when(memberStore.getMemberById(TEST_MEMBER_ID + 1)).thenReturn(member2);
        Invitation secondInvite = toTest.createNew(outing.getId(), member2.getId());
        System.out.println("Second Token: " + secondInvite.getToken());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreateNew_InvalidMember() throws Exception {
        member.withEmailAddress(null);
        when(memberStore.getMemberById(TEST_MEMBER_ID)).thenReturn(member);
        Invitation actual = toTest.createNew(outing.getId(), member.getId());
    }

    @Test
    public void testGetInvitationsForSession() {
        /* setup test */

        /* train mocks */
        when(sessionPrincipal.getSessionPrincipal()).thenReturn(principal);
        when(memberStore.getMemberByEmail(principal.getEmailAddress())).thenReturn(member);
        when(invitationStore.getUpcomingInvitationsByMemberId(member.getId()))
                .thenReturn(Collections.singletonList(Invitation.Builder.from(invitation)));

        /* make call */
        List<Invitation> invites = toTest.getInvitationsForSession();

        /* verify results */
        assertNotNull(invites);
        assertEquals(1, invites.size());
        assertEquals(invites.get(0), invitation);
    }
}