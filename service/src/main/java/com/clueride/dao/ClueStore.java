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
import java.util.List;

import com.clueride.domain.user.Clue;

/**
 * Persistence interface for {@link Clue} instances.
 */
public interface ClueStore {
    /**
     * Accepts fully-constructed {@link Clue} to the store and returns the ID.
     * @param clue - instance to be added to the memory-based copy (which may be persisted).
     * @return Unique Integer ID of the new Clue.
     * @throws IOException if trouble persisting.
     */
    Integer addNew(Clue clue) throws IOException;

    /**(
     * Retrieves from the store the {@link Clue} matching the ID.
     * @param id - Unique identifier for the Clue.
     * @return the matching Clue.
     */
    Clue getClueById(Integer id);

    /**
     * Accept the {@link Clue} instance as a replacement for the existing Clue with
     * the same ID as this Clue.
     * @param clue - Instance of Clue to be replace the existing one already in the store.
     */
    void update(Clue clue);

    /**
     * When changes are made to the locations, it may be necessary to re-index the clue store.
     */
    void reIndex();

    /**
     * Returns a list of fully-populated Clues for the given Location.
     * @param locationId - Unique ID of a Location which may or may not have clues associated.
     * @return - List of Clues or an empty list if the Location has no clues.
     */
    List<Clue> getCluesByLocation(Integer locationId);

}
