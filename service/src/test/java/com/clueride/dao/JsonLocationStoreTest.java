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

import com.clueride.domain.user.Clue;
import com.clueride.domain.user.Location;
import com.clueride.domain.user.LocationType;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises an injected instance of JsonLocationStore.
 */
public class JsonLocationStoreTest {
    private Location.Builder builder;
    private Injector injector;

    @Inject
    private LocationStore locationStore;

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(NodeStore.class).to(DefaultNodeStore.class);
                bind(LocationStore.class).to(JsonLocationStore.class);
            }
        });
        builder = getLocationBuilder();
    }

    @Test
    public void testAddNew() throws Exception {
        Location location = builder.build();
        LocationStore locationStore = injector.getInstance(LocationStore.class);
        assertNotNull(locationStore);

        Integer id = locationStore.addNew(location);
        assertNotNull(id);

        System.out.println("Location ID: " + id);
        ObjectMapper objectMapper = new ObjectMapper();
        String locationAsJson = objectMapper.writeValueAsString(location);
        System.out.println("As JSON: ");
        System.out.println(locationAsJson);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testAddNewBadNodeId() throws Exception {
        // Negative is allowed, but won't be found
        builder.setNodeId(-1);
        Location location = builder.build();
        LocationStore locationStore = injector.getInstance(LocationStore.class);

        Integer id = locationStore.addNew(location);
        assertNotNull(id);
    }

    @Test
    public void testGetLocationById() throws Exception {
        LocationStore locationStore = injector.getInstance(LocationStore.class);
        assertNotNull(locationStore);

        Integer id = 1;
        Location location = locationStore.getLocationById(id);
        assertNotNull(location);
        assertEquals(location.getId(), id);
    }

    @Test
    public void testGetLocations() throws Exception {
        Collection<Location> locations = injector.getInstance(LocationStore.class).getLocations();
        assertNotNull(locations);
    }

    private Location.Builder getLocationBuilder() throws MalformedURLException {
        // Test values
        String expectedName = "Test Location";
        String expectedDescription = "Here's a nice spot to spread out the blanket or toss the frisbee.";
        LocationType expectedLocationType = LocationType.PICNIC;
        Integer expectedNodeId = 5;
        List<Clue> expectedClues = new ArrayList<>();
        List<URL> expectedImageUrls = new ArrayList<>();
        expectedImageUrls.add(new URL("https://clueride.com/"));
        expectedClues.add(Clue.Builder.builder().build());

        builder = Location.Builder.builder()
                .setName(expectedName)
                .setDescription(expectedDescription)
                .setLocationType(expectedLocationType)
                .setNodeId(expectedNodeId)
                .setClues(expectedClues)
                .setImageUrls(expectedImageUrls);
        return builder;
    }
}