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
 * Created Oct 18, 2015
 */
package com.clueride.dao;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.geotools.feature.DefaultFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.TrackFeatureImpl;
import com.clueride.feature.LineFeature;
import com.clueride.feature.TrackFeature;
import com.clueride.io.JsonStoreType;
import com.clueride.io.JsonUtil;

/**
 * TODO: Description.
 * 
 * TODO: Make this TestModeAware.
 *
 * @author jett
 */
public class DefaultTrackStore implements TrackStore {

    private static final Logger LOGGER = Logger
            .getLogger(DefaultTrackStore.class);

    private static DefaultTrackStore instance = null;
    private static Map<Integer, TrackFeature> trackPerId = new HashMap<>();
    private static JsonUtil jsonUtilTracks = new JsonUtil(JsonStoreType.RAW);

    public static DefaultTrackStore getInstance() {
        if (instance == null) {
            instance = new DefaultTrackStore();
        }
        return instance;
    }

    /**
     * Use {@link:getInstance()}
     */
    private DefaultTrackStore() {
        try {
            this.loadAllFeatures();
        } catch (IOException e) {
            LOGGER.error("Unable to Load", e);
        }
    }

    void loadAllFeatures() throws IOException {
        DefaultFeatureCollection features = jsonUtilTracks
                .readFeatureCollection();
        for (SimpleFeature feature : features) {
            TrackFeature trackFeature = new TrackFeatureImpl(feature);
            trackPerId.put(trackFeature.getId(), trackFeature);
        }
        LOGGER.info("Loading complete: " + this.toString());
    }

    /**
     * @see com.clueride.dao.TrackStore#getStoreLocation()
     */
    @Override
    public String getStoreLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see com.clueride.dao.TrackStore#getTrackFeatures()
     */
    @Override
    public Collection<TrackFeature> getTrackFeatures() {
        return trackPerId.values();
    }

    /**
     * @see com.clueride.dao.TrackStore#getLineFeatures()
     */
    @Override
    public Set<LineFeature> getLineFeatures() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see com.clueride.dao.TrackStore#getTrackById(java.lang.Integer)
     */
    @Override
    public TrackFeature getTrackById(Integer id) {
        return trackPerId.get(id);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DefaultTrackStore [getTrackFeatures()=").append(
                getTrackFeatures()).append("]");
        return builder.toString();
    }

}
