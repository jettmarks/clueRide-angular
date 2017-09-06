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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Provider;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.domain.DomainGuiceModuleTest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

@Guice(modules = DomainGuiceModuleTest.class)
public class LocationTest {

    Location toTest;
    Location.Builder builder;

    @Inject
    private Provider<Location.Builder> toTestProvider;

    // Test values
    String expectedName;
    String expectedDescription;
    LocationType expectedLocationType;
    Integer expectedNodeId;

    @BeforeMethod
    public void setUp() throws Exception {
        toTest = toTestProvider.get().build();
        assertNotNull(toTest);
        builder = toTestProvider.get();

        expectedName = builder.getName();
        expectedDescription = builder.getDescription();
        expectedLocationType = builder.getLocationType();
        expectedNodeId = builder.getNodeId();
    }

    @Test(expectedExceptions =NullPointerException.class, expectedExceptionsMessageRegExp = "Location Node \\(point\\) missing")
    public void testGetNodeId() throws Exception {
        Integer actual = toTest.getNodeId();
        assertEquals(actual, expectedNodeId);
        builder.withNodeId(null);
        toTest = builder.build();
        assertNull(toTest);
    }

    @Test
    public void testGetScorePerTag() throws Exception {
        Map<String,Optional<Double>> moreTags = new HashMap<>();
        Double expected = 1.234;
//        Optional<Double> expected = Optional.of(1.234);
        moreTags.put("T1", Optional.of(expected));

        builder.withTagScores(moreTags);
        toTest = builder.build();
        assertNotNull(toTest);
        Optional<Double> actual = toTest.getScorePerTag("T1");
        Double defaultScore = 1.0;
        assertEquals(actual.or(defaultScore), expected);
    }

    @Test
    public void testGetLocationGroupId() throws Exception {
        Integer expectedLocationId = 1234;
//        Optional<Integer> expectedLocationId = Optional.of(1234);
        builder.withLocationGroupId(Optional.of(expectedLocationId));
        toTest = builder.build();
        assertNotNull(toTest);

        Integer actual = toTest.getLocationGroupId();
        assertEquals(actual, expectedLocationId);
    }

    @Test
    public void testGetEstablishment() throws Exception {
        String expectedName = "Atlanta Bicycle";
        Optional<Establishment> expectedEstablishment = Optional.of(new Establishment(expectedName));
        builder.withEstablishment(expectedEstablishment);
        toTest = builder.build();
        assertNotNull(toTest);

        Establishment actual = toTest.getEstablishment().orNull();
        assertEquals(actual.getName(), expectedName);
    }

    @Test
    public void testAddDeleteClues() throws Exception {
        toTest = builder.build();
        List<Integer> expectedCluesAfterDelete = toTest.getClueIds();
        List<Integer> expectedCluesAfterAdd = new ArrayList<>(expectedCluesAfterDelete);
        expectedCluesAfterAdd.add(10);

        toTest = builder.withClueIds(expectedCluesAfterAdd).build();
        List<Integer> actualAfterAdd = toTest.getClueIds();
        assertEquals(actualAfterAdd, expectedCluesAfterAdd);

        toTest.removeClue(10);
        List<Integer> actualAfterDelete = toTest.getClueIds();
        assertEquals(actualAfterDelete, expectedCluesAfterDelete);
    }

    /* Readiness level. */

    @Test
    public void testReadinessLevel_Attraction() throws Exception {
        LocationLevel expected = LocationLevel.ATTRACTION;
        LocationLevel actual = toTest.getReadinessLevel();
        assertEquals(actual, expected);
    }

    // Node's shouldn't be displayed
//    @Test
    public void testReadinessLevel_Node() throws Exception {
        LocationLevel expected = LocationLevel.NODE;
        Location.Builder builder = Location.Builder.builder()
                .withNodeId(toTest.getNodeId())
                .withPoint(toTest.getPoint());

        toTest = builder.build();
        LocationLevel actual = toTest.getReadinessLevel();
        assertEquals(actual, expected);
    }

    @Test
    public void testReadinessLevel_Draft_onName() throws Exception {
        LocationLevel expected = LocationLevel.DRAFT;
        Location.Builder builder = Location.Builder.builder()
                .withName(toTest.getName())
                .withNodeId(toTest.getNodeId())
                .withPoint(toTest.getPoint());

        toTest = builder.build();
        LocationLevel actual = toTest.getReadinessLevel();
        assertEquals(actual, expected);
    }

    @Test
    public void testReadinessLevel_Draft_onDescription() throws Exception {
        LocationLevel expected = LocationLevel.DRAFT;
        Location.Builder builder = Location.Builder.builder()
                .withDescription(toTest.getDescription())
                .withNodeId(toTest.getNodeId())
                .withPoint(toTest.getPoint());

        toTest = builder.build();
        LocationLevel actual = toTest.getReadinessLevel();
        assertEquals(actual, expected);
    }

    @Test
    public void testReadinessLevel_Draft_onFeaturedImage() throws Exception {
        LocationLevel expected = LocationLevel.DRAFT;
        Location.Builder builder = Location.Builder.builder()
                .withFeaturedImage(toTest.getFeaturedImage())
                .withNodeId(toTest.getNodeId())
                .withPoint(toTest.getPoint());

        toTest = builder.build();
        LocationLevel actual = toTest.getReadinessLevel();
        assertEquals(actual, expected);
    }

    @Test
    public void testReadinessLevel_Draft_onType() throws Exception {
        LocationLevel expected = LocationLevel.DRAFT;
        Location.Builder builder = Location.Builder.builder()
                .withLocationType(toTest.getLocationType())
                .withNodeId(toTest.getNodeId())
                .withPoint(toTest.getPoint());

        toTest = builder.build();
        LocationLevel actual = toTest.getReadinessLevel();
        assertEquals(actual, expected);
    }

    @Test
    public void testReadinessLevel_Place_missingClues() throws Exception {
        LocationLevel expected = LocationLevel.PLACE;

        Location.Builder builder = Location.Builder.from(toTest);
        builder.withClueIds(Collections.<Integer>emptyList());
        toTest = builder.build();

        LocationLevel actual = toTest.getReadinessLevel();
        assertEquals(actual, expected);
    }

    @Test
    public void testReadinessLevel_Featured() throws Exception {
        LocationLevel expected = LocationLevel.FEATURED;

        Location.Builder builder = Location.Builder.from(toTest);
        builder.withGooglePlaceId(1);
        toTest = builder.build();

        LocationLevel actual = toTest.getReadinessLevel();
        assertEquals(actual, expected);
    }
}