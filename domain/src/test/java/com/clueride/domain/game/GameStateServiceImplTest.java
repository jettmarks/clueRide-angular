package com.clueride.domain.game;
/*
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
 * Created by jett on 9/14/18.
 */

import javax.inject.Inject;
import javax.inject.Provider;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.domain.DomainGuiceModuleTest;
import com.clueride.domain.ssevent.SSEventService;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the GameStateServiceImplTest class.
 */

@Guice(modules= DomainGuiceModuleTest.class)
public class GameStateServiceImplTest {
    private GameStateServiceImpl toTest;

    private static final Integer OUTING_ID = 2;

    @Inject
    private Provider<GameStateServiceImpl> toTestProvider;

    @Inject
    private GameState gameState;

    @Inject
    private SSEventService ssEventService;

    @BeforeMethod
    public void setup() {
        initMocks(this);
        reset(
                ssEventService
        );
        toTest = toTestProvider.get();
        assertNotNull(toTest);
    }

    @Test
    public void testGetGameStateByTeam() throws Exception {
    }

    @Test
    public void testUpdateGameStateByTeam() throws Exception {
    }

    @Test
    public void testUpdateGameStateWithTeamAssembled() {
        /* setup test */
        GameState expected = GameState.Builder.builder()
                .withTeamAssembled(true)
                .build();

        /* train mocks */

        /* make call */
        GameState actual = toTest.updateWithTeamAssembled(OUTING_ID);

        /* verify results */
        assertEquals(actual, expected);
        verify(ssEventService, times(1)).sendTeamReadyEvent(OUTING_ID);
    }

    @Test
    public void testUpdateOutingStateWithDeparture() throws Exception {
        /* setup test */
        Integer outingId = OUTING_ID;
        GameState expected = GameState.Builder.from(gameState)
                .withTeamAssembled(true)
                .withRolling(true)
                .withPathIndex(1)
                .build();

        /* train mocks */
        toTest.updateWithTeamAssembled(OUTING_ID);

        /* make call */
        GameState actual = toTest.updateOutingStateWithDeparture(outingId);

        /* verify results */
        assertEquals(actual, expected);
        verify(ssEventService, times(1)).sendTeamReadyEvent(OUTING_ID);
        verify(ssEventService, times(1)).sendDepartureEvent(OUTING_ID);
        verify(ssEventService, times(0)).sendArrivalEvent(OUTING_ID);
    }

    @Test
    public void testUpdateOutingStateWithArrival_AtMeetingLocation() throws Exception {
        /* setup test */
        GameState expected = GameState.Builder.from(gameState)
                .withTeamAssembled(true)
                .withRolling(false)
                .withPathIndex(1)
                .build();

        /* train mocks */
        toTest.updateWithTeamAssembled(OUTING_ID);
        toTest.updateOutingStateWithDeparture(OUTING_ID);

        /* make call */
        GameState actual = toTest.updateOutingStateWithArrival(OUTING_ID);

        /* verify results */
        assertEquals(actual, expected);
        verify(ssEventService, times(1)).sendTeamReadyEvent(OUTING_ID);
        verify(ssEventService, times(1)).sendDepartureEvent(OUTING_ID);
        verify(ssEventService, times(1)).sendArrivalEvent(OUTING_ID);
    }

}