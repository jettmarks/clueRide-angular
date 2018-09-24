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

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import com.clueride.domain.course.Course;
import com.clueride.domain.course.CourseStore;
import com.clueride.service.builder.CourseBuilder;

/**
 * Implementation of the CourseService.
 */
public class CourseServiceImpl implements CourseService {
    private static final Logger LOGGER = Logger.getLogger(CourseServiceImpl.class);
    private final CourseStore courseStore;
    private final CourseBuilder courseBuilder;

    @Inject
    public CourseServiceImpl(
            CourseStore courseStore,
            CourseBuilder courseBuilder
    ) {
        this.courseStore = courseStore;
        this.courseBuilder = courseBuilder;
    }

    @Override
    public Course getCourse(Integer courseId) {
        return courseStore.getCourseById(courseId);
    }

    @Override
    public String getCourseMetaData(Integer courseId) {
        LOGGER.info("Requesting Course Data for Course " + courseId);
        if (courseId == null) {
            LOGGER.info("Returning all " + courseStore.getCourses().size());
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(courseStore.getCourses());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            Course course = courseStore.getCourseById(courseId);
            if (course == null) {
                LOGGER.error("Course ID " + courseId + " not found");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(course);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return "Problem";
    }

    @Override
    public String getCourseGeometry(Integer courseId) {
        LOGGER.info("Requesting Course Geometry for Course " + courseId);
        Course course = courseStore.getCourseById(courseId);
        return courseBuilder.getCourseFeatureCollection(course);
    }

    @Override
    public String getCourseSchedule(Integer courseId) {
        return null;
    }

}
