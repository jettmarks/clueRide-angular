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
 * Created by jett on 2/2/16.
 */
package com.clueride.service;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.clueride.dao.PathStore;
import com.clueride.domain.user.path.Path;
import com.clueride.service.builder.PathBuilder;

/**
 * Implementation of the PathService interface.
 */
public class PathServiceImpl implements PathService {
    private static final Logger LOGGER = Logger.getLogger(PathServiceImpl.class);
    private final PathStore pathStore;
    private final PathBuilder pathBuilder;

    @Inject
    public PathServiceImpl(
            PathStore pathStore,
            PathBuilder pathBuilder
    ) {
        this.pathStore = pathStore;
        this.pathBuilder = pathBuilder;
    }

    @Override
    public Path getPath(Integer pathId) {
        return pathStore.getPathById(pathId);
    }

    @Override
    public String getPathMetaData(Integer pathId) {
        return null;
    }

    @Override
    public String getPathGeometry(Integer pathId) {
        LOGGER.info("Requesting Path Geometry for Path/Leg " + pathId);
        Path path = pathStore.getPathById(pathId);
        return pathBuilder.getPathFeatureCollection(path);
    }

}
