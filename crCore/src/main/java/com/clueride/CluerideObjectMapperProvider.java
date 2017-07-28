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
 * Created by jett on 3/6/16.
 */
package com.clueride;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handles the ObjectMapper POJO conversion for outbound Jersey responses.
 */
@Provider
public class CluerideObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
    }

    public CluerideObjectMapperProvider() {
        // Would we be able to use the LOGGER?
        System.out.println("Instantiate CluerideObjectMapperProvider");
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        System.out.println("CluerideObjectMapperProvider.getContext() called with type: "+type);
        return MAPPER;
    }
}
