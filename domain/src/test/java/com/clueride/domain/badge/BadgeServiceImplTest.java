package com.clueride.domain.badge;/*
 * Copyright 2018 Jett Marks
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
 * Created by jett on 8/27/18.
 */

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.domain.DomainGuiceModuleTest;
import com.clueride.domain.account.principal.BadgeOsPrincipal;
import com.clueride.domain.account.principal.SessionPrincipal;
import com.clueride.infrastructure.DbSourced;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the BadgeServiceImplTest class.
 */
@Guice(modules = DomainGuiceModuleTest.class)
public class BadgeServiceImplTest {

    private BadgeServiceImpl toTest;

    @Inject
    private Provider<BadgeServiceImpl> toTestProvider;

    @Inject
    private BadgeOsPrincipal badgeOsPrincipal;

    @Inject
    private BadgeStore badgeStore;

    @Inject
    private BadgeTypeService badgeTypeService;

    @Inject
    @DbSourced
    private Badge.Builder badgeBuilderFromDb;

    @Inject
    private SessionPrincipal sessionPrincipal;

    @BeforeMethod
    public void setUp() throws Exception {
        assertNotNull(toTestProvider);
        toTest = toTestProvider.get();
        assertNotNull(toTest);
    }

    @Test
    public void testGetBadges() {
        /* setup test */
        Badge expectedBadge = badgeBuilderFromDb.withBadgeType(BadgeType.SEEKER).build();
        List<Badge> expected = Collections.singletonList(expectedBadge);

        /* train mocks */
        when(badgeStore.getAwardedBadgesForUser(2)).thenReturn(
                Collections.singletonList(badgeBuilderFromDb)
        );
        when(badgeTypeService.getTypeOfBadge(badgeBuilderFromDb)).thenReturn(
                BadgeType.SEEKER
        );
        when(sessionPrincipal.getSessionPrincipal()).thenReturn(
            badgeOsPrincipal
        );

        /* make call */
        List<Badge> actual = toTest.getBadges();

        /* verify results */
        assertEquals(actual.get(0), expected.get(0));

    }

}