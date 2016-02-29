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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.codehaus.jackson.map.ObjectMapper;

import com.clueride.domain.user.Location;
import com.clueride.io.PojoJsonUtil;

@Singleton
public class JsonLocationStore implements LocationStore {

    /** Reference to the Nodes which support the Locations. */
    private final NodeStore nodeStore;

    private static Map<Integer,Location> locationMap = new HashMap<>();
    private static List<Location> locations;

    @Inject
    public JsonLocationStore(NodeStore nodeStore) {
        this.nodeStore = nodeStore;
        loadAll();
    }

    private void loadAll() {
        locations = PojoJsonUtil.loadLocations();
        reIndex(locations);
    }

    private void reIndex(List<Location> locations) {
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
            throw new IllegalStateException(
                    location.getName() + '(' + location.getId()
                            + ") NodeId:" + location.getNodeId() + "\n");
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

        locations.add(location);
        reIndex(locations);
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

    /**
     * Checks to see if the location exists in memory, and if so, removes from the old one from the list
     * before replacing with the new item.  The new item is then added as if it were a new location which also
     * triggers re-indexing the list.
     * @param location to be updated.
     */
    @Override
    public void update(Location location) {
        removeFromList(location.getId());
        try {
            addNew(location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pulls the reference to an outdated copy of the Location using the ID of the new instance passed in.
     * @param locId - Integer ID of the location being removed from the list.
     */
    private void removeFromList(Integer locId) {
        if (locationMap.containsKey(locId)) {
            for (Iterator<Location> iter = locations.iterator(); iter.hasNext(); ) {
                if ( iter.next().getId().equals(locId)) {
                    iter.remove();
                }
            }
        }
    }
}
