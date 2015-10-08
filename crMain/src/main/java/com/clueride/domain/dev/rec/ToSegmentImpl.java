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

import static com.clueride.domain.dev.rec.NetworkRecType.TRACK_TO_SEGMENT;

import com.clueride.domain.GeoNode;
import com.clueride.feature.Edge;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.TranslateUtil;

/**
 * Description.
 *
 * @author jett
 *
 */
public class ToSegmentImpl extends TrackImpl implements ToSegment {
    /** Existing portion of network where track intersects. */
    private Edge segment;
    /** Node on segment and track where the segment is proposed to be split. */
    private GeoNode splittingNode;

    public ToSegmentImpl(GeoNode reqNode, TrackFeature onTrack,
            Edge segment, GeoNode splittingNode) {
        super(reqNode, onTrack);
        this.segment = segment;
        addFeature(TranslateUtil.segmentToFeature(segment));
        this.splittingNode = splittingNode;
        addFeature(TranslateUtil.geoNodeToFeature(splittingNode));
    }

    /**
     * @return the segment
     */
    public Edge getSegment() {
        return segment;
    }

    /**
     * @see com.clueride.domain.dev.rec.NetworkRecImpl#getRecType()
     */
    @Override
    public NetworkRecType getRecType() {
        return TRACK_TO_SEGMENT;
    }

}
