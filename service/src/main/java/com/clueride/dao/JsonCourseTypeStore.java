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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import com.clueride.domain.course.CourseType;
import com.clueride.domain.course.CourseTypeStore;
import com.clueride.io.PojoJsonUtil;

/**
 * Implementation of CourseTypeStore.
 */
public class JsonCourseTypeStore implements CourseTypeStore {
    private static List<CourseType> instances = new ArrayList<>();
    private static Map<Integer,CourseType> instancePerId = new HashMap<>();

    @Inject
    public JsonCourseTypeStore() {
        loadAll();
    }

    private void loadAll() {
        instances = PojoJsonUtil.loadCourseTypes();
        reIndex();
    }

    private void reIndex() {
        for (CourseType instance : instances) {
            instancePerId.put(instance.getId(), instance);
        }
    }

    @Override
    public Integer addNew(CourseType courseType) throws IOException {
        return null;
    }

    @Override
    public CourseType getCourseTypeById(Integer courseTypeId) {
        return instancePerId.get(courseTypeId);
    }

}
