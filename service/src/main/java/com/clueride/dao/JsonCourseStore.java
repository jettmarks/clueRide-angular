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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import com.clueride.domain.course.Course;
import com.clueride.io.JsonStoreType;
import com.clueride.io.PojoJsonUtil;

/**
 * Implementation of CourseStore that writes the Store out to JSON files.
 */
@Singleton
public class JsonCourseStore implements CourseStore {

    /** In-memory references to our known Paths. */
    private Map<Integer,Course> courseMap = new HashMap<>();

    @Inject
    public JsonCourseStore() {
        loadAll();
    }

    private void loadAll() {
        List<Course> courses = PojoJsonUtil.loadCourses();
        for (Course course : courses) {
            Integer id = course.getId();
            courseMap.put(id, course);
            // TODO: Validate the Paths against the Path store
        }
    }

    @Override
    public Integer addNew(Course course) throws IOException {
        validate(course);
        File outFile = PojoJsonUtil.getFile(course.getId(), JsonStoreType.COURSE);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper
                    .writer()
                    .withDefaultPrettyPrinter()
                    .writeValue(outFile, course);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return course.getId();
    }

    @Override
    public Course getCourseById(Integer id) {
        return courseMap.get(id);
    }

    @Override
    public void update(Course course) {
        Integer id = course.getId();
        courseMap.put(id, course);
    }

    @Override
    public List<Course> getCourses() {
        return ImmutableList.copyOf(courseMap.values());
    }

    private void validate(Course course) {
        // TODO: Populate this (NOTE: much of the validation should occur in the Builder)
    }

}
