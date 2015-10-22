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
 * Created Oct 15, 2015
 */
package com.clueride.service.builder;

import com.clueride.dao.DefaultLocationStore;
import com.clueride.dao.DefaultNetworkStore;
import com.clueride.dao.LocationStore;
import com.clueride.dao.NetworkStore;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkRecommendation;
import com.clueride.domain.dev.rec.OnNode;
import com.clueride.domain.dev.rec.OnNodeImpl;
import com.clueride.domain.dev.rec.OnSegment;
import com.clueride.domain.dev.rec.OnSegmentImpl;
import com.clueride.domain.dev.rec.Rec;
import com.clueride.domain.dev.rec.ToNodeImpl;
import com.clueride.domain.dev.rec.ToSegmentAndNodeImpl;
import com.clueride.domain.dev.rec.ToSegmentImpl;
import com.clueride.domain.dev.rec.ToTwoNodesImpl;
import com.clueride.domain.dev.rec.ToTwoSegmentsImpl;
import com.clueride.feature.Edge;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.SplitLineString;
import com.clueride.geo.score.TrackConnection;
import com.clueride.service.GeoEval;

/**
 * Instantiation of Location Recommendations for NetworkProposal.
 *
 * @author jett
 *
 */
public class NewLocRecBuilder {
    private GeoNode newLoc;

    private static final LocationStore LOCATION_STORE = DefaultLocationStore
            .getInstance();
    private static final NetworkStore EDGE_STORE = DefaultNetworkStore
            .getInstance();

    /**
     * Create an instance of our factory given a GeoNode we're attempting to
     * bring into our network.
     * 
     * The supplied GeoNode is used to create appropriate sub-types of the
     * RecImpl class.
     * 
     * @param requestedGeoNode
     */
    public NewLocRecBuilder(GeoNode requestedGeoNode) {
        this.newLoc = requestedGeoNode;
    }

    public OnNode onNode(Integer nodeId) {
        return onNode((GeoNode) LOCATION_STORE.getNodeById(nodeId));
    }

    public OnNode onNode(GeoNode matchedNode) {
        return new OnNodeImpl(newLoc, matchedNode);
    }

    /**
     * @param matchedSegment
     *            - Edge representing where the New Location is sitting on the
     *            network.
     * @return OnSegment - specific NetworkRecommendation for adding a new
     *         location.
     */
    public OnSegment onSegment(Edge matchedSegment) {
        return new OnSegmentImpl(newLoc, matchedSegment);
    }

    /**
     * @param matchingSegmentId
     * @return OnSegment - specific NetworkRecommendation for adding a new
     *         location.
     */
    public NetworkRecommendation onSegment(Integer matchingSegmentId) {
        return onSegment(EDGE_STORE.getEdgeById(matchingSegmentId));
    }

    /**
     * Given a full TrackFeature and a node sitting on that track, sort out
     * whether this track provides options for getting on the network.
     * 
     * @param track
     * @param geoNode
     * @return
     */
    public Rec getTrackRec(TrackFeature track, GeoNode geoNode) {
        GeoEval geoEval = GeoEval.getInstance();
        // There are two directions we can go; calculate these first
        SplitLineString lsPair = new SplitLineString(track, geoNode);
        TrackConnection endConnection = geoEval.getTrackConnection(lsPair
                .getLineStringToEnd());
        TrackConnection startConnection = geoEval.getTrackConnection(lsPair
                .getLineStringToEnd());
        if (endConnection.isConnected() && startConnection.isConnected()) {
            // Multiple Connection
            return getMultipleTrackRec(track, endConnection, startConnection);
        } else if (endConnection.isConnected()) {
            // Just the end
            return getSingleTrackRec(track, endConnection);
        } else if (startConnection.isConnected()) {
            // Just the start
            return getSingleTrackRec(track, startConnection);
        }
        // No connection
        return null;
    }

    /**
     * Replace much of the specific newing up with the RecommendationBuilder.
     * 
     * @param track
     * @param endConnection
     * @param startConnection
     * @return
     */
    private Rec getMultipleTrackRec(TrackFeature track,
            TrackConnection endConnection, TrackConnection startConnection) {
        if (endConnection.hasEdge() && startConnection.hasEdge()) {
            // Both have Edges
            // TODO: This should be accepting the Nodes as well.
            return new ToTwoSegmentsImpl(newLoc, track,
                    endConnection.getEdge(), startConnection.getEdge());
        } else if (!endConnection.hasEdge() && !startConnection.hasEdge()) {
            // Both have Nodes
            return new ToTwoNodesImpl(newLoc, track,
                    endConnection.getGeoNode(), startConnection.getGeoNode());
        } else if (!endConnection.hasEdge() && startConnection.hasEdge()) {
            // End Node and Start Edge
            return new ToSegmentAndNodeImpl(newLoc, track, startConnection
                    .getEdge(), endConnection.getGeoNode());
        } else if (endConnection.hasEdge() && !startConnection.hasEdge()) {
            // End Edge and Start Node
            return new ToSegmentAndNodeImpl(newLoc, track, endConnection
                    .getEdge(), startConnection.getGeoNode());
        } else {
            // No other options
            return null;
        }
    }

    private Rec getSingleTrackRec(TrackFeature track, TrackConnection connection) {
        if (connection.getEdge() != null) {
            return new ToSegmentImpl(newLoc, track,
                    connection.getEdge(), connection.getGeoNode());
        } else {
            return new ToNodeImpl(newLoc, track, connection.getGeoNode());
        }
    }

}
