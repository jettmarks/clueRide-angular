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
 * Created by jett on 1/16/16.
 */
package com.clueride.domain;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.domain.user.Location;
import com.clueride.domain.user.Path;
import com.clueride.service.IdProvider;
import com.clueride.service.MemoryBasedCourseIdProvider;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

/**
 * Exercises the GameCourseTest class.
 */
public class GameCourseTest {

    @Mock
    private Location startLocation;

    @Mock
    private Location secondLocation;

    @Mock
    private Location destinationLocation;

    @Mock
    private Path firstPath;

    @Mock
    private Path secondPath;

    private GameCourse toTest;

    private IdProvider idProvider = new MemoryBasedCourseIdProvider();

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
        GameCourse.Builder builder = GameCourse.Builder.getBuilder();
        toTest = builder
                .withName("Test Course")
                .withDescription("Trying out the Builder for a Game Course")
                .addLocation(startLocation)
                .addPath(firstPath)
                .addLocation(secondLocation)
                .addPath(secondPath)
                .addLocation(destinationLocation)
                .build();
    }

    @Test
    public void testGetId() throws Exception {
        Integer expected = idProvider.getLastId();
        Integer actual = toTest.getId();
        assertEquals(actual, expected, "ID should have been assigned");
    }

    @Test
    public void testGetDeparture() throws Exception {
        Location expected = startLocation;
        Location actual = toTest.getDeparture();
        assertEquals(actual, expected, "Departure location should be first location provided");
    }

    @Test
    public void testGetDestination() throws Exception {
        Location expected = destinationLocation;
        Location actual = toTest.getDestination();
        assertEquals(actual, expected, "Destination location should be last location provided");
    }

    @Test
    public void testNextStep() throws Exception {
        Step expected = firstPath;
        Step actual = toTest.nextStep();
        assertEquals(actual, expected, "First Path should be the next step");
    }

    @Test
    public void testCurrentStep() throws Exception {
        Step expected = startLocation;
        Step actual = toTest.currentStep();
        assertEquals(actual,expected, "Departure should be the current step");
    }

    @Test
    public void testGetSteps() throws Exception {

    }

    @Test
    public void testGetGeoJson() throws Exception {

    }
}