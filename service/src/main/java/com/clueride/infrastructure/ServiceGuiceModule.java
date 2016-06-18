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
import com.clueride.dao.CourseStore;
import com.clueride.dao.InvitationStore;
import com.clueride.dao.JsonClueStore;
import com.clueride.dao.JsonCourseStore;
import com.clueride.dao.JsonInvitationStore;
import com.clueride.dao.JsonLocationStore;
import com.clueride.dao.JsonPathStore;
import com.clueride.dao.LocationStore;
import com.clueride.dao.PathStore;

/**
 * Bindings for Services Guice Module.
 */
public class ServiceGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ClueStore.class).to(JsonClueStore.class);
        bind(CourseStore.class).to(JsonCourseStore.class);
        bind(PathStore.class).to(JsonPathStore.class);
        bind(LocationStore.class).to(JsonLocationStore.class);
        bind(InvitationStore.class).to(JsonInvitationStore.class);
    }
}
