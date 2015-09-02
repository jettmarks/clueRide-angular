/**
 *   Copyright 2015 Jett Marks
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created Aug 27, 2015
 */
package com.clueride.service;

import org.opengis.feature.simple.SimpleFeature;

import com.clueride.config.GeoProperties;
import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.DefaultNodeGroup;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NodeNetworkState;
import com.clueride.domain.factory.NodeFactory;
import com.clueride.domain.factory.PointFactory;
import com.clueride.geo.DefaultNetwork;
import com.clueride.geo.Network;
import com.clueride.geo.TranslateUtil;
import com.clueride.io.JsonStoreType;
import com.clueride.io.JsonUtil;
import com.vividsolutions.jts.geom.Point;

/**
 * Handles requests for Locations.
 *
 * @author jett
 */
public class LocationService {

    // TODO: Add Dependency Injection for the NetworkService
    private Network network = new DefaultNetwork();

    public String addNewLocation(Double lat, Double lon) {
        String result = "";
        JsonUtil jsonUtil = new JsonUtil(JsonStoreType.LOCATION);
        Point point = PointFactory.getJtsInstance(lat, lon, 0.0);
        GeoNode geoNode = new DefaultGeoNode();
        geoNode.setPoint(point);
        geoNode.setName("candidate");
        NodeNetworkState nodeEvaluation = network.evaluateNodeState(geoNode);
        result = jsonUtil.toString(geoNode);
        return result;
    }

    /**
     * @return
     */
    public String getLocationGroups() {
        // Make up our location groups for now
        double lat = 33.5;
        double lon = -84.4;
        double elevation = 300.0;
        double radius = (double) GeoProperties.getInstance()
                .get("group.radius");

        DefaultNodeGroup nodeGroup = (DefaultNodeGroup) NodeFactory
                .getGroupInstance(lat, lon, elevation, radius);
        SimpleFeature feature = TranslateUtil.groupNodeToFeature(nodeGroup);
        JsonUtil jsonUtil = new JsonUtil(JsonStoreType.LOCATION);
        return (jsonUtil.toString(feature));
    }
}
