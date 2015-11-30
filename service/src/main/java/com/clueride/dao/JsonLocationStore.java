package com.clueride.dao;

import com.clueride.domain.user.Location;

import java.util.ArrayList;
import java.util.List;

/**
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
public class JsonLocationStore implements LocationStore {

    /** Storage for Locations; the data Users care to see. */
    private static LocationStore instance = new JsonLocationStore();

    private List<Location> locations = new ArrayList<>();

    // TODO: will eventually read from disk
    private JsonLocationStore() {}

    @Override
    public Integer addNew(Location location) {
//        location.setI
        return null;
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
