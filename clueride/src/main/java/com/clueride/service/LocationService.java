/*
 * Copyright 2015 Jett Marks
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
 * Created by jett on 12/12/15.
 */
package com.clueride.service;

import java.io.InputStream;

/**
 * Provides business-layer services for User's Location instances.
 *
 * This is also used by Location Editor when constructing Location records in the field.
 */
public interface LocationService {
    /**
     * Given an ID, return the JSON string representing the Location.
     * @param locationId - Unique Integer ID for the Location.
     * @return String JSON representing User's Loccation domain object.
     */
    String getLocation(Integer locationId);

    /**
     * Accept an InputStream and persist this against the Location ID provided.
     * @param fileData - InputStream with the JPEG image data to be saved to a file.
     */
    void saveLocationImage(InputStream fileData);
}
