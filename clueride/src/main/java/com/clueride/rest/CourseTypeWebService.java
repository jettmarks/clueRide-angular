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
package com.clueride.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.clueride.service.CourseTypeService;

/**
 * API for working with Course Types.
 *
 * At this time, this is read-only because at this time, there is little need to
 * provide an API for creating new Course Types.
 */
@Path("course-type")
public class CourseTypeWebService {
    private final CourseTypeService courseTypeService;

    @Inject
    public CourseTypeWebService(CourseTypeService courseTypeService) {
        this.courseTypeService = courseTypeService;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public com.clueride.domain.CourseType getCourseTypeById(@PathParam("id") Integer courseTypeId) {
        return courseTypeService.getCourseType(courseTypeId);
    }

}
