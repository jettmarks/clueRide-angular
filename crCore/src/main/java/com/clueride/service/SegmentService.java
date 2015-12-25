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

import org.apache.log4j.Logger;
import org.geotools.feature.DefaultFeatureCollection;

import com.clueride.dao.DefaultNetworkStore;
import com.clueride.dao.NetworkStore;
import com.clueride.domain.EdgeImpl;
import com.clueride.domain.GeoNode;
import com.clueride.feature.Edge;
import com.clueride.feature.TrackFeature;
import com.clueride.io.GeoJsonUtil;
import com.clueride.io.JsonStoreType;

/**
 * Handles requests for Segments of all types.
 *
 * @author jett
 */
public class SegmentService {
    private static final Logger LOGGER = Logger.getLogger(SegmentService.class);
    private static NetworkStore networkStore = DefaultNetworkStore
            .getInstance();

    /**
     * @param type
     *            represents both the type and the location of the
     *            data to be retrieved.
     * @return
     */
    public static String getFeatureCollection(JsonStoreType type) {
        LOGGER.debug("Requesting Feature Collection for " + type);
        String result = "";
        GeoJsonUtil geoJsonUtil = new GeoJsonUtil(type);
        try {
            DefaultFeatureCollection features = geoJsonUtil
                    .readFeatureCollection();
            result = geoJsonUtil.toString(features);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param segment
     */
    public static void addSegment(Edge segment) {
        networkStore.addNew(segment);
    }

    /**
     * @param segmentToSplit
     * @param endNode
     */
    public static void splitSegment(Edge segmentToSplit,
            GeoNode endNode) {
        networkStore.splitEdge(segmentToSplit.getId(), endNode);
    }

    /**
     * 
     */
    // TODO: Need to work this out soon
    public static void saveChanges() {
//        try {
//            networkStore.persistAndReload();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void addSegment(TrackFeature proposedTrack) {
        if (proposedTrack instanceof Edge) {
            networkStore.addNew((Edge) proposedTrack);
        } else {
            networkStore.addNew(new EdgeImpl(proposedTrack.getFeature()));
        }
    }
}
