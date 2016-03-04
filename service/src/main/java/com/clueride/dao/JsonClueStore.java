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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static Map<Integer,List<Clue>> cluesPerLocation = new HashMap<>();
    /** In-memory references to the full set of Clues indexed by their ID. */
    private static Map<Integer,Clue> clueById = new HashMap<>();

    /** Working reference to the Clues as a list. */
    private static Collection<Clue> clues;

    private static boolean needsReload = true;

    private final LocationStore locationStore;

    @Inject
    public JsonClueStore(LocationStore locationStore) {
        this.locationStore = locationStore;
        if (needsReload) {
            loadAll();
        }
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
        clues = PojoJsonUtil.loadClues();
        reIndex();
        needsReload = false;
    }

    public void reIndex() {
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

    /**
     * New Clue for the pool, added to both in-memory cache and persisted to the
     * file system as JSON.
     * ReIndexing could happen here, but more likely, the Location will be updated
     * with the new Clue and when that object is updated, it will kick off a reIndex.
     * @param clue - instance to be added to the memory-based copy (which may be persisted).
     * @return Integer ID of the new Clue.
     * @throws IOException
     */
    @Override
    public Integer addNew(Clue clue) throws IOException {
        validateClue(clue);
        clues.add(clue);
        reIndex();

        File outFile = PojoJsonUtil.getFileForClueId(clue.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper
                    .writer()
                    .withDefaultPrettyPrinter()
                    .writeValue(outFile, clue);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clue.getId();
    }

    private void validateClue(Clue clue) {

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

    @Override
    public boolean hasValidClue(Integer clueId) {
        /* At this time, there are no clues kept in the store which are not valid. */
        return (clueById.containsKey(clueId));
    }
}
