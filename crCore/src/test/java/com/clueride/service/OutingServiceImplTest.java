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
 * Created by jett on 3/5/16.
 */
package com.clueride.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.CoreGuiceModuleTest;
import com.clueride.domain.Team;
import com.clueride.domain.outing.Outing;
import com.clueride.domain.outing.OutingStore;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * Exercises the Outing Service.
 */
@Guice(modules = CoreGuiceModuleTest.class)
public class OutingServiceImplTest {
    private OutingServiceImpl toTest;
    private List<Outing> outings;
    private List<Integer> outingIds;
    private static final Integer OUTING_ID1 = 1;
    private static final Integer OUTING_ID2 = 2;
    private static final Integer TEAM_ID1 = 101;
    private static final Integer TEAM_ID2 = 102;

    @Inject
    private Outing outing;

    @Inject
    private Provider<OutingServiceImpl> testProvider;

    @Inject
    private OutingStore outingStore;

    @Mock
    private Team team1, team2;

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
        toTest = testProvider.get();
        assertNotNull(toTest);
        populateToTest(toTest);
        assertTrue(outingIds.size() > 0);
    }

    @Test
    public void testGetByOutingId() throws Exception {
        /* train mocks */
        when(outingStore.getOutingById(OUTING_ID1)).thenReturn(outings.get(0));
        when(outingStore.getOutingById(OUTING_ID2)).thenReturn(outings.get(1));

        for (Integer outingId : outingIds) {
            assertNotNull(toTest.getByOutingId(outingId));
        }
    }

    @Test
    public void testGetByTeamId() throws Exception {
        assertNotNull(toTest.getByTeamId(101));
        assertNotNull(toTest.getByTeamId(102));
    }

    @Test
    public void testCreateOuting() throws Exception {
        Outing expected = Outing.Builder.builder()
                .withTeamId(101)
                .withCourseId(14)
                .build();
        Outing created = toTest.createOuting(expected);
        assertNotNull(created);

        Integer outingId = created.getId();
        assertNotNull(created.getId());

        when(outingStore.getOutingById(outingId)).thenReturn(expected);
        Outing actual = toTest.getByOutingId(outingId);
        assertEquals(expected, actual);

        expected = toTest.getByTeamId(101);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateOuting() throws Exception {

    }

    @Test
    public void testLastStateChange() throws Exception {
        for (Integer outingId : outingIds) {
            assertEquals(toTest.lastStateChange(outingId), new Long(0L));
        }
    }

    @Test
    public void testLastPositionChange() throws Exception {
        for (Integer outingId : outingIds) {
            assertEquals(toTest.lastPositionChange(outingId), new Long(0L));
        }
    }

    @Test
    public void testUpdateState() throws Exception {
        for (Integer outingId : outingIds) {
            toTest.updateState(outingId);
            assertNotEquals(toTest.lastStateChange(outingId), 0L);
        }
    }

    @Test
    public void testUpdatePosition() throws Exception {
        for (Integer outingId : outingIds) {
            toTest.updatePosition(outingId);
            assertNotEquals(toTest.lastPositionChange(outingId), 0L);
        }
    }

    /* Helper methods. */

    private void populateToTest(OutingService toTest) {
        Outing outing1 = Outing.Builder.from(outing)
                .withId(OUTING_ID1)
                .withTeamId(TEAM_ID1)
                .build();
        Outing outing2 = Outing.Builder.from(outing)
                .withId(OUTING_ID2)
                .withTeamId(TEAM_ID2)
                .build();
        when(team1.getId()).thenReturn(TEAM_ID1);
        when(team2.getId()).thenReturn(TEAM_ID2);

        outings = asList(
                outing1,
                outing2
        );

        outingIds = new ArrayList<>();
        outingIds.add(
                toTest.createOuting(
                        outing1
                ).getId()
        );

        outingIds.add(
                toTest.createOuting(
                        outing2
                ).getId()
        );
    }
}