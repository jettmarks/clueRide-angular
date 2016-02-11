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
 * Created by jett on 12/13/15.
 */
package com.clueride.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.dao.CourseStore;
import com.clueride.dao.ImageStore;
import com.clueride.dao.JsonLocationStore;
import com.clueride.dao.JsonNodeStore;
import com.clueride.dao.LocationStore;
import com.clueride.dao.NodeStore;
import com.clueride.dao.PathStore;
import com.clueride.domain.factory.PointFactory;
import com.clueride.domain.user.Location;
import com.clueride.domain.user.LocationType;
import com.clueride.service.builder.LocationBuilder;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Exercises the DefaultLocationServiceTest class.
 */
public class DefaultLocationServiceTest {
    private static final Logger LOGGER = Logger.getLogger(DefaultLocationServiceTest.class);

    @Mock
    private NodeService nodeService;

    @Mock
    private LocationStore locationStore;

    @Inject
    private ImageStore imageStore;

    @Inject
    private LocationBuilder locationBuilder;

    @Inject
    private CourseStore courseStore;

    @Inject
    private PathStore pathStore;

    private LocationService toTest;

    @BeforeMethod
    public void setup() {
        initMocks(this);
    }

    @Test
    public void testGetLocation() throws Exception {
        // Actual instances for this test
        NodeStore nodeStore = new JsonNodeStore();
        locationStore = new JsonLocationStore(nodeStore);
        toTest = new DefaultLocationService(
                locationStore,
                imageStore,
                nodeService,
                courseStore,
                pathStore,
                locationBuilder);
        assertNotNull(toTest);

        Integer locationId = 1;
        String actual = toTest.getLocation(locationId);
        assertNotNull(actual);
        assertTrue(!actual.isEmpty());
        LOGGER.debug(actual);
    }

    /**
     * For this test, the location IDs match the Node IDs.
     * @throws Exception
     */
    @Test
    public void testGetNearestLocations() throws Exception {
        // Prepare Node Mocking (with fake lat/lon pairs)
        when(nodeService.getPointByNodeId(1)).thenReturn(PointFactory.getJtsInstance(10.0, 11.0, 0.0));
        when(nodeService.getPointByNodeId(2)).thenReturn(PointFactory.getJtsInstance(10.0, 12.0, 0.0));
        when(nodeService.getPointByNodeId(3)).thenReturn(PointFactory.getJtsInstance(10.0, 13.0, 0.0));
        when(nodeService.getPointByNodeId(4)).thenReturn(PointFactory.getJtsInstance(10.0, 14.0, 0.0));
        when(nodeService.getPointByNodeId(5)).thenReturn(PointFactory.getJtsInstance(10.0, 15.0, 0.0));
        when(nodeService.getPointByNodeId(6)).thenReturn(PointFactory.getJtsInstance(10.0, 16.0, 0.0));

        List<URL> imageList = new ArrayList<>();
        imageList.add(new URL("http://localhost:8080/"));
        List<Integer> clueList = new ArrayList<>();
        clueList.add(1);
        clueList.add(2);

        // Prepare fake locations
        List<Location> locationList = new ArrayList<>();
        Location.Builder builder = Location.Builder.builder();
        for (Integer id = 1; id<7; id++) {
            locationList.add(
                    builder.setId(id)
                            .setNodeId(id)
                            .setName("Test Loc " + id)
                            .setDescription("Description for Loc " + id)
                            .setLocationType(LocationType.BAR)
                            .setImageUrls(imageList)
                            .setClueIds(clueList)
                            .build());
        }
        when(locationStore.getLocations()).thenReturn(locationList);

        toTest = new DefaultLocationService(
                locationStore,
                imageStore,
                nodeService,
                courseStore,
                pathStore,
                locationBuilder);
        assertNotNull(toTest);

        String actual = toTest.getNearestLocations(-10.0, 12.7);
        LOGGER.debug(actual);
    }
}