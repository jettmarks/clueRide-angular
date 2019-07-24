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
 * Created by jett on 12/20/15.
 */
package com.clueride.dao;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Storage of Images used by Clue Ride.
 *
 * Location images are keyed using a location id.
 *
 * Callers are responsible for assembling the URL which is used by the application to
 * retrieve images stored; the location ID and Sequence number are the two keys used
 * to retrieve the file via a separate server.
 */
public interface ImageStore {
    /**
     * Given the location ID and the data, determine a sequence number for this
     * image based on previously stored images (to be returned to the caller) and
     * then persist the image data.
     * @param locationId - unique identifier for the Location; ties the
     * @param imageData - InputStream that supplies the image data to be stored.
     * @return Integer sequence number of the image for this location.
     */
     Integer addNew(Integer locationId, InputStream imageData) throws FileNotFoundException;

}
