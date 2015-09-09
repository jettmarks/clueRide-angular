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
 * Created Aug 15, 2015
 */
package com.clueride.domain.factory;

import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.DefaultNodeGroup;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NodeGroup;
import com.vividsolutions.jts.geom.Point;

/**
 * Description.
 *
 * @author jett
 *
 */
public class NodeFactory {

    private static Integer maxId = 0;

    /**
     * @param point
     * @return
     */
    public static GeoNode getInstance(Point point) {
        DefaultGeoNode node = new DefaultGeoNode();
        node.setPoint(point);
        return node;
    }

    public static GeoNode getInstance(double lon, double lat, double elevation) {
        DefaultGeoNode node = new DefaultGeoNode();
        node.setPoint(PointFactory.getJtsInstance(lon, lat, elevation));
        return node;
    }

    /**
     * @param lat
     * @param lon
     * @param elevation
     * @param radius
     * @return
     */
    public static NodeGroup getGroupInstance(double lat, double lon,
            double elevation, double radius) {
        Point underlyingPoint = PointFactory
                .getJtsInstance(lat, lon, elevation);
        DefaultNodeGroup nodeGroup = new DefaultNodeGroup(underlyingPoint,
                radius);
        nodeGroup.setId(getNextId());
        return nodeGroup;
    }

    /**
     * Assigns the next ID in sequence for Nodes.
     * 
     * TODO: Needs to pickup on IDs that are read into memory.
     * 
     * @return
     */
    public static Integer getNextId() {
        synchronized (maxId) {
            maxId++;
            return maxId;
        }
    }
}
