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
 * Created by jett on 2/25/16.
 */
package com.clueride.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import com.clueride.dao.ClueStore;
import com.clueride.domain.user.Clue;
import com.clueride.domain.user.location.Location;
import com.clueride.domain.user.location.LocationStore;
import com.clueride.infrastructure.Json;

/**
 * Implementation of the Clue Service.
 */
public class ClueServiceImpl implements ClueService {
    private static final Logger LOGGER = Logger.getLogger(ClueServiceImpl.class);
    private final ClueStore clueStore;
    private final LocationStore locationStore;

    @Inject
    public ClueServiceImpl(
            ClueStore clueStore,
            @Json LocationStore locationStore
    ) {
        this.clueStore = clueStore;
        this.locationStore = locationStore;
    }

    @Override
    public String getCluesPerLocation(Integer locationId) {
        List<Clue> clues = new ArrayList<>();
//        List<Clue> clues = clueStore.getCluesByLocation(locationId);
        ObjectMapper objectMapper = new ObjectMapper();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        boolean firstTimeThrough = true;
        for (Clue clue : clues) {
            if (!firstTimeThrough) {
                stringBuilder.append(",");
            } else {
                firstTimeThrough = false;
            }
            try {
                stringBuilder.append(
                        objectMapper
                                .writer()
                                .withDefaultPrettyPrinter()
                                .writeValueAsString(clue)
                );
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    @Override
    public String getClue(Integer clueId) {
        LOGGER.info("Requesting Clue " + clueId);
        Clue clue = clueStore.getClueById(clueId);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper
                    .writer()
                    .withDefaultPrettyPrinter()
                    .writeValueAsString(clue);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    // TODO: CA-324 -- Move over to Location or Puzzle Service
    @Override
    public void removeClueFromLocation(Integer locId, Integer clueId) {
        LOGGER.info("Removing Clue ID " + clueId + " from Location " + locId);

        Location location = locationStore.getLocationById(locId);
//        location.removeClue(clueId);
        locationStore.update(location);
    }

    @Override
    public String addClue(Clue clue) {
        Integer clueId = clue.getId();
        LOGGER.info("Adding Clue " + clue.getName() + " with ID " + clueId);
        try {
            clueStore.addNew(clue);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /* Build the result the same as we would for a get. */
        return getClue(clueId);
    }

    @Override
    public void update(Clue clue) {

    }
}
