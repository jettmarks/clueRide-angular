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
 * Created by jett on 12/22/15.
 */
package com.clueride.service;

import javax.inject.Provider;

import com.google.inject.Inject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.CoreGuiceModuleTest;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Exercises the DefaultNodeServiceTest class.
 */
@Guice(modules = CoreGuiceModuleTest.class)
public class DefaultNodeServiceTest {
    private NodeService toTest;

    @Inject
    private Provider<NodeService> toTestProvider;

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
        toTest = toTestProvider.get();
    }

    @Test
    public void testGetPointByNodeId() throws Exception {

    }

    @Test
    public void testAddNewNode() throws Exception {
        Double lat = 33.0;
        Double lon = -84.0;
        String actual = toTest.addNewNode(lat, lon);
        System.out.println(actual);
    }

    @Test
    public void testConfirmNewNode() throws Exception {

    }

    @Test
    public void testGetNodeGroups() throws Exception {

    }

    @Test
    public void testSetNodeGroup() throws Exception {

    }

    @Test
    public void testShowAllNodes() throws Exception {

    }
}