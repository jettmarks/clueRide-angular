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
 * Created Sep 27, 2015
 */
package com.clueride.domain.dev.rec;

import java.util.List;

import com.clueride.feature.TrackFeature;

/**
 * Represents New Location which is connected somehow to the Network; extensions
 * describe how this track is connected.
 *
 * @author jett
 *
 */
public interface OnTrack extends Rec {
    Integer getSourceTrackId();

    TrackFeature getSourceTrack();

    TrackFeature getProposedTrack();
    List<TrackFeature> getProposedTracks();

    void addProposedTrack(TrackFeature trackStart);
}
