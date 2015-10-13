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

import java.util.List;

import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.dev.Node;
import com.clueride.domain.dev.TrackImpl;
import com.clueride.domain.dev.UnratedSegment;
import com.clueride.feature.Edge;
import com.clueride.feature.TrackFeature;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

/**
 * Represents a Raw Track (often from RideWithGPS at the {@link getUrl()}) which
 * may source a set of points (represented by {@link UnratedSegment}) that get
 * turned into a {@link Edge}.
 *
 * @author jett
 */
public class TrackFeatureImpl implements TrackFeature {
    private final Integer id;
    private String displayName;
    private String url;
    protected SimpleFeature feature;
    private LineString lineString;

    /** Canonical Constructor given domain-specific instance and the Geometry. */
    public TrackFeatureImpl(TrackImpl track, LineString lineString) {
        this.id = track.getId();
        this.displayName = track.getDisplayName();
        this.url = track.getUrl();
        this.lineString = lineString;
    }

    /**
     * Creates the separate Domain and Geometry instances from a fully-formed
     * Feature.
     * 
     * @param lineStringFeature
     */
    public TrackFeatureImpl(SimpleFeature lineStringFeature) {
        Object obj = lineStringFeature.getDefaultGeometry();
        if (obj == null) {
            throw new IllegalArgumentException(
                    "Expecting geometry to be non-null");
        }
        if (!(obj instanceof LineString)) {
            throw new IllegalArgumentException(
                    "Expecting geometry to be LineString instead of "
                            + obj.getClass());
        }
        this.lineString = (LineString) obj;
        Object idObj = lineStringFeature.getAttribute(0);
        Integer idInt = 0;
        if (idObj instanceof Integer) {
            idInt = (Integer) idObj;
        }
        if (idObj instanceof Long) {
            idInt = ((Long) idObj).intValue();
        }
        this.id = idInt;
        setDisplayName((String) lineStringFeature.getAttribute(1));
        this.url = (String) lineStringFeature.getAttribute(2);
        this.feature = lineStringFeature;
    }

    /**
     * @see com.clueride.domain.dev.Track#getId()
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * @see com.clueride.domain.dev.Track#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @see com.clueride.domain.dev.Track#setDisplayName(java.lang.String)
     */
    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @see com.clueride.domain.dev.Track#getUrl()
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * @see com.clueride.domain.dev.Track#getNodeList()
     */
    @Override
    public List<Node> getNodeList() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see com.clueride.domain.MappableFeature#getGeoJson()
     */
    @Override
    public String getGeoJson() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see com.clueride.feature.LineFeature#getLineString()
     */
    @Override
    public LineString getLineString() {
        return lineString;
    }

    /**
     * @see com.clueride.feature.LineFeature#setLineString(com.vividsolutions.jts.geom.LineString)
     *      TODO: Unsure if I'll be using this; hang on until later
     */
    @Override
    public void setLineString(LineString lineString) {

    }

    /**
     * @see com.clueride.feature.LineFeature#getGeometry()
     */
    @Override
    public Geometry getGeometry() {
        return (Geometry) feature.getDefaultGeometry();
    }

    /**
     * @see com.clueride.feature.LineFeature#getFeature()
     */
    @Override
    public SimpleFeature getFeature() {
        return feature;
    }

}
