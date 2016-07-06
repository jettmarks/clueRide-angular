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
import org.testng.annotations.Test;

import com.clueride.dao.OutingStore;
import com.clueride.domain.Course;
import com.clueride.domain.Outing;
import com.clueride.domain.Team;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * Exercises the Outing Service.
 */
public class OutingServiceImplTest {

    @Inject
    private OutingService toTest;

    @Inject
    private Provider<OutingService> testProvider;

    @Mock
    private Team team1, team2;

    @Mock
    private OutingStore outingStore;

    @Mock
    private Course course1, course2;

    private List<Integer> outingIds;

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
        toTest = new OutingServiceImpl(outingStore);
        populateToTest(toTest);
        assertTrue(outingIds.size() > 0);
    }

    @Test
    public void testGetByOutingId() throws Exception {
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
                .setTeamId(101)
                .setCourseId(14)
                .build();
        Outing created = toTest.createOuting(expected);
        assertNotNull(created);
        Integer outingId = created.getId();
        assertNotNull(created.getId());
        Outing actual = toTest.getByOutingId(outingId);
        assertEquals(actual, expected);
        expected = toTest.getByTeamId(101);
        assertEquals(actual, expected);
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
            assertNotEquals(toTest.lastStateChange(outingId), new Long(0L));
        }
    }

    @Test
    public void testUpdatePosition() throws Exception {
        for (Integer outingId : outingIds) {
            toTest.updatePosition(outingId);
            assertNotEquals(toTest.lastPositionChange(outingId), new Long(0L));
        }
    }

    /* Helper methods. */

    private void populateToTest(OutingService toTest) {
        when(team1.getId()).thenReturn(101);
        when(team2.getId()).thenReturn(102);
        outingIds = new ArrayList<>();
        outingIds.add(
                toTest.createOuting(Outing.Builder.builder()
                        .setTeamId(101)
                        .setCourseId(14)
                        .build()).getId()
        );

        outingIds.add(
                toTest.createOuting(Outing.Builder.builder()
                        .setTeamId(102)
                        .setCourseId(14)
                        .build()).getId()
        );
    }
}