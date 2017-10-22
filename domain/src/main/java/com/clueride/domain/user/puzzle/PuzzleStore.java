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
 * Created by jett on 10/19/17.
 */
package com.clueride.domain.user.puzzle;

import java.util.List;

import com.clueride.domain.user.location.Location;

/**
 * Definition of operations on Puzzle records.
 */
public interface PuzzleStore {
    /**
     * Accepts a Puzzle.Builder (including the Answers) and persists
     * as a new Puzzle record. The Puzzle Builder needs to have its
     * Location set so it can be attached to that Location.
     * @param puzzleBuilder fully-populated instance of Puzzle as a Builder.
     * @return unique ID for this new Puzzle instance.
     */
    Integer addNew(Puzzle.Builder puzzleBuilder);

    Puzzle.Builder getPuzzleById(Integer id);

    /**
     * Given a Location ID, retrieve all Puzzles for that location.
     *
     * @param locationBuilder@return (possibly empty) list of Puzzles for the Location.
     */
    List<Puzzle.Builder> getPuzzlesForLocation(Location.Builder locationBuilder);

}
