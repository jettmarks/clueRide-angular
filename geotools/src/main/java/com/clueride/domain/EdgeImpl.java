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
 * Created Oct 4, 2015
 */
package com.clueride.domain;

import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.dev.Node;
import com.clueride.domain.dev.TrackImpl;
import com.clueride.feature.Edge;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * Description.
 *
 * @author jett
 *
 */
public class EdgeImpl extends TrackFeatureImpl implements
        Edge {

    /**
     * @param track
     * @param lineStringFeature
     */
    public EdgeImpl(TrackImpl track,
            LineString lineString) {
        super(track, lineString);
    }

    /**
     * Constructor where the Feature already has the domain-specific attributes.
     * 
     * @param lineStringFeature
     */
    public EdgeImpl(SimpleFeature lineStringFeature) {
        super(lineStringFeature);
        // TODO: At some point, we may want to record specific attributes
    }

    /**
     * @see com.clueride.domain.dev.UnratedSegment#getStart()
     */
    @Override
    public Node getStart() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see com.clueride.domain.dev.UnratedSegment#getEnd()
     */
    @Override
    public Node getEnd() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see com.clueride.feature.LineFeature#getGeoStart()
     */
    public Point getGeoStart() {
        return ((LineString) feature.getDefaultGeometry()).getStartPoint();
    }

    /**
     * @see com.clueride.feature.LineFeature#getGeoEnd()
     */
    public Point getGeoEnd() {
        return ((LineString) feature.getDefaultGeometry()).getEndPoint();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EdgeImpl [getId()=").append(getId()).append(
                ", getDisplayName()=").append(getDisplayName()).append(
                ", getUrl()=").append(getUrl()).append("]");
        return builder.toString();
    }

}
