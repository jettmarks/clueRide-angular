/*
 * Copyright 2017 Jett Marks
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
 * Created by jett on 10/22/17.
 */
package com.clueride.domain.user.puzzle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.clueride.domain.user.location.Location;

/**
 * Implementation of the PuzzleService interface.
 */
public class PuzzleServiceImpl implements PuzzleService {
    private static final Logger LOGGER = Logger.getLogger(PuzzleServiceImpl.class);
    private final PuzzleStore puzzleStore;

    @Inject
    public PuzzleServiceImpl(PuzzleStore puzzleStore) {
        this.puzzleStore = puzzleStore;
    }

    @Override
    public Puzzle getById(Integer id) {
        return puzzleStore.getPuzzleById(id).build();
    }

    @Override
    public List<Puzzle> getByLocation(Integer locationId) {
        List<Puzzle> puzzles = new ArrayList<>();
        Location.Builder locationBuilder = Location.Builder.builder().withId(locationId);
        for (Puzzle.Builder puzzleBuilder : puzzleStore.getPuzzlesForLocation(locationBuilder)) {
            try {
                puzzles.add(puzzleBuilder.build());
            } catch (IllegalStateException ise) {
                LOGGER.warn("Problem building puzzle from DB record: ", ise);
                // Continue retrieving remaining records
            }
        }
        return puzzles;
    }

    @Override
    public Puzzle addNew(Puzzle.Builder puzzleBuilder, Location.Builder locationBuilder) {
        return null;
    }
}
