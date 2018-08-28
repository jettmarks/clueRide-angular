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
 * Created by jett on 8/4/18.
 */

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Provider;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.domain.DomainGuiceModuleTest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the BadgeTest class.
 *
 * Of particular interest are the methods translating from String to objects.
 */
@Guice(modules = DomainGuiceModuleTest.class)
public class BadgeTest {
    private Badge toTest;
    private Badge.Builder builder;

    @Inject
    private Provider<Badge.Builder> toTestProvider;

    @BeforeMethod
    public void setUp() throws Exception {
        builder = toTestProvider.get();

        toTest = builder.build();
        assertNotNull(toTest);
        assertNotNull(toTest.getId());
        // TODO: CA-359
//        assertNotNull(toTest.getBadgeType());
        assertNotNull(toTest.getBadgeImageUrl());
        assertNotNull(toTest.getBadgeCriteriaUrl());
    }

    @Test
    public void testGetId() throws Exception {
        Integer expected = builder.getId();
        Integer actual = toTest.getId();
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testGetId_missing_all() throws Exception {
        builder = Badge.Builder.builder();
        toTest = builder.build();
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testGetId_missing_id() throws Exception {
        Badge.Builder toTestBuilder = Badge.Builder.builder()
                .withBadgeType(builder.getBadgeType())
                .withCriteriaUrl(builder.getCriteriaUrl())
                .withImageUrl(builder.getImageUrl());
        toTest = toTestBuilder.build();
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testGetId_missing_badgeType() throws Exception {
        Badge.Builder toTestBuilder = Badge.Builder.builder()
                .withId(builder.getId())
                .withCriteriaUrl(builder.getCriteriaUrl())
                .withImageUrl(builder.getImageUrl());
        toTest = toTestBuilder.build();
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testGetId_missing_imageUrl() throws Exception {
        Badge.Builder toTestBuilder = Badge.Builder.builder()
                .withId(builder.getId())
                .withBadgeType(builder.getBadgeType())
                .withCriteriaUrl(builder.getCriteriaUrl());
        toTest = toTestBuilder.build();
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testGetId_missing_criteriaUrl() throws Exception {
        Badge.Builder toTestBuilder = Badge.Builder.builder()
                .withId(builder.getId())
                .withBadgeType(builder.getBadgeType())
                .withImageUrl(builder.getImageUrl());
        toTest = toTestBuilder.build();
    }

    // TODO: CA-359
//    @Test
//    public void testGetBadgeType_OK() throws Exception {
//        BadgeType expected = BadgeType.GUIDE;
//        toTest = builder.withBadgeTypeString(expected.toString()).build();
//        BadgeType actual = toTest.getBadgeType();
//        assertEquals(actual, expected);
//    }

//    @Test(expectedExceptions = IllegalArgumentException.class)
//    public void testGetBadgeType_unrecognizedType() throws Exception {
//        toTest = builder.withBadgeTypeString("not a real badge").build();
//    }

    @Test
    public void testGetBadgeImageUrl_OK() throws Exception {
        URL expected = builder.getImageUrl();
        toTest = builder.withImageUrlString(expected.toString()).build();
        URL actual = toTest.getBadgeImageUrl();
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = MalformedURLException.class)
    public void testGetBadgeImageUrl_badUrl() throws Exception {
        toTest = builder.withImageUrlString("not a URL").build();
    }

    @Test
    public void testGetBadgeCriteriaUrl_OK() throws Exception {
        URL expected = builder.getCriteriaUrl();
        toTest = builder.withCriteriaUrlString(expected.toString()).build();
        URL actual = toTest.getBadgeCriteriaUrl();
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = MalformedURLException.class)
    public void testGetBadgeCriteriaUrl_badUrl() throws Exception {
        toTest = builder.withCriteriaUrlString("not a URL").build();
    }

}