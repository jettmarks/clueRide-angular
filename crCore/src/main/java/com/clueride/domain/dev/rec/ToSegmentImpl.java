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

import java.util.Collections;
import java.util.List;

import com.google.common.base.Objects;

import com.clueride.domain.GeoNode;
import com.clueride.feature.Edge;
import com.clueride.feature.LineFeature;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.TranslateUtil;
import static com.clueride.domain.dev.rec.NetworkRecType.TRACK_TO_SEGMENT;

/**
 * Description.
 *
 * @author jett
 *
 */
public class ToSegmentImpl extends OnTrackImpl implements ToSegment {
    /** Existing portion of network where track intersects. */
    private Edge segment;
    /** Node on segment and track where the segment is proposed to be split. */
    private GeoNode splittingNode;

    /**
     * All the pieces for this recommendation come into this constructor.
     *
     * Note that the existing Network Edge will need to be split at the
     * splittingNode and that two new segments are created which replace
     * the original segment.
     *
     * @param reqNode
     *            - Node for which we're preparing the Recommendation.
     * @param track
     *            - TrackFeature which brings us to the network.
     * @param edge
     *            - Network Edge where we'll be reaching the network.
     * @param splittingNode
     *            - Node at which the track connects to the Edge.
     */
    public ToSegmentImpl(
            GeoNode reqNode,
            LineFeature track,
            Edge edge,
            GeoNode splittingNode
    ) {
        super(reqNode, (TrackFeature) track);
        this.segment = edge;
        addFeature(edge.getFeature());
        this.splittingNode = splittingNode;
        addFeature(TranslateUtil.geoNodeToFeature(splittingNode));
    }

    /**
     * @return the segment
     */
    public Edge getSegment() {
        return segment;
    }

    @Override
    public GeoNode getSplittingNode() {
        return splittingNode;
    }

    /**
     * @see com.clueride.domain.dev.rec.NetworkRecImpl#getRecType()
     */
    @Override
    public NetworkRecType getRecType() {
        return TRACK_TO_SEGMENT;
    }

    @Override
    public List<GeoNode> getNodeList() {
        return Collections.singletonList(getSplittingNode());
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("segment", segment)
                .add("splittingNode", splittingNode)
                .toString();
    }

}
