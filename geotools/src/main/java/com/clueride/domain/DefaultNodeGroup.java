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
 * Created Sep 1, 2015
 */
package com.clueride.domain;

import com.clueride.domain.dev.NodeGroup;
import com.vividsolutions.jts.geom.Point;

/**
 * Description.
 *
 * @author jett
 *
 */
public class DefaultNodeGroup extends DefaultGeoNode implements NodeGroup {

    private double radius = 0.0002;

    public DefaultNodeGroup(Point point, Double radius) {
        super(point);
        this.setName("Temp Group");
        this.radius = radius;
    }

    /**
     * @see com.clueride.domain.dev.NodeGroup#getRadius()
     */
    @Override
    public Double getRadius() {
        return radius;
    }

    /**
     * @see com.clueride.domain.dev.NodeGroup#setLat(java.lang.Double)
     */
    @Override
    public void setLat(Double lat) {
        point.getCoordinates()[0].y = lat;
    }

    /**
     * @see com.clueride.domain.dev.NodeGroup#setLon(java.lang.Double)
     */
    @Override
    public void setLon(Double lon) {
        point.getCoordinates()[0].x = lon;
    }

}
