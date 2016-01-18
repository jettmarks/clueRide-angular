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
package com.clueride.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.clueride.service.CourseService;

/**
 * API for working with a Course.
 *
 * Parts of this can be shared with the Client side, but when developing a course, we'll
 * be editing changes as well as viewing lists of courses with different criteria for
 * selection.
 */
@Path("course")
public class Course {
    private CourseService courseService;

    @Inject
    public Course(CourseService courseService) {
        this.courseService = courseService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCourseMetaData(@QueryParam("courseId") Integer courseId) {
        return courseService.getCourseMetaData(courseId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("show")
    public String getCourseGeometry(@QueryParam("courseId") Integer courseId) {
        return courseService.getCourseGeometry(courseId);
    }

}
