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

import javax.inject.Provider;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.CoreGuiceModuleTest;
import com.clueride.domain.factory.PointFactory;
import com.clueride.domain.user.latlon.LatLon;
import com.clueride.domain.user.latlon.LatLonService;
import com.clueride.domain.user.location.Location;
import com.clueride.domain.user.location.LocationStore;
import com.clueride.domain.user.loctype.LocationType;
import com.clueride.domain.user.loctype.LocationTypeService;
import com.clueride.infrastructure.Jpa;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Exercises the DefaultLocationServiceTest class.
 */
@Guice(modules=CoreGuiceModuleTest.class)
public class DefaultLocationServiceTest {
    private static final Logger LOGGER = Logger.getLogger(DefaultLocationServiceTest.class);
    private DefaultLocationService toTest;

    @Inject
    private Location location;

    @Inject
    private @Jpa LocationStore locationStore;

    @Inject
    private NodeService nodeService;

    @Inject
    private LatLonService latLonService;

    @Inject
    private LocationTypeService locationTypeService;

    @Inject
    private Provider<DefaultLocationService> toTestProvider;

    @Inject
    private Provider<LocationType> locationTypeProvider;

    @BeforeMethod
    public void setup() {
        initMocks(this);
        reset(
                locationStore,
                nodeService
        );
        toTest = toTestProvider.get();
    }

    @Test
    public void testGetLocation() throws Exception {
        Location expected = location;
        /* train mocks */
        Integer locationId = location.getId();
        when(locationStore.getLocationById(locationId)).thenReturn(expected);

        /* make call */
        String actual = toTest.getLocation(locationId);

        /* verify results */
        assertNotNull(actual);
        assertTrue(!actual.isEmpty());
        LOGGER.debug(actual);
    }

    /**
     * For this test, the location IDs match the Node IDs.
     * @throws Exception catchall
     */
    @Test
    public void testGetNearestMarkerLocations() throws Exception {

        List<URL> imageList = new ArrayList<>();
        imageList.add(new URL("http://localhost:8080/"));
        List<Integer> clueList = new ArrayList<>();
        clueList.add(1);
        clueList.add(2);

        // Prepare fake locations
        List<Location.Builder> locationList = new ArrayList<>();
        Location.Builder builder = Location.Builder.builder();
        for (Integer id = 1; id<=6; id++) {
            LocationType.Builder locationTypeBuilderFromService =
                    LocationType.Builder.from(locationTypeProvider.get())
                    .withId(id);
            LocationType locationTypeToMatch = locationTypeBuilderFromService.build();
            when(locationTypeService.getById(id)).thenReturn(locationTypeToMatch);
            when(nodeService.getPointByNodeId(id)).thenReturn(PointFactory.getJtsInstance(10.0, 11.0, 0.0));

            locationList.add(
                    builder.withId(id)
                            .withNodeId(id)
                            .withName("Test Loc " + id)
                            .withDescription("Description for Loc " + id)
                            .withLocationTypeId(id)
                            .withImageUrls(imageList)
                            .withClueIds(clueList)
            );
        }
        when(locationStore.getLocationBuilders()).thenReturn(locationList);

        assertNotNull(toTest);

        String actual = toTest.getNearestMarkerLocations(-10.0, 12.7);
        LOGGER.debug(actual);
    }

    @Test
    public void testProposeLocation() throws Exception {
        /* setup data */
        double lat = 33.77;
        double lon = -84.37;
        Integer newLatLonId = 19;
        LatLon latLonWithoutId = new LatLon(lat, lon);
        LatLon latLon = new LatLon(lat, lon).setId(newLatLonId);
        LocationType expectedLocationType = locationTypeProvider.get();
        Location expected = Location.Builder.builder()
                .withLocationType(expectedLocationType)
                .withLatLon(latLon)
                .build();

        /* train mocks */
        when(latLonService.addNew(latLonWithoutId)).thenReturn(latLon);
        when(locationStore.addNew(any(Location.Builder.class))).thenReturn(newLatLonId);
        when(locationStore.getLocationById(newLatLonId)).thenReturn(expected);
        when(locationTypeService.getByName(expected.getLocationTypeName()))
                .thenReturn(expectedLocationType);

        /* make call */
        Location actual;
        actual = toTest.proposeLocation(latLon, locationTypeProvider.get().getName());

        /* verify results */
        assertNotNull(actual);
        assertEquals(actual, expected);
    }

    @Test
    public void testDecodeBase64() {
//        String expected = "Test String to be encoded";
//
//        String encoded = "data:image/jpeg;base64," + BaseEncoding.base64().encode(expected);
//        String actual = toTest.decodeBase64(encoded);
//        assertEquals(actual, expected);
    }
}