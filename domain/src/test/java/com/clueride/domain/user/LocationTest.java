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
 * Created by jett on 11/29/15.
 */
package com.clueride.domain.user;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Provider;

import com.google.inject.Inject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.domain.DomainGuiceModuleTest;
import com.clueride.domain.user.location.Location;
import com.clueride.domain.user.loctype.LocationType;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Guice(modules = DomainGuiceModuleTest.class)
public class LocationTest {

    private Location toTest;
    private Location.Builder builder;

    @Inject
    private Provider<Location.Builder> toTestProvider;

    // Test values
    private LocationType expectedLocationType;

    @BeforeMethod
    public void setUp() throws Exception {
        builder = toTestProvider.get();

        toTest = builder.build();
        expectedLocationType = toTest.getLocationType();
        assertNotNull(toTest);
        assertNotNull(expectedLocationType);
    }

    @Test
    public void testAddDeleteClues() throws Exception {
        List<Integer> expectedCluesAfterDelete = toTest.getClueIds();
        List<Integer> expectedCluesAfterAdd = new ArrayList<>(expectedCluesAfterDelete);
        expectedCluesAfterAdd.add(10);

        builder = Location.Builder.from(toTest);
        toTest = builder.withClueIds(expectedCluesAfterAdd).build();
        List<Integer> actualAfterAdd = toTest.getClueIds();
        assertEquals(actualAfterAdd, expectedCluesAfterAdd);

        toTest.removeClue(10);
        List<Integer> actualAfterDelete = toTest.getClueIds();
        assertEquals(actualAfterDelete, expectedCluesAfterDelete);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testBuilder_missingLocationType() throws Exception {
        builder = Location.Builder.builder();
        builder.build();
    }

}