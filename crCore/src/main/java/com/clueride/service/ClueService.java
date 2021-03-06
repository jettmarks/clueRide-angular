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

import com.clueride.domain.user.Clue;

/**
 * Handles requests from the client for Clues which are referred to by Locations.
 */
public interface ClueService {
    /**
     * Given a Location ID, retrieve the list of clues associated with that Location.
     * @deprecated moving to Puzzles instead of Clues.
     */
    String getCluesPerLocation(Integer locationId);

    /**
     * Given a specific Clue ID, return that Clue.
     */
    String getClue(Integer clueId);

    /**
     * Given a Location and a Clue ID, remove that clue from the Location's list and persist the
     * updated Location; the clue remains otherwise unchanged.
     * @param locId - ID of the Location to be modified with the removal of a Clue Reference.
     * @param clueId - The Clue Reference to be removed.
     */
    void removeClueFromLocation(Integer locId, Integer clueId);

    /* TODO: CA-199 - persist clues. */
    void update(Clue clue);

    /**
     * Persists the clue, not yet added to any location.
     * @param clue - fully-populated instance of Clue.
     * @return - String JSON for the resulting fully-populated instance.
     */
    String addClue(Clue clue);
}
