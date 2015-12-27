/*
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
package com.clueride.feature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.GeoNode;
import com.clueride.domain.MappableFeature;

/**
 * Just as a PointFeature combines geometrical and non-geometrical properties
 * for a Node/Location, the LineFeature does the same for Segments.
 *
 * The designation of "Start" and "End" is arbitrary, but significant in that the direction
 * of travel determines which direction has uphill slopes.  Reversing the direction turns
 * uphills into downhills and downhills into uphills.  Any changes to the direction require
 * re-evaluation of the hilliness score.
 *
 * @author jett
 */
public interface LineFeature extends MappableFeature {

    /**
     * Unique Identifier for this LineFeature; generally comes from the extending classes and has
     * a life-cycle determined by that subclass.
     * @return
     */
    Integer getId();

    /**
     * The underlying Geometry instance representing a list of Points which can be mapped.
     * @return LineString geometry instance.
     */
    LineString getLineString();

    /**
     * Setter for the LineString.
     * @param lineString - Geometry representing list of Points in a given order.
     */
    void setLineString(LineString lineString);

    /**
     * More generic class for the LineString.
     * @return Geometry instance corresponding to this class's LineString -- generally the same instance.
     */
    Geometry getGeometry();

    /**
     * The SimpleFeature representation of this Geometry containing meta-data for the Geometry that
     * is recognized by the GeoTools library, the JSON utils, and the client.
     * The particular meta-data stored is dependent on the subclass.
     * @return SimpleFeature instance containing both the Geometry and the meta-data.
     */
    SimpleFeature getFeature();

    /**
     * Order-dependent "first" Point is defined as the Start Node.
     * @return GeoNode representing the first Point of the LineString.
     */
    GeoNode getGeoStart();

    /**
     * Order-dependent "last" Point is defined as the End Node.
     * @return GeoNode representing the last Point of the LineString.
     */
    GeoNode getGeoEnd();
}
