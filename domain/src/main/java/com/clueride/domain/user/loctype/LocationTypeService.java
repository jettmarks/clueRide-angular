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
 * Created by jett on 10/4/17.
 */
package com.clueride.domain.user.loctype;

import java.util.List;

/**
 * Defines operations on Location Type instances.
 */
public interface LocationTypeService {
    /**
     * Returns (possibly cached) list of all Location Types.
     * @return List of LocationType.
     */
    List<LocationType> getLocationTypes();

    /**
     * Returns the matching Location Type given the name of the Type shown to users.
     * @param locationTypeName String that the user recognizes.
     * @return Fully-populated instance of Location Type matching the name.
     */
    LocationType getByName(String locationTypeName);

    /**
     * Returns the matching Location Type given the ID of the Type.
     * @param locationTypeId ID stored in the Location instance.
     * @return Fully-populated instance of Location Type matching the ID.
     */
    LocationType getById(Integer locationTypeId);

}
