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

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Singleton
public class JsonLocationStore implements LocationStore {

    /** Reference to the Nodes which support the Locations. */
    private final NodeStore nodeStore;

    private Map<Integer,Location> locationMap = new HashMap<>();

    @Inject
    public JsonLocationStore(NodeStore nodeStore) {
        this.nodeStore = nodeStore;
        loadAll();
    }

    private void loadAll() {
        List<Location> locations = PojoJsonUtil.loadLocations();
        List<String> problemList = new ArrayList<>();
        for (Location location : locations)  {
            try {
                validateLocation(location);
                locationMap.put(location.getId(), location);
            } catch (IllegalStateException e) {
                problemList.add(e.getMessage());
            }
        }

        // Report validation problems
        if (!problemList.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Locations with Validation Problems:\n");
            for (String message : problemList) {
                stringBuilder.append(message).append("\n");
            }
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    /**
     * Validates the Location against its underlying Node reference to be sure we've got that Node in the NodeStore.
     * @param location to be checked.
     * @return True if the location's Node ID is found in the NodeStore.
     * @throws IllegalStateException if the NodeID isn't found in the NodeStore.
     */
    private boolean validateLocation(Location location) {
        if (null == nodeStore.getNodeById(location.getNodeId()) ) {
            StringBuilder stringBuilder = new StringBuilder()
                    .append(location.getName()).append('(').append(location.getId())
                    .append(") NodeId:").append(location.getNodeId()).append("\n");
            throw new IllegalStateException(stringBuilder.toString());
        }
        return true;
    }

    @Override
    public Integer addNew(Location location) throws IOException {
        validateLocation(location);
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
        return locationMap.get(id);
    }

    @Override
    public Collection<Location> getLocations() {
        return locationMap.values();
    }
}
