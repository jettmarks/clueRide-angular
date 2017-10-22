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

import java.util.List;

import com.clueride.domain.user.location.Location;

/**
 * Defines the operations on a Puzzle.
 */
public interface PuzzleService {
    /**
     * Given the unique ID of a Puzzle, return the full instance.
     * @param id unique ID for the puzzle.
     * @return matching Puzzle instance.
     */
    Puzzle getById(Integer id);

    /**
     * Given a Location ID, find the Puzzles associated with that Location.
     * @param locationId unique identifier for a Location.
     * @return list of Puzzles for the location.
     */
    List<Puzzle> getByLocation(Integer locationId);

    /**
     * Create a New puzzle instance for the given Location.
     * @param puzzleBuilder data for the new Puzzle.
     * @param locationBuilder data for the Location to accept the new Puzzle.
     * @return fully-populated instance of the new Puzzle.
     */
    Puzzle addNew(Puzzle.Builder puzzleBuilder, Location.Builder locationBuilder);

}
