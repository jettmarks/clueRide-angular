package com.clueride.domain.user.place;/*
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
 * Created by jett on 10/8/17.
 */

import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Provider;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.domain.DomainGuiceModuleTest;
import com.clueride.domain.user.ReadinessLevel;
import com.clueride.domain.user.location.Location;
import com.clueride.domain.user.loctype.LocationType;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the ScoredLocationServiceImplTest class.
 */
@Guice(modules= DomainGuiceModuleTest.class)
public class ScoredLocationServiceImplTest {
    private ScoredLocationServiceImpl toTest;

    @Inject
    private Location.Builder locationBuilder;

    @Inject
    private Provider<ScoredLocationServiceImpl> toTestProvider;

    @Inject
    private Provider<LocationType> locationTypeProvider;

    @BeforeMethod
    public void setUp() throws Exception {
        assertNotNull(toTestProvider);
        assertNotNull(locationTypeProvider);

        toTest = toTestProvider.get();
        assertNotNull(toTest);
    }

    @Test
    public void testCalculateReadinessLevel() throws Exception {
    }

    @Test
    public void testReadinessLevel_Attraction() throws Exception {
        ReadinessLevel expected = ReadinessLevel.ATTRACTION;
        ReadinessLevel actual = toTest.calculateReadinessLevel(locationBuilder);
        assertEquals(actual, expected);
    }

    // Node's shouldn't be displayed
    @Test
    public void testReadinessLevel_Node() throws Exception {
        /* setup test */
        ReadinessLevel expected = ReadinessLevel.NODE;
        LocationType locationTypeForNode = LocationType.Builder.from(locationTypeProvider.get())
                .withId(0)
                .build();
        Location.Builder builder = Location.Builder.builder()
                .withNodeId(locationBuilder.getNodeId())
                .withLatLon(locationBuilder.getLatLon())
                .withLocationType(locationTypeForNode);

        /* make call */
        ReadinessLevel actual = toTest.calculateReadinessLevel(builder);

        /* verify results */
        assertEquals(actual, expected);
    }

    @Test
    public void testReadinessLevel_Draft_onName() throws Exception {
        ReadinessLevel expected = ReadinessLevel.DRAFT;
        Location.Builder builder = Location.Builder.builder()
                .withLocationType(locationBuilder.getLocationType())
                .withName(locationBuilder.getName())
                .withNodeId(locationBuilder.getNodeId())
                .withLatLon(locationBuilder.getLatLon());

        ReadinessLevel actual = toTest.calculateReadinessLevel(builder);
        assertEquals(actual, expected);
    }

    @Test
    public void testReadinessLevel_Draft_onDescription() throws Exception {
        ReadinessLevel expected = ReadinessLevel.DRAFT;
        Location.Builder builder = Location.Builder.builder()
                .withLocationType(locationBuilder.getLocationType())
                .withDescription(locationBuilder.getDescription())
                .withNodeId(locationBuilder.getNodeId())
                .withLatLon(locationBuilder.getLatLon());

        ReadinessLevel actual = toTest.calculateReadinessLevel(builder);
        assertEquals(actual, expected);
    }

    @Test
    public void testReadinessLevel_Draft_onFeaturedImage() throws Exception {
        ReadinessLevel expected = ReadinessLevel.DRAFT;
        Location.Builder builder = Location.Builder.builder()
                .withLocationType(locationBuilder.getLocationType())
                .withFeaturedImage(locationBuilder.getFeaturedImage())
                .withNodeId(locationBuilder.getNodeId())
                .withLatLon(locationBuilder.getLatLon());

        ReadinessLevel actual = toTest.calculateReadinessLevel(builder);
        assertEquals(actual, expected);
    }

    @Test
    public void testReadinessLevel_Draft_onType() throws Exception {
        ReadinessLevel expected = ReadinessLevel.DRAFT;
        Location.Builder builder = Location.Builder.builder()
                .withLocationType(locationBuilder.getLocationType())
                .withNodeId(locationBuilder.getNodeId())
                .withLatLon(locationBuilder.getLatLon());

        ReadinessLevel actual = toTest.calculateReadinessLevel(builder);
        assertEquals(actual, expected);
    }

    @Test
    public void testReadinessLevel_Place_missingClues() throws Exception {
        ReadinessLevel expected = ReadinessLevel.PLACE;

        Location.Builder builder = Location.Builder.from(locationBuilder.build());
        builder.withClueIds(Collections.<Integer>emptyList());

        ReadinessLevel actual = toTest.calculateReadinessLevel(builder);
        assertEquals(actual, expected);
    }

    @Test
    public void testReadinessLevel_Featured() throws Exception {
        ReadinessLevel expected = ReadinessLevel.FEATURED;

        Location.Builder builder = Location.Builder.from(locationBuilder.build());
        builder.withGooglePlaceId(1);

        ReadinessLevel actual = toTest.calculateReadinessLevel(builder);
        assertEquals(actual, expected);
    }

    @Test (expectedExceptions = NullPointerException.class)
    public void testReadinessLevel_nullClues() throws Exception {
        ReadinessLevel expected = ReadinessLevel.PLACE;

        Location.Builder builder = Location.Builder.from(locationBuilder.build());
        builder.withClueIds(null);

        ReadinessLevel actual = toTest.calculateReadinessLevel(builder);
        assertEquals(actual, expected);
    }

    @Test
    public void testReadinessLevel_proposed() throws Exception {
        ReadinessLevel expected = ReadinessLevel.DRAFT;

        Location.Builder builder = Location.Builder.builder();
        builder.withLatLon(locationBuilder.getLatLon());
        builder.withLocationType(locationBuilder.getLocationType());

        ReadinessLevel actual = toTest.calculateReadinessLevel(builder);
        assertEquals(actual, expected);
    }

}