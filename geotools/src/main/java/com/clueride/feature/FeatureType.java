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

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeatureType;

import com.clueride.domain.dev.NodeNetworkState;

/**
 * Support for the Feature Types used in this application.
 * 
 * NOTE: The string "id" is not recognized when it occurs in the JSON file; it
 * behaves like it is an undocumented "reserved" word and is silently ignored.
 *
 * @author jett
 *
 */
public class FeatureType {
    public static final SimpleFeatureType EDGE_FEATURE_TYPE = buildEdgeInstance();
    public static final SimpleFeatureType SEGMENT_FEATURE_TYPE = buildSegmentInstance();
    public static final SimpleFeatureType NODE_FEATURE_TYPE = buildNodeInstance();
    public static final SimpleFeatureType NODE_GROUP_FEATURE_TYPE = buildNodeGroupInstance();
    public static final SimpleFeatureType TRACK_FEATURE_TYPE = buildTrackInstance();
    public static final SimpleFeatureType PATH_FEATURE_TYPE = buildPathInstance();

    public static SimpleFeatureType buildEdgeInstance() {

        String featureTypeName = "EdgeFeatureType";
        SimpleFeatureTypeBuilder builder = prepareBuilder(featureTypeName);

        // add attributes in order
        builder.add("edgeId", Integer.class);
        builder.add("name", String.class);
        builder.add("url", String.class);
        builder.add("selected", Boolean.class);
        builder.add("the_geom", LineString.class);

        // build the type
        return builder.buildFeatureType();
    }

    public static SimpleFeatureType buildSegmentInstance() {

        SimpleFeatureTypeBuilder builder = prepareBuilder("SegmentFeatureType");

        // add attributes in order
        builder.add("segId", Integer.class);
        builder.add("name", String.class);
        builder.add("url", String.class);
        builder.add("selected", Boolean.class);
        builder.add("the_geom", LineString.class);

        // build the type
        return builder.buildFeatureType();
    }

    public static SimpleFeatureType buildNodeInstance() {
        SimpleFeatureTypeBuilder builder = prepareBuilder("PointFeatureType");

        // add attributes in order
        builder.add("pointId", Integer.class);
        builder.add("name", String.class);
        builder.add("state", NodeNetworkState.class);
        builder.add("selected", Boolean.class);
        builder.add("the_geom", Point.class);

        // build the type
        return builder.buildFeatureType();
    }

    private static SimpleFeatureType buildPathInstance() {
        SimpleFeatureTypeBuilder builder = prepareBuilder("PathFeatureType");

        // add attributes in order
        builder.add("pathId", Integer.class);
        builder.add("startNodeId", Integer.class);
        builder.add("endNodeId", Integer.class);
        builder.add("startLocationId", Integer.class);
        builder.add("endLocationId", Integer.class);
        builder.add("the_geom", Point.class);

        // build the type
        return builder.buildFeatureType();
    }

    public static SimpleFeatureType buildNodeGroupInstance() {
        SimpleFeatureTypeBuilder builder = prepareBuilder("NodeGroupFeatureType");

        // add attributes in order
        builder.add("nodeGroupId", Integer.class);
        builder.add("name", String.class);
        builder.add("state", String.class);
        builder.add("selected", Boolean.class);
        builder.add("radius", Double.class);
        builder.add("the_geom", Point.class);

        // build the type
        return builder.buildFeatureType();
    }

    public static SimpleFeatureType buildTrackInstance() {
        SimpleFeatureTypeBuilder builder = prepareBuilder("TrackFeatureType");

        // add attributes in order
        builder.add("trackId", Integer.class);
        builder.add("name", String.class);
        builder.add("url", String.class);
        builder.add("the_geom", LineString.class);

        // build the type
        return builder.buildFeatureType();
    }

    public static SimpleFeatureTypeBuilder prepareBuilder(String featureTypeName) {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(featureTypeName);
        builder.setNamespaceURI("http://clueride.com/");
        builder.setCRS(DefaultGeographicCRS.WGS84_3D); // <- Coordinate reference system
        return builder;
    }
}
