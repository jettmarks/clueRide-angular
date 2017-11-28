package com.clueride.domain.badge.event;/*
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
 * Created by jett on 11/25/17.
 */

import java.security.Principal;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Provider;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.domain.DomainGuiceModuleTest;
import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberService;
import com.clueride.domain.account.principal.EmailPrincipal;
import com.clueride.domain.account.principal.PrincipalService;
import com.clueride.infrastructure.ClientSourced;
import com.clueride.infrastructure.DbSourced;
import com.clueride.infrastructure.ServiceSourced;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the BadgeEventServiceImplTest class.
 */
@Guice(modules = DomainGuiceModuleTest.class)
public class BadgeEventServiceImplTest {
    private BadgeEventServiceImpl toTest;
    private Date sharedTimestamp = new Date();

    @Inject
    private BadgeEventStore badgeEventStore;

    @Inject
    private Provider<BadgeEventServiceImpl> toTestProvider;

    @Inject
    @ClientSourced
    private BadgeEvent.Builder badgeEventBuilderFromClient;

    @Inject
    @DbSourced
    private BadgeEvent.Builder badgeEventBuilderFromDb;

    @Inject
    @ServiceSourced
    private BadgeEvent.Builder badgeEventBuilderFromService;

    @Inject
    private BadgeEvent badgeEvent;

    @Inject
    private MemberService memberService;

    @Inject
    private Member member;

    @Inject
    private PrincipalService principalService;

    @BeforeMethod
    public void setUp() throws Exception {
        reset(
                badgeEventStore,
                memberService,
                principalService
        );
        toTest = toTestProvider.get();
        assertNotNull(toTest);
    }

    @Test
    public void testSend() throws Exception {
        /* setup test */

        /* train mocks */
        Principal principal = badgeEventBuilderFromClient.getPrincipal();
        when(memberService.getMemberByEmail(principal.getName())).thenReturn(member);

        /* make call */
        toTest.send(badgeEventBuilderFromClient);

        /* Allow other thread to complete. */
        Thread.sleep(100);

        /* verify results */
        verify(badgeEventStore).add(badgeEventBuilderFromClient);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testSend_null() throws Exception {
       /* setup test */

       /* train mocks */

       /* make call */
        toTest.send(null);

        /* Allow other thread to complete. */
        Thread.sleep(100);

       /* verify results */
    }

    @Test
    public void testSend_storeFailure() throws Exception {
        /* setup test */

        /* train mocks */
        Principal principal = badgeEventBuilderFromClient.getPrincipal();
        when(memberService.getMemberByEmail(principal.getName())).thenReturn(member);
        doThrow(RuntimeException.class).when(badgeEventStore).add(badgeEventBuilderFromClient);

        /* make call */
        toTest.send(badgeEventBuilderFromClient);

        /* Allow other thread to complete. */
        Thread.sleep(100);

        /* verify results */
    }

    /* Population of Transient Fields into/from Columns */

    @Test
    public void testFillPrincipal() throws Exception {
        /* setup test */
        Integer badgeEventId = badgeEvent.getId();
        Integer memberId = badgeEventBuilderFromDb.getMemberId();
        Principal alternatePrincipal = new EmailPrincipal("guest.different@clueride.com");
        BadgeEvent expected = badgeEventBuilderFromService
                .withId(badgeEventBuilderFromDb.getId())
                .withTimestamp(sharedTimestamp)
                .withPrincipal(alternatePrincipal)
                .build();

        /* train mocks */
        when(badgeEventStore.getById(badgeEventId)).thenReturn(
                badgeEventBuilderFromDb
                        .withTimestamp(sharedTimestamp)
        );
        when(memberService.getMember(memberId)).thenReturn(member);
        when(principalService.getPrincipalForEmailAddress(member.getEmailAddress())).thenReturn(alternatePrincipal);

        /* make call */
        BadgeEvent actual = toTest.getBadgeEventById(badgeEventId);

        /* verify results */
        assertEquals(actual.getPrincipal(), expected.getPrincipal());
    }

    @Test
    public void testFillBuilder() throws Exception {
        /* setup test */
        BadgeEvent.Builder expected = BadgeEvent.Builder.from(badgeEvent)
                .withMemberId(member.getId())
                .withTimestamp(sharedTimestamp)
                .withId(null);

        /* train mocks */
        Principal principal = badgeEventBuilderFromClient.getPrincipal();
        when(memberService.getMemberByEmail(principal.getName())).thenReturn(member);

        /* make call */
        toTest.send(badgeEventBuilderFromClient.withTimestamp(sharedTimestamp));

        /* Allow other thread to complete. */
        Thread.sleep(100);

        /* verify results */
        verify(badgeEventStore).add(expected);
    }

    @Test
    public void testFrom() throws Exception {
        /* make call */
        BadgeEvent.Builder expected = BadgeEvent.Builder.from(badgeEvent);

        /* verify results */
        assertNotNull(expected.getId());
        assertNotNull(expected.getReturnValueAsString());
        assertNotNull(expected.getMemberId());
        assertNotNull(expected.getClassName());
    }
}