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
 * Created by jett on 7/5/16.
 */
package com.clueride.dao;

import java.io.IOException;

import com.clueride.domain.Outing;

/**
 * Persistence interface for {@link Outing} instances.
 */
public interface OutingStore {
    /**
     * Accepts fully-populated Outing instance and returns the ID of a newly persisted
     * record.
     *
     * @param outing - {@link Outing} instance which is fully-populated.
     * @return Integer DB-generated ID.
     * @throws IOException when there is trouble writing to the store.
     */
    Integer addNew(Outing outing) throws IOException;

    /**
     * Given a specific Outing Identifier, return the matching {@link Outing} instance.
     * @param outingId - Integer ID for the Outing.
     * @return Matching Outing.
     */
    Outing getOutingById(Integer outingId);
}
