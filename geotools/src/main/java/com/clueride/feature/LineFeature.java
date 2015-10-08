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
package com.clueride.feature;

import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.MappableFeature;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

/**
 * Just as a PointFeature combines geometrical and non-geometrical properties
 * for a Node/Location, the LineFeature does the same for Segments.
 *
 * @author jett
 *
 */
public interface LineFeature extends MappableFeature {

    LineString getLineString();

    void setLineString(LineString lineString);

    Geometry getGeometry();

    SimpleFeature getFeature();

    Integer getId();

}
