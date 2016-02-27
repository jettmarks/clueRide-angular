/*
 * Copyright 2016 Jett Marks
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
 * Created by jett on 2/27/16.
 */
package com.clueride.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.clueride.domain.user.Clue;
import com.clueride.domain.user.Location;
import com.clueride.io.PojoJsonUtil;

/**
 * Implementation of ClueStore that writes the Store out to JSON files.
 */
public class JsonClueStore implements ClueStore {
    private static final Logger LOGGER = Logger.getLogger(JsonClueStore.class);

    /** In-memory references to the full set of Clues indexed by Location. */
    private Map<Integer,List<Clue>> cluesPerLocation = new HashMap<>();
    /** In-memory references to the full set of Clues indexed by their ID. */
    private Map<Integer,Clue> clueById = new HashMap<>();

    private final LocationStore locationStore;

    @Inject
    public JsonClueStore(LocationStore locationStore) {
        this.locationStore = locationStore;
        loadAll();
    }

    /**
     * Reads through all Clue locations on disk to bring in and then index the Clues
     * for each location.
     *
     * It's possible to validate the Locations against the clues by checking the
     * available clues against the references within the locations.  It's also possible
     * to validate that any given clue is only referred to by a single location, but
     * not sure that we want this or not.
     */
    private void loadAll() {
        /* Bring in all the clues available. */
        Collection<Clue> clues = PojoJsonUtil.loadClues();

        /* Index the clues by ID. */
        for (Clue clue : clues) {
            clueById.put(clue.getId(), clue);
        }

        /* Index the clues by Location. */
        Collection<Location> locations = locationStore.getLocations();
        for (Location location : locations) {
            List<Clue> cluesInThisLocation = new ArrayList<>();
            cluesPerLocation.put(location.getId(), cluesInThisLocation);
            for (Integer clueId : location.getClueIds()) {
                Clue clue = clueById.get(clueId);
                if (clue != null) {
                    cluesInThisLocation.add(clue);
                } else {
                    LOGGER.warn("Location " + location.getId() + " refers to missing clue " + clueId);
                }
            }
        }
    }

    @Override
    public Integer addNew(Clue clue) throws IOException {
        return null;
    }

    @Override
    public Clue getClueById(Integer id) {
        return clueById.get(id);
    }

    @Override
    public void update(Clue clue) {

    }

    @Override
    public List<Clue> getCluesByLocation(Integer locationId) {
        return cluesPerLocation.get(locationId);
    }
}
