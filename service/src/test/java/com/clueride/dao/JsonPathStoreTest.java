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

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.domain.GamePath;
import com.clueride.domain.user.Path;
import com.clueride.service.NetworkEval;
import com.clueride.service.NetworkEvalImpl;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the JsonPathStoreTest class.
 */
public class JsonPathStoreTest {
    /** Instance under test. */
    private PathStore toTest;

    private GamePath.Builder builder;
    private Injector injector;
    private NetworkEval networkEval;

    private Integer startNodeId = 30;
    private Integer endNodeId = 8;

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(NodeStore.class).to(DefaultNodeStore.class);
                bind(NetworkStore.class).to(DefaultNetworkStore.class);
                bind(NetworkEval.class).to(NetworkEvalImpl.class);
                bind(PathStore.class).to(JsonPathStore.class);
            }
        });
        networkEval = injector.getInstance(NetworkEval.class);

        builder = getBuilder();
    }

    @Test
    public void testAddNew() throws Exception {
        Path path = builder.build();
        toTest = injector.getInstance(PathStore.class);
        assertNotNull(toTest);

        Integer id = toTest.addNew(path);
        assertNotNull(id);

        System.out.println("Path ID: " + id);
        ObjectMapper objectMapper = new ObjectMapper();
        String asJson = objectMapper.writeValueAsString(path);
        System.out.println("As JSON: ");
        System.out.println(asJson);
    }

    @Test
    public void testLoadAll() throws Exception {
        int expected = 5;
        toTest = injector.getInstance(PathStore.class);
        assertNotNull(toTest);

        Path actual = toTest.getPathById(2);
        assertNotNull(actual);
        assertEquals(actual.getEdgeIds().size(), expected, "Right number of Edges");
    }

    @Test
    public void testGetPathById() throws Exception {

    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testPersistPath() throws Exception {

    }

    private GamePath.Builder getBuilder() {
        return GamePath.Builder.getBuilder(networkEval)
                .withStartNodeId(startNodeId)
                .withEndNodeId(endNodeId)
                .addEdgeId(39)
                .addEdgeId(43)
                .addEdgeId(79)
                .addEdgeId(78)
                .addEdgeId(87);
    }
}