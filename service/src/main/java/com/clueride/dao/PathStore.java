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
 * Created by jett on 1/17/16.
 */
package com.clueride.dao;

import java.io.IOException;

import com.clueride.domain.user.Path;

/**
 * Persistance interface for {@link Path} instances.
 */
public interface PathStore {
    /**
     * Accepts fully constructed {@link Path} to the store and returns the ID.
     *
     * @param path - instance to be added to the memory-based copy (which may be persisted).
     * @return Unique Integer ID of the new Path.
     * @throws IOException
     */
    Integer addNew(Path path) throws IOException;

    /**
     * Retrieve the {@link Path} matching the ID from the store.
     * @param id - Unique identifier for the Path.
     * @return the matching Path.
     */
    Path getPathById(Integer id);

    /**
     * Accept the {@link Path} instance as a replacement for the existing Path with
     * the same ID as this Path instance.
     * @param path - Instance of Path to replace the one already in the store.
     */
    void update(Path path);
}
