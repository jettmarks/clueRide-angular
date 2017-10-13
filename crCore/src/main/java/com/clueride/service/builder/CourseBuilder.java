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
package com.clueride.service.builder;

import javax.inject.Inject;

import org.geotools.feature.DefaultFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.dao.NetworkStore;
import com.clueride.dao.NodeStore;
import com.clueride.dao.PathStore;
import com.clueride.domain.Course;
import com.clueride.domain.GeoNode;
import com.clueride.domain.user.path.Path;
import com.clueride.feature.Edge;
import com.clueride.geo.TranslateUtil;
import com.clueride.io.GeoJsonUtil;
import com.clueride.io.JsonStoreType;

/**
 * Assembles a Feature Collection for a Course so it can be displayed on a map.
 */
public class CourseBuilder {
    private final PathStore pathStore;
    private final NetworkStore networkStore;
    private final NodeStore nodeStore;

    @Inject
    public CourseBuilder(
            PathStore pathStore,
            NetworkStore networkStore,
            NodeStore nodeStore
    ) {
        this.pathStore = pathStore;
        this.networkStore = networkStore;
        this.nodeStore = nodeStore;
    }

    public String getCourseFeatureCollection(Course course) {
        GeoJsonUtil jsonRespWriter = new GeoJsonUtil(JsonStoreType.OTHER);
        DefaultFeatureCollection fcPoints = new DefaultFeatureCollection();
        DefaultFeatureCollection fcNonPoints = new DefaultFeatureCollection();
        Path path = null;
        GeoNode node;
        for (Integer pathId : course.getPathIds()) {
            // TODO: Refactor this to use what is already done in the Path Builder.
            path = pathStore.getPathById(pathId);
            node = (GeoNode) nodeStore.getNodeById(path.getStartNodeId());
            fcPoints.add(TranslateUtil.geoNodeToFeature(node));
            for (Integer edgeId : path.getEdgeIds()) {
                Edge edge = networkStore.getEdgeById(edgeId);
                fcNonPoints.add(edge.getFeature());
            }
        }
        if (path == null) {
            throw new IllegalStateException("No paths in the course");
        }
        node = (GeoNode) nodeStore.getNodeById(path.getEndNodeId());
        fcPoints.add(TranslateUtil.geoNodeToFeature(node));
        String pointResult = jsonRespWriter.toString(fcPoints);
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(pointResult.substring(0, pointResult.length()-2));
        for (SimpleFeature feature : fcNonPoints) {
            stringBuffer.append(",").append(jsonRespWriter.toString(feature));
        }
        stringBuffer.append("]}");
        return stringBuffer.toString();
    }
}
