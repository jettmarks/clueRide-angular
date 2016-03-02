/*
 * Copyright 2015 Jett Marks
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
 * Created by jett on 11/29/15.
 */
package com.clueride.domain.user;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class LocationTest {

    Location toTest;
    Location.Builder builder;

    // Test values
    String expectedName = "Test Location";
    String expectedDescription = "Here's a nice spot to spread out the blanket or toss the frisbee.";
    LocationType expectedLocationType = LocationType.PICNIC;
    Integer expectedNodeId = 123;
    List<Integer> expectedClues = new ArrayList<>();
    List<URL> expectedImageUrls = new ArrayList<>();

    @BeforeMethod
    public void setUp() throws Exception {
        expectedClues.add(1);
        expectedClues.add(2);
        expectedClues.add(3);
        expectedClues.add(4);
        expectedClues.add(5);
        expectedClues.add(6);
        expectedClues.add(7);
        expectedImageUrls.add(new URL("https://clueride.com/"));
        builder = Location.Builder.builder()
                .setName(expectedName)
                .setDescription(expectedDescription)
                .setLocationType(expectedLocationType)
                .setNodeId(expectedNodeId)
                .setClueIds(expectedClues)
                .setImageUrls(expectedImageUrls);
    }

    @Test(expectedExceptions =NullPointerException.class, expectedExceptionsMessageRegExp = "Location name missing")
    public void testGetName() throws Exception {
        toTest = builder.build();
        assertNotNull(toTest);
        String actual = toTest.getName();
        assertEquals(actual, expectedName);
        builder.setName(null);
        toTest = builder.build();
        assertNull(toTest);
    }

    @Test(expectedExceptions =NullPointerException.class, expectedExceptionsMessageRegExp = "Location description missing")
    public void testGetDescription() throws Exception {
        toTest = builder.build();
        assertNotNull(toTest);
        String actual = toTest.getDescription();
        assertEquals(actual, expectedDescription);
        builder.setDescription(null);
        toTest = builder.build();
        assertNull(toTest);
    }

    @Test(expectedExceptions =NullPointerException.class, expectedExceptionsMessageRegExp = "Location Type missing")
    public void testGetLocationType() throws Exception {
        toTest = builder.build();
        assertNotNull(toTest);
        LocationType actual = toTest.getLocationType();
        assertEquals(actual, expectedLocationType);
        builder.setLocationType(null);
        toTest = builder.build();
        assertNull(toTest);
    }

    @Test(expectedExceptions =NullPointerException.class, expectedExceptionsMessageRegExp = "Location Node \\(point\\) missing")
    public void testGetNodeId() throws Exception {
        toTest = builder.build();
        assertNotNull(toTest);
        Integer actual = toTest.getNodeId();
        assertEquals(actual, expectedNodeId);
        builder.setNodeId(null);
        toTest = builder.build();
        assertNull(toTest);
    }

//    @Test
//    public void testGetTags() throws Exception {
//        toTest = builder.build();
//        assertNotNull(toTest);
//        Set<String> actual = toTest.getTags();
//        assertNotNull(actual);
//
//        Map<String,Double> moreTags = new HashMap<>();
//        Double expected = 1.234;
//        moreTags.put("T1", expected);
//
//        builder.setTagScores(moreTags);
//        toTest = builder.build();
//        assertNotNull(toTest);
//        assertEquals(toTest.getTags().size(), 1);
//    }

    @Test
    public void testGetScorePerTag() throws Exception {
        toTest = builder.build();
        assertNotNull(toTest);

        Map<String,Optional<Double>> moreTags = new HashMap<>();
        Double expected = 1.234;
//        Optional<Double> expected = Optional.of(1.234);
        moreTags.put("T1", Optional.of(expected));

        builder.setTagScores(moreTags);
        toTest = builder.build();
        assertNotNull(toTest);
        Optional<Double> actual = toTest.getScorePerTag("T1");
        Double defaultScore = 1.0;
        assertEquals(actual.or(defaultScore), expected);
    }

    @Test
    public void testGetLocationGroupId() throws Exception {
        toTest = builder.build();
        assertNotNull(toTest);

        Integer expectedLocationId = 1234;
//        Optional<Integer> expectedLocationId = Optional.of(1234);
        builder.setLocationGroupId(Optional.of(expectedLocationId));
        toTest = builder.build();
        assertNotNull(toTest);

        Integer actual = toTest.getLocationGroupId();
        assertEquals(actual, expectedLocationId);
    }

    @Test
    public void testGetEstablishment() throws Exception {
        toTest = builder.build();
        assertNotNull(toTest);

        String expectedName = "Atlanta Bicycle";
        Optional<Establishment> expectedEstablishment = Optional.of(new Establishment(expectedName));
        builder.setEstablishment(expectedEstablishment);
        toTest = builder.build();
        assertNotNull(toTest);

        Establishment actual = toTest.getEstablishment().orNull();
        assertEquals(actual.getName(), expectedName);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetClues() throws Exception {
        toTest = builder.build();
        assertNotNull(toTest);

        List<Integer> actual = toTest.getClueIds();
        assertTrue(actual.size() > 0);

        builder.setClueIds(Collections.<Integer>emptyList());
        toTest = builder.build();
    }

    @Test
    public void testAddDeleteClues() throws Exception {
        toTest = builder.build();
        List<Integer> expectedCluesAfterDelete = toTest.getClueIds();
        List<Integer> expectedCluesAfterAdd = new ArrayList<>(expectedCluesAfterDelete);
        expectedCluesAfterAdd.add(10);

        toTest = builder.setClueIds(expectedCluesAfterAdd).build();
        List<Integer> actualAfterAdd = toTest.getClueIds();
        assertEquals(actualAfterAdd, expectedCluesAfterAdd);

        toTest.removeClue(10);
        List<Integer> actualAfterDelete = toTest.getClueIds();
        assertEquals(actualAfterDelete, expectedCluesAfterDelete);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetImageUrls() throws Exception {
        toTest = builder.build();
        assertNotNull(toTest);

        List<URL> actual = toTest.getImageUrls();
        assertTrue(actual.size() > 0);

        builder.setImageUrls(Collections.<URL>emptyList());
        toTest = builder.build();
    }
}