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
 * Created by jett on 7/10/16.
 */
package com.clueride.service;

import com.clueride.domain.CourseType;

/**
 * Handles requests from client -- particularly the REST Web API -- for Course Types.
 */
public interface CourseTypeService {
    /**
     * Given a Course Type ID, retrieve the details for that Type of Course.
     * @param courseTypeId - Unique identifier for the Course Type.
     * @return CourseType instance holding course type details.
     */
    CourseType getCourseType(Integer courseTypeId);
}
