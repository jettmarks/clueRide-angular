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
 * Created by jett on 2/9/16.
 */
package com.clueride.service;

import javax.inject.Inject;

import org.apache.log4j.Logger;

/**
 * Default implementation of ControlService.
 */
public class DefaultControlService implements ControlService {

    private static final Logger LOGGER = Logger.getLogger(ControlService.class);

    private final Network network;

    @Inject
    public DefaultControlService(Network network) {
        this.network = network;
    }

    @Override
    public String refresh() {
        LOGGER.info("Refreshing Network indices");
        network.refreshIndices();
        return "{\"status\": \"OK\"}";
    }
}
