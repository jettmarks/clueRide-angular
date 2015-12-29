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

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Objects;

import com.clueride.domain.GeoNode;
import com.clueride.feature.TrackFeature;

/**
 * Base Implementation of the various Recommendations that involve adding
 * a new Track-based Edge to the Network.
 *
 * @author jett
 */
public class OnTrackImpl extends RecImpl implements OnTrack {

    private TrackFeature sourceTrack;
    private List<TrackFeature> proposedTracks = new ArrayList<>();

    public OnTrackImpl(GeoNode reqNode, TrackFeature sourceTrack) {
        super(reqNode);
        this.sourceTrack = sourceTrack;
        addFeature(sourceTrack.getFeature());

        for (TrackFeature proposedTrackFeature : proposedTracks) {
            proposedTrackFeature.setDisplayName("Proposed");
            addFeature(proposedTrackFeature.getFeature());
        }
    }

    /**
     * @see com.clueride.domain.dev.NetworkRecommendation#getScore()
     */
    @Override
    public Double getScore() {
        // TODO: Figure out what goes here; should come from the sourceTrack, but
        // needs to be one of the features we're running around with.
        return null;
    }

    @Override
    public int getFeatureCount() {
        // We're adding some stuff here.  Tally up the features.
        // First cut at tallying this is to look at what has been added to the featureCollection
        int featureCount = featureCollection.size();
        return featureCount;
    }

    /**
     * @see com.clueride.domain.dev.rec.OnTrack#getSourceTrackId()
     */
    @Override
    public Integer getSourceTrackId() {
        return sourceTrack.getId();
    }

    /**
     * @see com.clueride.domain.dev.rec.OnTrack#getSourceTrack()
     */
    @Override
    public TrackFeature getSourceTrack() {
        return sourceTrack;
    }

    @Override
    public TrackFeature getProposedTrack() {
        return proposedTracks.get(0);
    }

    @Override
    public List<TrackFeature> getProposedTracks() {
        return proposedTracks;
    }

    @Override
    public void addProposedTrack(TrackFeature proposedTrack) {
        proposedTracks.add(proposedTrack);
        featureCollection.add(proposedTrack.getFeature());
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("sourceTrack", sourceTrack)
                .add("proposedTracks", proposedTracks)
                .add("score", getScore())
                .add("sourceTrackId", getSourceTrackId())
                .add("proposedTrack", getProposedTrack())
                .toString();
    }
}
