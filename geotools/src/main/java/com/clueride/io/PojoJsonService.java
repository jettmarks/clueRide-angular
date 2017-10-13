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
 * Created by jett on 10/10/17.
 */
package com.clueride.io;

import java.util.List;

import com.clueride.domain.user.location.Location;
import com.clueride.domain.user.path.Path;

/**
 * Defines operations for Json Pojos.
 */
public interface PojoJsonService {
    /**
     * Knows how to retrieve the Location data from JSON files/stores on disk.
     * @return List of all Locations in the store.
     */
    List<Location> loadLocations();

    /**
     * Knows how to retrieve the Path data from JSON files/stores on disk.
     * @return List of all Paths in the store.
     */
    List<Path> loadPaths();
}
