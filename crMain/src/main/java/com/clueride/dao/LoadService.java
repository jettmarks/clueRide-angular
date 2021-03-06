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
 * Created Aug 18, 2015
 */
package com.clueride.dao;

import java.io.IOException;
import java.util.List;

import org.geotools.feature.DefaultFeatureCollection;

import com.clueride.feature.TrackFeature;
import com.clueride.io.GeoJsonUtil;
import com.clueride.io.JsonStoreType;
import com.clueride.poc.geotools.TrackStore;
import com.clueride.service.DefaultNetwork;
import com.clueride.service.Network;

/**
 * Description.
 *
 * @author jett
 * @deprecated - replaced by Stores.
 */
public class LoadService {

    private static LoadService instance;
    private static TrackStore trackStore = null;

    /**
     * @return
     */
    public static LoadService getInstance() {
        if (instance == null) {
            instance = new LoadService();
        }
        return instance;
    }

    /**
     * For the time being, we don't have one; building an empty one from
     * scratch.
     * 
     * @return
     * @throws IOException
     */
    public Network loadNetwork() throws IOException {
        Network network = DefaultNetwork.getInstance();

        if (trackStore == null) {
            loadTrackStore();
        }

        network.add(trackStore.getFirstFeature());
        return network;
    }

    /**
     * @param i
     * @return
     * @throws IOException
     */
    public Network loadNetwork(int index) throws IOException {
        Network network = DefaultNetwork.getInstance();

        if (trackStore == null) {
            loadTrackStore();
        }

        network.add(trackStore.getTrackPerId(index));
        return network;
    }

    /**
     * @return
     * @throws IOException
     */
    public TrackStore loadTrackStore() throws IOException {
        if (trackStore == null) {
            GeoJsonUtil geoJsonUtil = new GeoJsonUtil(JsonStoreType.RAW);
            DefaultFeatureCollection features = geoJsonUtil
                    .readFeatureCollection();
            trackStore = new TrackStore(features);
        }
        return trackStore;
    }

    /**
     * Temporary method for picking up a track for testing.
     * 
     * @return
     * @throws IOException
     */
    private TrackFeature getFirstFeature() throws IOException {
        if (trackStore == null) {
            loadTrackStore();
        }
        return trackStore.getFirstFeature();
    }

    /**
     * @param trackIds
     * @return
     * @throws IOException
     */
    public Network loadNetwork(List<Integer> trackIds) throws IOException {
        Network network = DefaultNetwork.getInstance();
        if (trackStore == null) {
            loadTrackStore();
        }
        for (Integer trackId : trackIds) {
            network.add(trackStore.getTrackPerId(trackId));
        }
        return network;
    }
}
