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
 * Created by jett on 12/5/15.
 */
package com.clueride.dao;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.Node;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the DefaultNodeStoreTest class.
 */
public class DefaultNodeStoreTest {
    private Injector injector;

    @BeforeMethod
    public void setUp() {
       injector = Guice.createInjector(new AbstractModule() {
           @Override
           protected void configure() {
              bind(NodeStore.class).to(DefaultNodeStore.class);
           }
       });
    }

    @Test
    public void testGetNodeById() throws Exception {
        NodeStore nodeStore = injector.getInstance(NodeStore.class);
        assertNotNull(nodeStore);
        Integer id = 5;
        assertNotNull(nodeStore.getNodeById(id));
        assertNotNull(nodeStore.getNodeById(id).getId());
        assertEquals(id, nodeStore.getNodeById(id).getId());
    }

    @Test
    public void testGetNodes() {
        NodeStore nodeStore = injector.getInstance(NodeStore.class);
        assertNotNull(nodeStore);
        Collection<GeoNode> nodes = nodeStore.getNodes();
        assertNotNull(nodes);
        System.out.println("Dumping list of current Nodes:");
        for (Node node : nodes) {
            System.out.println(node);
        }
    }
}