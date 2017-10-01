package com.clueride.service;/*
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
 * Created by jett on 9/14/17.
 */

import javax.inject.Provider;

import com.google.inject.Inject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.CoreGuiceModuleTest;
import static org.testng.Assert.assertNotNull;

/**
 * TODO: Exercises the ClueServiceImplTest class.
 */
@Guice(modules= CoreGuiceModuleTest.class)
public class ClueServiceImplTest {
    private ClueServiceImpl toTest;

    @Inject
    private Provider<ClueServiceImpl> toTestProvider;

    @BeforeMethod
    public void setUp() throws Exception {
        toTest = toTestProvider.get();
        assertNotNull(toTest);
    }

    @Test
    public void testGetCluesPerLocation() throws Exception {
    }

    @Test
    public void testGetClue() throws Exception {
    }

    @Test
    public void testRemoveClueFromLocation() throws Exception {
    }

    @Test
    public void testAddClue() throws Exception {
    }

    @Test
    public void testUpdate() throws Exception {
    }

}