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
 * Created by jett on 12/5/15.
 */
package com.clueride.config;

import com.google.inject.AbstractModule;

import com.clueride.dao.JsonNetworkStore;
import com.clueride.dao.JsonNodeStore;
import com.clueride.dao.NetworkStore;
import com.clueride.dao.NodeStore;
import com.clueride.io.PojoJsonService;
import com.clueride.io.PojoJsonUtil;

/**
 * Bindings for GeoTools Guice Module.
 */
public class GeoToolsGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(NodeStore.class).to(JsonNodeStore.class);
        bind(NetworkStore.class).to(JsonNetworkStore.class);
        bind(PojoJsonService.class).to(PojoJsonUtil.class);
    }
}
