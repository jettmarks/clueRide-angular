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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.domain.GamePath;
import com.clueride.domain.user.path.Path;
import com.clueride.io.PojoJsonService;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the JsonPathStoreTest class.
 */
@Guice(modules=ServiceGuiceModuleTest.class)
public class JsonPathStoreTest {
    /** Instance under test. */
    private PathStore toTest;

    private GamePath.Builder gamePathBuilder;

    @Inject
    private PojoJsonService pojoJsonService;

    @Inject
    private Provider<JsonPathStore> toTestProvider;

    @Inject
    private Provider<GamePath.Builder> gamePathBuilderProvider;

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
        reset(pojoJsonService);
        gamePathBuilder = gamePathBuilderProvider.get();

        /* setup data */
        List<Path> paths = new ArrayList<>();
        paths.add(gamePathBuilder.build());

        /* train mocks */
        when(pojoJsonService.loadPaths()).thenReturn(paths);

        /* TODO: Would prefer populating outside the constructor so we can get an instance and then train the mock. */
        toTest = toTestProvider.get();
        assertNotNull(toTest);
    }

    @Test
    public void testAddNew() throws Exception {
        Path path = gamePathBuilder.build();
        toTest = toTestProvider.get();

        Integer id = toTest.addNew(path);
        assertNotNull(id);

        System.out.println("Path ID: " + id);
        ObjectMapper objectMapper = new ObjectMapper();
        String asJson = objectMapper.writeValueAsString(path);
        System.out.println("As JSON: ");
        System.out.println(asJson);
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

}