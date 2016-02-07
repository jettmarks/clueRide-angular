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
package com.clueride.service.builder;

import javax.inject.Inject;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.dao.NetworkStore;
import com.clueride.dao.NodeStore;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NodeNetworkState;
import com.clueride.domain.user.Path;
import com.clueride.feature.Edge;
import com.clueride.feature.FeatureType;
import com.clueride.geo.TranslateUtil;
import com.clueride.io.GeoJsonUtil;
import com.clueride.io.JsonStoreType;

/**
 * Assembles a Feature Collection for a Path so it can be displayed on a map.
 */
public class PathBuilder {
    private final NetworkStore networkStore;
    private final NodeStore nodeStore;

    @Inject
    public PathBuilder(
            NetworkStore networkStore,
            NodeStore nodeStore
    ) {
        this.networkStore = networkStore;
        this.nodeStore = nodeStore;
    }


    public String getPathFeatureCollection(Path path) {
        GeoJsonUtil jsonRespWriter = new GeoJsonUtil(JsonStoreType.OTHER);
        DefaultFeatureCollection fcPoints = new DefaultFeatureCollection();
        DefaultFeatureCollection fcNonPoints = new DefaultFeatureCollection();

        GeoNode node;
        node = (GeoNode) nodeStore.getNodeById(path.getStartNodeId());
        // TODO: Consider the Path object knowing about the appropriate Node State
        node.setState(NodeNetworkState.PATH_START);

        fcPoints.add(TranslateUtil.geoNodeToFeature(node));
        for (Integer edgeId : path.getEdgeIds()) {
            Edge edge = networkStore.getEdgeById(edgeId);
            fcNonPoints.add(edge.getFeature());
        }

        node = (GeoNode) nodeStore.getNodeById(path.getEndNodeId());
        node.setState(NodeNetworkState.PATH_END);
        fcPoints.add(TranslateUtil.geoNodeToFeature(node));

        StringBuilder stringBuffer = new StringBuilder();

        // Add in the points
        String pointResult = jsonRespWriter.toString(fcPoints);
        stringBuffer.append(pointResult.substring(0, pointResult.length()-2));

        // Identify this collection as belonging to a specific path
        SimpleFeatureBuilder featureBuilder = getPathFeatureBuilder(path);
        String pathResult = jsonRespWriter.toString(featureBuilder.buildFeature(null));
        stringBuffer.append(",\n").append(pathResult);

        // Now add in each of the legs/edges
        for (SimpleFeature feature : fcNonPoints) {
            stringBuffer.append(",").append(jsonRespWriter.toString(feature));
        }

        stringBuffer.append("]}");
        return stringBuffer.toString();
    }

    public SimpleFeatureBuilder getPathFeatureBuilder(Path path) {
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(FeatureType.PATH_FEATURE_TYPE);
        featureBuilder.add(path.getId());
        featureBuilder.add(path.getStartNodeId());
        featureBuilder.add(path.getEndNodeId());
        featureBuilder.add(path.getStartLocationId());
        featureBuilder.add(path.getEndLocationId());
        return featureBuilder;
    }
}
