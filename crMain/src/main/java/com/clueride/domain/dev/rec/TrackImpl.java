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
 * Created Sep 29, 2015
 */
package com.clueride.domain.dev.rec;

import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.GeoNode;

/**
 * Description.
 *
 * @author jett
 *
 */
public class TrackImpl extends RecImpl implements OnTrack {

    private SimpleFeature track;

    public TrackImpl(GeoNode reqNode, SimpleFeature track) {
        super(reqNode);
        this.track = track;
    }

    /**
     * @see com.clueride.domain.dev.NetworkRecommendation#getScore()
     */
    @Override
    public Double getScore() {
        // TODO: Figure out what goes here; should come from the track, but
        // needs to be one of the features we're running around with.
        return null;
    }

    /**
     * @see com.clueride.domain.dev.rec.OnTrack#getTrackId()
     */
    @Override
    public Integer getTrackId() {
        // TODO: Ditto
        return null;
    }

    /**
     * @see com.clueride.domain.dev.rec.OnTrack#getTrack()
     */
    @Override
    public SimpleFeature getTrack() {
        return track;
    }

}
