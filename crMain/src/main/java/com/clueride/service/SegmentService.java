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
package com.clueride.service;

import java.io.IOException;

import org.geotools.feature.DefaultFeatureCollection;

import com.clueride.dao.DefaultNetworkStore;
import com.clueride.dao.NetworkStore;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.Segment;
import com.clueride.io.JsonStoreType;
import com.clueride.io.JsonUtil;

/**
 * Handles requests for Segments of all types.
 *
 * @author jett
 *
 */
public class SegmentService {
    private static NetworkStore networkStore = DefaultNetworkStore
            .getInstance();

    /**
     * @param raw
     * @return
     */
    public static String getFeatureCollection(JsonStoreType type) {
        String result = "";
        JsonUtil jsonUtil = new JsonUtil(type);
        try {
            DefaultFeatureCollection features = jsonUtil
                    .readFeatureCollection();
            result = jsonUtil.toString(features);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param brandNewSegment
     */
    public static void addSegment(Segment segment) {
        networkStore.addNew(segment);
    }

    /**
     * @param existingSegmentToSplit
     * @param endNode
     */
    public static void splitSegment(Segment segmentToSplit,
            GeoNode endNode) {
        networkStore.splitSegment(segmentToSplit.getSegId(), endNode);
    }

    /**
     * 
     */
    public static void saveChanges() {
        try {
            networkStore.persistAndReload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
