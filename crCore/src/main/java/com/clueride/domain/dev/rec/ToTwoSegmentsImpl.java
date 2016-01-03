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

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Objects;

import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.GeoNode;
import com.clueride.feature.Edge;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.TranslateUtil;
import static com.clueride.domain.dev.rec.NetworkRecType.TRACK_TO_2_SEGMENTS;

/**
 * When a proposed track runs through the New Node and intersects the Network on
 * both ends, and each end meets the Network at a Segment, this class captures the
 * extra information needed to add this Node and the new Segments.
 *
 * @author jett
 */
public class ToTwoSegmentsImpl extends OnTrackImpl implements ToTwoSegments {
    private Edge segmentStart;
    private Edge segmentEnd;
    private GeoNode splittingNodeStart;
    private GeoNode splittingNodeEnd;

    public ToTwoSegmentsImpl(
            GeoNode reqNode,
            TrackFeature onTrack,
            Edge segmentStart,
            Edge segmentEnd,
            DefaultGeoNode splittingNodeStart,
            DefaultGeoNode splittingNodeEnd
    ) {
        super(reqNode, onTrack);

        this.segmentStart = segmentStart;
        addFeature(segmentStart.getFeature());
        this.segmentEnd = segmentEnd;
        addFeature(segmentEnd.getFeature());

        addFeature(TranslateUtil.geoNodeToFeature(splittingNodeStart));
        addFeature(TranslateUtil.geoNodeToFeature(splittingNodeEnd));
        this.splittingNodeStart = splittingNodeStart;
        this.splittingNodeEnd = splittingNodeEnd;
    }

    @Override
    public boolean isDoubleEnded() {
        return true;
    }

    /**
     * @see com.clueride.domain.dev.rec.NetworkRecImpl#getRecType()
     */
    @Override
    public NetworkRecType getRecType() {
        return TRACK_TO_2_SEGMENTS;
    }

    @Override
    public Edge getStartSegment() {
        return segmentStart;
    }

    @Override
    public GeoNode getSplittingNodeStart() {
        return splittingNodeStart;
    }

    @Override
    public Edge getEndSegment() {
        return segmentEnd;
    }

    @Override
    public GeoNode getSplittingNodeEnd() {
        return splittingNodeEnd;
    }

    @Override
    public List<GeoNode> getNodeList() {
        return Arrays.asList(getSplittingNodeStart(),getSplittingNodeEnd());
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("segmentStart", segmentStart)
                .add("segmentEnd", segmentEnd)
                .toString();
    }
}
