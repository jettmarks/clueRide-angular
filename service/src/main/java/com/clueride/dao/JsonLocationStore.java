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
 * Created by jett on 11/24/15.
 */
package com.clueride.dao;

import com.clueride.domain.user.Location;
import com.clueride.io.PojoJsonUtil;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonLocationStore implements LocationStore {

    /** Storage for Locations; the data Users care to see. */
    private static LocationStore instance = new JsonLocationStore();

    private List<Location> locations = new ArrayList<>();

    // TODO: will eventually read from disk
    private JsonLocationStore() {
        loadAll();
    }

    private void loadAll() {

    }

    @Override
    public Integer addNew(Location location) {
        File outFile = PojoJsonUtil.getFileForLocation(location);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper
                    .writer()
                    .withDefaultPrettyPrinter()
                    .writeValue(outFile, location);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location.getId();
    }


    @Override
    public Location getLocationById(Integer id) {
        return null;
    }

    @Override
    public List<Location> getLocations() {
        return null;
    }

    /**
     * Returns an instance of JsonLocationStore appropriate for reading/writing
     * locations to a JSON-based set of files.
     * @return
     */
    public static LocationStore getInstance() {
        return instance;
    }
}
