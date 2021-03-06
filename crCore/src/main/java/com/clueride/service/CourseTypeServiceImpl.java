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

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.clueride.domain.course.CourseType;
import com.clueride.domain.course.CourseTypeStore;

/**
 * Implementation of the CourseTypeService.
 */
public class CourseTypeServiceImpl implements CourseTypeService {
    private static final Logger LOGGER = Logger.getLogger(CourseTypeServiceImpl.class);
    private final CourseTypeStore courseTypeStore;

    @Inject
    public CourseTypeServiceImpl(CourseTypeStore courseTypeStore) {
        this.courseTypeStore = courseTypeStore;
    }

    @Override
    public CourseType getCourseType(Integer courseTypeId) {
        LOGGER.info("Requesting Course Type data for Course Type " + courseTypeId);
        return courseTypeStore.getCourseTypeById(courseTypeId);
    }

}
