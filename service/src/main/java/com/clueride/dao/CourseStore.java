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
import java.util.List;

import com.clueride.domain.Course;

/**
 * Persistence interface for {@link Course} instances.
 */
public interface CourseStore {
    /**
     * Accepts fully constructed {@link Course} to the store and returns the ID.
     *
     * @param course - instance to be added to the memory-based copy (which may be persisted).
     * @return Unique Integer ID of the new Course.
     * @throws IOException
     */
    Integer addNew(Course course) throws IOException;

    /**
     * Retrieve the {@link Course} matching the ID from the store.
     * @param id - Unique identifier for the Course.
     * @return the matching Course.
     */
    Course getCourseById(Integer id);

    /**
     * Accept the {@link Course} instance as a replacement for the existing Course with
     * the same ID as this Course instance.
     * @param course - Instance of Course to replace the one already in the store.
     */
    void update(Course course);

    /**
     * Returns Metadata for all Courses.
     * @return List of all Courses.
     */
    List<Course> getCourses();
}
