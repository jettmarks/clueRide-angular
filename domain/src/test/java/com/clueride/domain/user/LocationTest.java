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
import java.util.List;

import javax.inject.Provider;

import com.google.inject.Inject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.domain.DomainGuiceModuleTest;
import com.clueride.domain.user.location.Location;
import com.clueride.domain.user.location.LocationType;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Guice(modules = DomainGuiceModuleTest.class)
public class LocationTest {

    @Inject
    private Provider<Location.Builder> toTestProvider;

    Location toTest;
    Location.Builder builder;

    // Test values
    String expectedName;
    String expectedDescription;
    LocationType expectedLocationType;
    Integer expectedNodeId;

    @BeforeMethod
    public void setUp() throws Exception {
        builder = toTestProvider.get();

        toTest = builder.build();
        expectedName = toTest.getName();
        expectedDescription = toTest.getDescription();
        expectedLocationType = toTest.getLocationType();
        expectedNodeId = toTest.getNodeId();
        assertNotNull(toTest);
    }

    @Test
    public void testAddDeleteClues() throws Exception {
        List<Integer> expectedCluesAfterDelete = toTest.getClueIds();
        List<Integer> expectedCluesAfterAdd = new ArrayList<>(expectedCluesAfterDelete);
        expectedCluesAfterAdd.add(10);

        builder = Location.Builder.from(toTest);
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
    @Test
    public void testReadinessLevel_Node() throws Exception {
        LocationLevel expected = LocationLevel.NODE;
        Location.Builder builder = Location.Builder.builder()
                .withNodeId(toTest.getNodeId())
                .withLatLon(toTest.getLatLon());

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
                .withLatLon(toTest.getLatLon());

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
                .withLatLon(toTest.getLatLon());

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
                .withLatLon(toTest.getLatLon());

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
                .withLatLon(toTest.getLatLon());

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

    @Test
    public void testReadinessLevel_nullClues() throws Exception {
        LocationLevel expected = LocationLevel.PLACE;

        Location.Builder builder = Location.Builder.from(toTest);
        builder.withClueIds(null);
        toTest = builder.build();

        LocationLevel actual = toTest.getReadinessLevel();
        assertEquals(actual, expected);
    }

    @Test
    public void testReadinessLevel_proposed() throws Exception {
        LocationLevel expected = LocationLevel.DRAFT;

        Location.Builder builder = Location.Builder.builder();
        builder.withLatLon(toTest.getLatLon());
        builder.withLocationType(toTest.getLocationType());
        toTest = builder.build();

        LocationLevel actual = toTest.getReadinessLevel();
        assertEquals(actual, expected);
    }
}