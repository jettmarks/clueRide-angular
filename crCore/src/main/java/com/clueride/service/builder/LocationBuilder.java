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
 * Created by jett on 2/6/16.
 */
package com.clueride.service.builder;

import javax.inject.Inject;

import org.geotools.feature.DefaultFeatureCollection;

import com.clueride.dao.NodeStore;
import com.clueride.domain.GeoNode;
import com.clueride.domain.user.Location;
import com.clueride.geo.TranslateUtil;
import com.clueride.io.GeoJsonUtil;
import com.clueride.io.JsonStoreType;
import com.clueride.service.NodeService;

/**
 * Assembles a Feature Collection for a Location so it can be displayed on a map.
 */
public class LocationBuilder {
    private final NodeStore nodeStore;

    @Inject
    public LocationBuilder(NodeStore nodeStore, NodeService nodeService) {
        this.nodeStore = nodeStore;
    }

    public String getLocationFeatureCollection(Location location) {
        GeoNode node = (GeoNode) nodeStore.getNodeById(location.getNodeId());
        GeoJsonUtil jsonRespWriter = new GeoJsonUtil(JsonStoreType.OTHER);
        DefaultFeatureCollection fcPoints = new DefaultFeatureCollection();
        fcPoints.add(TranslateUtil.geoNodeToFeature(node));
        String result = jsonRespWriter.toString(fcPoints);
        return result;
    }
}
