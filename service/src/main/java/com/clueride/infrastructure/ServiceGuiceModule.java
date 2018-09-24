/*
 * Copyright 2015 Jett Marks
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
 * Created by jett on 12/10/15.
 */
package com.clueride.infrastructure;

import com.google.inject.AbstractModule;

import com.clueride.dao.ClueStore;
import com.clueride.dao.JsonClueStore;
import com.clueride.dao.JsonCourseStore;
import com.clueride.dao.JsonCourseTypeStore;
import com.clueride.dao.JsonLocationStore;
import com.clueride.dao.JsonMemberStore;
import com.clueride.dao.JsonNodeStore;
import com.clueride.dao.JsonOutingStore;
import com.clueride.dao.JsonPathStore;
import com.clueride.dao.NodeStore;
import com.clueride.dao.PathStore;
import com.clueride.domain.account.member.MemberStore;
import com.clueride.domain.account.member.MemberStoreJpa;
import com.clueride.domain.course.CourseStore;
import com.clueride.domain.course.CourseTypeStore;
import com.clueride.domain.outing.OutingStore;
import com.clueride.domain.user.location.LocationStore;
import com.clueride.domain.user.location.LocationStoreJpa;

/**
 * Bindings for Services Guice Module.
 */
public class ServiceGuiceModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(ClueStore.class).to(JsonClueStore.class);
        bind(CourseStore.class).to(JsonCourseStore.class);
        bind(CourseTypeStore.class).to(JsonCourseTypeStore.class);
        bind(LocationStore.class).annotatedWith(Json.class).to(JsonLocationStore.class);
        bind(LocationStore.class).annotatedWith(Jpa.class).to(LocationStoreJpa.class);
        bind(MemberStore.class).annotatedWith(Json.class).to(JsonMemberStore.class);
        bind(MemberStore.class).annotatedWith(Jpa.class).to(MemberStoreJpa.class);
        bind(NodeStore.class).to(JsonNodeStore.class);
        bind(OutingStore.class).to(JsonOutingStore.class);
        bind(PathStore.class).to(JsonPathStore.class);
    }
}
