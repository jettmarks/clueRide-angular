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

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import com.clueride.dao.ClueStore;
import com.clueride.domain.user.Clue;

/**
 * Implementation of the Clue Service.
 */
public class ClueServiceImpl implements ClueService {
    private static final Logger LOGGER = Logger.getLogger(ClueServiceImpl.class);
    private final ClueStore clueStore;

    @Inject
    public ClueServiceImpl(ClueStore clueStore) {
        this.clueStore = clueStore;
    }

    @Override
    public String getCluesPerLocation(Integer locationId) {
        return null;
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

    @Override
    public void update(Clue clue) {

    }
}
