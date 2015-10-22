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
 * Created Oct 21, 2015
 */
package com.clueride.service.builder;

import java.util.ArrayList;
import java.util.List;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkRecommendation;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.SplitLineString;
import com.clueride.service.GeoEval;

/**
 * Given a Node and the Tracks covering that node, prepare a set of track-based
 * recommendations we can add to the NetworkProposal.
 *
 * @author jett
 *
 */
public class TrackRecBuilder {

    private final GeoNode newLoc;
    private final List<TrackFeature> coveringTracks;
    private final GeoEval geoEval;

    /**
     * @param geoNode
     * @param coveringTracks
     */
    public TrackRecBuilder(GeoNode newLoc, List<TrackFeature> coveringTracks) {
        this.newLoc = newLoc;
        this.coveringTracks = coveringTracks;
        this.geoEval = GeoEval.getInstance();
    }

    /**
     * @return
     */
    public List<NetworkRecommendation> build() {
        List<NetworkRecommendation> recList = new ArrayList<>();

        for (TrackFeature track : coveringTracks) {
            SplitLineString lsPair = new SplitLineString(track, newLoc);
            RecommendationBuilder recBuilder = new RecommendationBuilder();
            recBuilder.addToStartTrack(lsPair.getLineStringToStart())
                    .addToEndTrack(lsPair.getLineStringToEnd());
            NetworkRecommendation rec = recBuilder.build();
            // Rec rec = recBuilder.getTrackRec(track, geoNode);
            recList.add(rec);
        }

        return recList;
    }

}
