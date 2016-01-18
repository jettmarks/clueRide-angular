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
package com.clueride.service;

/**
 * Handles requests from client -- particularly the REST Web API -- for Courses.
 */
public interface CourseService {
    /**
     * Given a courseId, retrieve the metadata (name, description, list of Paths)
     * for that Course.
     * See {@link #getCourseGeometry} for retrieving the GeoJSON to put the course on
     * a map.
     * See {@link #getCourseSchedule} for retrieving the details regarding when this
     * course is planned to run.
     * @param courseId - Unique identifier for the Course, if null, return entire set
     *                 of Courses as an array.
     * @return JSON string representing the course's metadata.
     */
    String getCourseMetaData(Integer courseId);

    /**
     * Given a courseId, retrieve the GeoJSON for displaying this entire Course on a map
     * along with Points for the Locations.
     * Use the Path API for retrieving the course one Path at a time.
     * @param courseId - Unique identifier for the Course.
     * @return GeoJSON representing the paths and locations of the entire course.
     */
    String getCourseGeometry(Integer courseId);

    /** Future release. */
    String getCourseSchedule(Integer courseId);
}
