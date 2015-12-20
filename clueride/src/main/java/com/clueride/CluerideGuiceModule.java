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
package com.clueride;

import com.clueride.dao.FileImageStore;
import com.clueride.dao.ImageStore;
import com.clueride.service.DefaultLocationService;
import com.clueride.service.DefaultNodeService;
import com.clueride.service.LocationService;
import com.clueride.service.NodeService;
import com.google.inject.AbstractModule;

/**
 * Bindings for Guice in the Clueride Module.
 */
public class CluerideGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(LocationService.class).to(DefaultLocationService.class);
        bind(NodeService.class).to(DefaultNodeService.class);
        bind(ImageStore.class).to(FileImageStore.class);
    }
}
