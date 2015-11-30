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

import com.clueride.domain.user.Location;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * TODO: Description.
 */
public class JsonLocationStoreTest {

    @Test
    public void testAddNew() throws Exception {
        Location location = new Location();
        LocationStore locationStore = JsonLocationStore.getInstance();
        assertNotNull(locationStore);

        Integer id = locationStore.addNew(location);
        assertNotNull(id);
    }

    @Test
    public void testGetLocationById() throws Exception {

    }

    @Test
    public void testGetLocations() throws Exception {

    }
}