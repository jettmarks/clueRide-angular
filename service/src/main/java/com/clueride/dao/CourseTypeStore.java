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
package com.clueride.dao;

import java.io.IOException;

import com.clueride.domain.CourseType;

/**
 * Persistence interface for {@link CourseType} instances.
 */
public interface CourseTypeStore {
    /**
     * Accepts fully-populated CourseType instance and returns the ID of a newly persisted record.
     *
     * Since the creation of a new instance a) occurs infrequently and b) requires manual creation/editing
     * of web pages on the main website, this method may not be used.
     *
     * @param courseType - Fully-populated instance.
     * @return Integer DB-generated ID.
     * @throws IOException when there is trouble writing to the store.
     */
    Integer addNew(CourseType courseType) throws IOException;

    /**
     * Given a specific Course Type identifier, return the matching {@link CourseType} instance.
     * @param courseTypeId - Unique Integer ID representing the CourseType.
     * @return Matching CourseType.
     */
    CourseType getCourseTypeById(Integer courseTypeId);
}
