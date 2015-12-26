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

import java.util.Collection;
import java.util.Set;

import com.clueride.feature.LineFeature;
import com.clueride.feature.TrackFeature;

/**
 * Similar to NodeStore except the Track Store is read-only from the
 * Clue-Ride perspective; we're using other tools to create GPX files and
 * persist the JSON stores that this store brings into memory.
 *
 * @author jett
 *
 */
public interface TrackStore {
    String getStoreLocation();

    Collection<TrackFeature> getTrackFeatures();

    Set<LineFeature> getLineFeatures();

    TrackFeature getTrackById(Integer id);

    // TODO: This may not yet be incoroporated; is it necessary?
    Integer persistTrack(TrackFeature trackFeature);
}
