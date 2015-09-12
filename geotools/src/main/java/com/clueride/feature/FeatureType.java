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
 * Created Aug 16, 2015
 */
package com.clueride.feature;

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeatureType;

import com.clueride.domain.dev.NodeNetworkState;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * Support for the Feature Types used in this application.
 *
 * @author jett
 *
 */
public class FeatureType {
    public static final SimpleFeatureType SEGMENT_FEATURE_TYPE = buildSegmentInstance();
    public static final SimpleFeatureType POINT_FEATURE_TYPE = buildPointInstance();
    public static final SimpleFeatureType NODE_GROUP_FEATURE_TYPE = buildNodeGroupInstance();

    public static SimpleFeatureType buildSegmentInstance() {

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("SegmentFeatureType");
        builder.setCRS(DefaultGeographicCRS.WGS84_3D); // <- Coordinate
                                                       // reference system

        // add attributes in order
        builder.add("segId", Integer.class);
        builder.add("name", String.class);
        builder.add("url", String.class);
        builder.add("selected", Boolean.class);
        builder.add("the_geom", LineString.class);

        // build the type
        final SimpleFeatureType featureType = builder.buildFeatureType();

        return featureType;
    }

    /**
     * @return
     */
    public static SimpleFeatureType buildPointInstance() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("PointFeatureType");
        builder.setCRS(DefaultGeographicCRS.WGS84_3D); // <- Coordinate
                                                       // reference system

        // add attributes in order
        builder.add("pointId", Integer.class);
        builder.add("name", String.class);
        builder.add("state", NodeNetworkState.class);
        builder.add("selected", Boolean.class);
        builder.add("the_geom", Point.class);

        // build the type
        final SimpleFeatureType featureType = builder.buildFeatureType();

        return featureType;
    }

    /**
     * @return
     */
    public static SimpleFeatureType buildNodeGroupInstance() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("NodeGroupFeatureType");
        builder.setCRS(DefaultGeographicCRS.WGS84_3D); // <- Coordinate
                                                       // reference system

        // add attributes in order
        builder.add("nodeGroupId", Integer.class);
        builder.add("name", String.class);
        builder.add("state", String.class);
        builder.add("selected", Boolean.class);
        builder.add("radius", Double.class);
        builder.add("the_geom", Point.class);

        // build the type
        final SimpleFeatureType featureType = builder.buildFeatureType();

        return featureType;
    }
}
