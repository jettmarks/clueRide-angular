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
 * Created by jett on 12/1/15.
 */
package com.clueride.io;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.domain.user.Clue;
import com.clueride.domain.user.Location;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Exercises the PojoJsonUtilTest class.
 */
public class PojoJsonUtilTest {

    @BeforeMethod
    public void setUp() throws Exception {

    }

    @Test
    public void testGetFileForLocation() throws Exception {

    }

    @Test
    public void testLoadLocations() throws Exception {
        List<Location> locations = PojoJsonUtil.loadLocations();
        assertNotNull(locations);
        assertTrue(locations.size() > 0);
    }

    @Test
    public void testLoadLocationId() throws Exception {
        Location location = PojoJsonUtil.loadLocationId(1);
        assertNotNull(location);
        assertEquals(location.getId(), new Integer(1));
    }

    @Test
    public void testLoadClueId() throws Exception {
        Clue clue = PojoJsonUtil.loadClue(1);
        assertNotNull(clue);
        assertEquals(clue.getId(), new Integer(1));
    }
}