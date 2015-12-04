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
 * Created by jett on 12/3/15.
 */
package com.clueride.service;

import com.clueride.dao.JsonLocationStore;
import com.clueride.dao.LocationStore;
import com.clueride.domain.user.Location;
import com.clueride.io.PojoJsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.FileNotFoundException;

/**
 * Services requests for Locations.
 *
 *
 */
public class LocationService {

    private static final LocationService INSTANCE = new LocationService();
    private static LocationStore locationStore;

    public static LocationService getInstance() {
        return INSTANCE;
    }

    private LocationService() {
        locationStore = JsonLocationStore.getInstance();
    }
    public String getLocation(Integer locationId) {
        String result = null;
//        Location location = locationStore.getLocationById(locationId);
        Location location = null;
        try {
            location = PojoJsonUtil.loadLocationId(locationId);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            result = PojoJsonUtil.generateLocation(location);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
