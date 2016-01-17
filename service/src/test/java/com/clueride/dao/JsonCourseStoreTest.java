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
 * Created by jett on 1/17/16.
 */
package com.clueride.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.domain.Course;
import com.clueride.domain.GameCourse;
import com.clueride.service.NetworkEval;
import com.clueride.service.NetworkEvalImpl;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the JsonCourseStoreTest class.
 */
public class JsonCourseStoreTest {
    /** Instance under test. */
    private CourseStore toTest;

    private GameCourse.Builder builder;
    private Injector injector;

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(NodeStore.class).to(DefaultNodeStore.class);
                bind(NetworkStore.class).to(DefaultNetworkStore.class);
                bind(NetworkEval.class).to(NetworkEvalImpl.class);
                bind(CourseStore.class).to(JsonCourseStore.class);
            }
        });
//        networkEval = injector.getInstance(NetworkEval.class);

        builder = getBuilder();

    }

    private GameCourse.Builder getBuilder() {
        return GameCourse.Builder.getBuilder()
                .withName("Five Free Things in Atlanta")
                .withDescription("From Washington Post article in 2014")
                .addPathId(2)
                .addPathId(3)
                .addPathId(4)
                .addPathId(5)
                .addPathId(6);
    }

    @Test
    public void testAddNew() throws Exception {
        Course course = builder.build();
        toTest = injector.getInstance(CourseStore.class);
        assertNotNull(toTest);

        Integer id = toTest.addNew(course);
        assertNotNull(id);

        System.out.println("Course ID: " + id);
        ObjectMapper objectMapper = new ObjectMapper();
        String asJson = objectMapper.writeValueAsString(course);
        System.out.println("As JSON: ");
        System.out.println(asJson);
    }

    @Test
    public void testGetCourseById() throws Exception {
        toTest = injector.getInstance(CourseStore.class);
        int expected = 5;

        Course actual = toTest.getCourseById(2);
        assertNotNull(actual);
        assertEquals(actual.getPathIds().size(),expected, "Right number of Paths");
    }

    @Test
    public void testUpdate() throws Exception {

    }
}