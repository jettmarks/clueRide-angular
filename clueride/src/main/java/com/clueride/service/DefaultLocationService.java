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

import com.clueride.dao.LocationStore;
import com.clueride.domain.user.Location;
import com.clueride.io.PojoJsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import java.io.*;

/**
 * Services requests for Locations.
 */
public class DefaultLocationService implements LocationService {

    private final LocationStore locationStore;

    @Inject
    public DefaultLocationService(LocationStore locationStore) {
        this.locationStore = locationStore;
    }

    @Override
    public String getLocation(Integer locationId) {
        String result = null;
        Location location = locationStore.getLocationById(locationId);
        try {
            result = PojoJsonUtil.generateLocation(location);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void saveLocationImage(InputStream fileData) {
        File file = new File("capture.jpg");
        if (!file.exists() || !file.canWrite()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            IOUtils.copy(fileData, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
