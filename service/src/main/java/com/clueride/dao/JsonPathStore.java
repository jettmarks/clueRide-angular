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
import com.google.inject.Inject;

import com.clueride.domain.user.path.Path;
import com.clueride.io.JsonStoreType;
import com.clueride.io.PojoJsonService;
import com.clueride.io.PojoJsonUtil;

/**
 * Implementation of PathStore that writes the Store out to JSON files.
 */
@Singleton
public class JsonPathStore implements PathStore {

    /** In-memory references to our known Paths. */
    private Map<Integer,Path> pathMap = new HashMap<>();
    private final PojoJsonService pojoJsonService;

    @Inject
    public JsonPathStore(PojoJsonService pojoJsonService) {
        this.pojoJsonService = pojoJsonService;
        loadAll();
    }

    private void loadAll() {
        List<Path> paths = pojoJsonService.loadPaths();
        for (Path path : paths) {
            Integer id = path.getId();
            pathMap.put(id, path);
            // TODO: Validate the Edges against the Edge store -- In the Service layer
        }
    }

    @Override
    public Integer addNew(Path path) throws IOException {
        validate(path);
        File outFile = PojoJsonUtil.getFile(path.getId(), JsonStoreType.PATH);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper
                    .writer()
                    .withDefaultPrettyPrinter()
                    .writeValue(outFile, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path.getId();
    }

    private void validate(Path path) {
        // TODO: Populate this (NOTE: much of the validation should occur in a service).
    }

    @Override
    public Path getPathById(Integer id) {
        return pathMap.get(id);
    }

    @Override
    public void update(Path path) {
        Integer id = path.getId();
        pathMap.put(id, path);
    }
}
