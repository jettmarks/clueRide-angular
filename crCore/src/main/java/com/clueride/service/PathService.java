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
 * Created by jett on 2/2/16.
 */
package com.clueride.service;

import com.clueride.domain.user.path.Path;

/**
 * Handles requests from Game Clients for the "legs" of the Course which run between
 * locations and their clues.
 */
public interface PathService {
    /**
     * Given Path ID, retrieve the Path matching that ID.
     * @param integer unique identifier for the Path.
     * @return Path record matching the given ID.
     */
    Path getPath(Integer integer);

    /**
     * Given a Path ID (provided by a Course), retrieve the metadata (Location IDs)
     * for that Path.
     * @param pathId - Unique identifier for the Path.
     * @return JSON string representing the path's metadata.
     */
    String getPathMetaData(Integer pathId);

    /**
     * Given a Path ID (provided by a Course), retrieve the GeoJSON for displaying
     * the various features on a Map.
     * @param pathId - Unique identifier for the Path.
     * @return GeoJSON string representing a Feature Collection of the segmement/edges
     * and Locations.
     */
    String getPathGeometry(Integer pathId);

}
