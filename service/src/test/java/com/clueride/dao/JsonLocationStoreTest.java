/*
 * Copyright 2015 Jett Marks
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Created by jett on 11/24/15.
 */
package com.clueride.dao;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Provider;

import com.google.inject.Inject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.domain.dev.Node;
import com.clueride.domain.user.location.Location;
import com.clueride.domain.user.loctype.LocationType;
import com.clueride.io.PojoJsonService;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises an injected instance of JsonLocationStore.
 */
@Guice(modules=ServiceGuiceModuleTest.class)
public class JsonLocationStoreTest {
    private JsonLocationStore toTest;

    @Inject
    private Location.Builder locationBuilder;

    @Inject
    private Provider<LocationType> locationTypeProvider;

    @Inject
    private Provider<JsonLocationStore> toTestProvider;

    @Inject
    private PojoJsonService pojoJsonService;

    @Inject
    private NodeStore nodeStore;

    @Inject
    private Provider<Node> nodeProvider;

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        List<Location> locations = new ArrayList<>();
        Location location = locationBuilder.build();
        locations.add(location);
        when(pojoJsonService.loadLocations()).thenReturn(locations);
        when(nodeStore.getNodeById(location.getNodeId())).thenReturn(nodeProvider.get());
        toTest = toTestProvider.get();
        assertNotNull(toTest);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testAddNewBadNodeId() throws Exception {
        // Negative is allowed, but won't be found
        locationBuilder.withNodeId(-1);
        Location location = locationBuilder.build();

        Integer id = toTest.addNew(location);
        assertNotNull(id);
    }

    @Test
    public void testGetLocations() throws Exception {
        Collection<Location> locations = toTest.getLocations();
        assertNotNull(locations);
    }

}