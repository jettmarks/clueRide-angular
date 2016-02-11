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
 * Created Oct 9, 2015
 */
package com.clueride.io;

import java.util.HashMap;
import java.util.Map;

import org.opengis.feature.simple.SimpleFeatureType;

import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.DefaultNodeGroup;
import com.clueride.domain.EdgeImpl;
import com.clueride.domain.SegmentFeatureImpl;
import com.clueride.domain.TrackFeatureImpl;
import com.clueride.domain.dev.SegmentImpl;
import com.clueride.feature.FeatureType;

/**
 * Maps between the Store Types and the FeatureType (schema) used to read/write
 * that type.
 * 
 * This is what defines the relationship between what is stored in the
 * JSON-based datastore and the class that instantiates records from that store.
 *
 * @author jett
 */
public class JsonSchemaTypeMap {
    private static Map<JsonStoreType, SimpleFeatureType> schemaMap;
    private static Map<JsonStoreType, Class<?>> classMap;

    static {
        schemaMap = new HashMap<>();
        // No entry for the base, not actually holding data
        // map.put(JsonStoreType.BASE, null);
        schemaMap.put(JsonStoreType.SEGMENTS, FeatureType.SEGMENT_FEATURE_TYPE);
        schemaMap.put(JsonStoreType.RAW, FeatureType.TRACK_FEATURE_TYPE);
        schemaMap.put(JsonStoreType.EDGE, FeatureType.EDGE_FEATURE_TYPE);
        schemaMap.put(JsonStoreType.NETWORK, FeatureType.SEGMENT_FEATURE_TYPE);
        schemaMap.put(JsonStoreType.LOCATION, FeatureType.NODE_FEATURE_TYPE);
        schemaMap.put(JsonStoreType.LOCATION_GROUP,
                FeatureType.NODE_GROUP_FEATURE_TYPE);

        classMap = new HashMap<>();
        classMap.put(JsonStoreType.SEGMENTS, SegmentFeatureImpl.class);
        classMap.put(JsonStoreType.RAW, TrackFeatureImpl.class);
        classMap.put(JsonStoreType.EDGE, EdgeImpl.class);
        classMap.put(JsonStoreType.NETWORK, SegmentImpl.class);
        // TODO: Revisit these implementations
        classMap.put(JsonStoreType.LOCATION, DefaultGeoNode.class);
        classMap.put(JsonStoreType.LOCATION_GROUP, DefaultNodeGroup.class);
    }

    public static SimpleFeatureType getSchema(JsonStoreType type) {
        return schemaMap.get(type);
    }

    public static Class<?> getClass(JsonStoreType type) {
        return classMap.get(type);
    }
}
