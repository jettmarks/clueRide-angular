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

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.dao.JsonNetworkStore;
import com.clueride.dao.JsonNodeStore;
import com.clueride.dao.NetworkStore;
import com.clueride.dao.NodeStore;
import com.clueride.service.IdProvider;
import com.clueride.service.MemoryBasedPathIdProvider;
import com.clueride.service.NetworkEval;
import com.clueride.service.NetworkEvalImpl;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

/**
 * Exercises the GamePath class.
 */
public class GamePathTest {
    private GamePath toTest;
    private IdProvider idProvider = new MemoryBasedPathIdProvider();
    private Injector injector;
    private NetworkStore networkStore = JsonNetworkStore.getInstance();
    private NetworkEval networkEval;

    private Integer startNodeId = 30;
    private Integer endNodeId = 8;


    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(NodeStore.class).to(JsonNodeStore.class);
                bind(NetworkStore.class).toInstance(networkStore);
                bind(NetworkEval.class).to(NetworkEvalImpl.class);
            }
        });
        networkEval = injector.getInstance(NetworkEval.class);

        toTest = GamePath.Builder.getBuilder(networkEval)
                .withStartNodeId(startNodeId)
                .withEndNodeId(endNodeId)
                .addEdgeId(39)
                .addEdgeId(43)
                .addEdgeId(79)
                .addEdgeId(78)
                .build();
    }

    @Test
    public void testGetId() throws Exception {
        Integer expected = idProvider.getLastId();
        Integer actual = toTest.getId();
        assertEquals(actual, expected, "ID should have been assigned");
    }

    @Test
    public void testGetStartNodeId() throws Exception {
        Integer expected = startNodeId;
        Integer actual = toTest.getStartNodeId();
        assertEquals(actual, expected, "ID should match");
    }

    @Test
    public void testGetEndNodeId() throws Exception {
        Integer expected = endNodeId;
        Integer actual = toTest.getEndNodeId();
        assertEquals(actual, expected, "ID should match");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testInvalidEdgeSequence() {
        toTest = GamePath.Builder.getBuilder(networkEval)
                .withStartNodeId(startNodeId)
                .withEndNodeId(endNodeId)
                .addEdgeId(79)
                .build();
    }
}