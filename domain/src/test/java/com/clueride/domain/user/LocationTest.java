package com.clueride.domain.user;

import com.google.common.base.Optional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.*;

import static org.testng.Assert.*;

/**
 * Copyright 2015 Jett Marks
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Created by jett on 11/29/15.
 */
public class LocationTest {

    Location toTest;
    Location.Builder builder;

    // Test values
    String expectedName = "Test Location";
    String expectedDescription = "Here's a nice spot to spread out the blanket or toss the frisbee.";
    LocationType expectedLocationType = LocationType.PICNIC;
    Integer expectedNodeId = 123;
    List<Clue> expectedClues = new ArrayList<>();
    List<URL> expectedImageUrls = new ArrayList<>();

    @BeforeMethod
    public void setUp() throws Exception {
        expectedClues.add(new Clue());
        expectedImageUrls.add(new URL("https://clueride.com/"));
        builder = Location.Builder.builder()
                .setName(expectedName)
                .setDescription(expectedDescription)
                .setLocationType(expectedLocationType)
                .setNodeId(expectedNodeId)
                .setClues(expectedClues)
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

    @Test
    public void testGetTags() throws Exception {
        toTest = builder.build();
        assertNotNull(toTest);
        Set<String> actual = toTest.getTags();
        assertNotNull(actual);

        Map<String,Double> moreTags = new HashMap<>();
        Double expected = 1.234;
        moreTags.put("T1", expected);

        builder.setTagScores(moreTags);
        toTest = builder.build();
        assertNotNull(toTest);
        assertEquals(toTest.getTags().size(), 1);
    }

    @Test
    public void testGetScorePerTag() throws Exception {
        toTest = builder.build();
        assertNotNull(toTest);

        Map<String,Double> moreTags = new HashMap<>();
        Double expected = 1.234;
        moreTags.put("T1", expected);

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
        builder.setLocationGroupId(expectedLocationId);
        toTest = builder.build();
        assertNotNull(toTest);

        Integer actual = toTest.getLocationGroupId().orNull();
        assertEquals(actual, expectedLocationId);
    }

    @Test
    public void testGetEstablishment() throws Exception {
        toTest = builder.build();
        assertNotNull(toTest);

        String expectedName = "Atlanta Bicycle";
        Establishment expectedEstablishment = new Establishment(expectedName);
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

        List<Clue> actual = toTest.getClues();
        assertTrue(actual.size() > 0);

        builder.setClues(Collections.<Clue>emptyList());
        toTest = builder.build();
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