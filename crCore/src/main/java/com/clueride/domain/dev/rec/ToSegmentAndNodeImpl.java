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

import com.google.common.base.Objects;

import com.clueride.domain.EdgeImpl;
import com.clueride.domain.GeoNode;
import com.clueride.feature.Edge;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.TranslateUtil;
import static com.clueride.domain.dev.rec.NetworkRecType.TRACK_TO_SEGMENT_AND_NODE;

/**
 * Track Recommendation where one end of the Track hits a Network Node and the other
 * end of the Track hits a Network Segment at the given Splitting Node.
 *
 * @author jett
 */
public class ToSegmentAndNodeImpl extends OnTrackImpl implements
        ToSegmentAndNode {

    private Edge edge;
    private GeoNode networkNode;
    private GeoNode splittingNode;

    /**
     * Constructor accepting the newly requested Node along with a Track that reaches
     * the Network in both directions, one direction leads to a Network Node, and the
     * other direction leads to a Network Edge/Segment.
     *
     * @param reqNode - Node to be added to the Network.
     * @param track - Track that connects on both ends to the Network.
     * @param edge - A Network Edge/Segment where one end of the Track meets Network.
     * @param splittingNode - Node at which the Track meets the Network Edge.
     * @param networkNode - An existing Network Node where the other end of the Track meets
     *                    the Network.
     */
    public ToSegmentAndNodeImpl(
            GeoNode reqNode,
            TrackFeature track,
            Edge edge,
            GeoNode splittingNode,
            GeoNode networkNode
    ) {
        super(reqNode, track);
        this.edge = edge;
        addFeature(((EdgeImpl) edge).getFeature());
        addFeature(TranslateUtil.geoNodeToFeature(splittingNode));
        this.splittingNode = splittingNode;
        this.networkNode = networkNode;
        addFeature(TranslateUtil.geoNodeToFeature(networkNode));
    }

    /**
     * @see com.clueride.domain.dev.NetworkRecommendation#getRecType()
     */
    @Override
    public NetworkRecType getRecType() {
        return TRACK_TO_SEGMENT_AND_NODE;
    }

    /**
     * @return the networkNode
     */
    public GeoNode getNetworkNode() {
        return networkNode;
    }

    /**
     * @return the edge
     */
    public Edge getSegment() {
        return edge;
    }

    @Override
    public GeoNode getSplittingNode() {
        return splittingNode;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("edge", edge)
                .add("networkNode", networkNode)
                .toString();
    }
}
