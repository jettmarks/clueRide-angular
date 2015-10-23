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

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.rec.Rec;
import com.clueride.feature.Edge;
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
    private final GeoEval geoEval;
    private final SplitLineString splitLineString;

    private GeoNode networkNodeStart;
    private GeoNode networkNodeEnd;
    private Edge networkEdgeEnd;
    private Edge networkEdgeStart;

    /**
     * @param geoNode
     * @param coveringTracks
     */
    public TrackRecBuilder(GeoNode newLoc, SplitLineString splitLineString) {
        this.newLoc = newLoc;
        this.splitLineString = splitLineString;
        this.geoEval = GeoEval.getInstance();
    }

    // /**
    // * @return
    // */
    // public List<NetworkRecommendation> build() {
    // List<NetworkRecommendation> recList = new ArrayList<>();
    //
    // for (TrackFeature track : coveringTracks) {
    // SplitLineString lsPair = new SplitLineString(track, newLoc);
    // RecommendationBuilder recBuilder = new RecommendationBuilder();
    // recBuilder.addToStartTrack(lsPair.getLineStringToStart())
    // .addToEndTrack(lsPair.getLineStringToEnd());
    // NetworkRecommendation rec = recBuilder.build();
    // // Rec rec = recBuilder.getTrackRec(track, geoNode);
    // recList.add(rec);
    // }
    //
    // return recList;
    // }

    /**
     * Accepts a Node in the Network that we have verified is covered by the
     * "To Start" portion of the Track we're building a recommendation for.
     * 
     * Based on this Node (if not null), we can calculate the Edge suggested by
     * the Track that runs from the newLoc and this potential Network Node. If
     * we get a closer Segment, we would recommend that instead.
     * 
     * @param networkNode
     *            - represents shortest connection of the Track to the Network.
     * @return this - to allow chaining of the builder.
     */
    public TrackRecBuilder addNetworkNodeStart(GeoNode networkNode) {
        // Record this now, not yet prepared to calculate distances or Edges.
        this.networkNodeStart = networkNode;
        return this;
    }

    /**
     * Accepts a Node in the Network that we have verified is covered by the
     * "To End" portion of the Track we're building a recommendation for.
     * 
     * Based on this Node (if not null), we can calculate the Edge suggested by
     * the Track that runs from the newLoc and this potential Network Node. If
     * we get a closer Segment, we would recommend that instead.
     * 
     * @param networkNode
     *            - represents shortest connection of the Track to the Network.
     * @return this - to allow chaining of the builder.
     */
    public TrackRecBuilder addNetworkNodeEnd(GeoNode networkNode) {
        // Record this now, not yet prepared to calculate distances or Edges.
        this.networkNodeEnd = networkNode;
        return this;
    }

    /**
     * @param nearestNetworkEdge
     * @return
     */
    public TrackRecBuilder addEdgeAtStart(Edge networkEdge) {
        // Record this now, not yet prepared to calculate distances or Edges.
        this.networkEdgeStart = networkEdge;
        return this;
    }

    /**
     * @param nearestNetworkEdge
     */
    public TrackRecBuilder addEdgeAtEnd(Edge networkEdge) {
        // Record this now, not yet prepared to calculate distances or Edges.
        this.networkEdgeEnd = networkEdge;
        return this;
    }

    /**
     * @return
     */
    public Rec build() {
        // TODO Auto-generated method stub
        return null;
    }

}
