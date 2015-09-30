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

import static com.clueride.domain.dev.rec.NetworkRecType.TRACK_TO_2_SEGMENTS;

import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.Segment;

/**
 * Description.
 *
 * @author jett
 *
 */
public class ToTwoSegmentsImpl extends TrackImpl implements ToTwoSegments {
    private Segment segment1;
    private Segment segment2;

    public ToTwoSegmentsImpl(GeoNode reqNode, SimpleFeature track,
            Segment segment1, Segment segment2) {
        super(reqNode, track);
        this.segment1 = segment1;
        this.segment2 = segment2;
    }

    /**
     * @return the segment1
     */
    public Segment getSegment1() {
        return segment1;
    }

    /**
     * @return the segment2
     */
    public Segment getSegment2() {
        return segment2;
    }

    /**
     * @see com.clueride.domain.dev.rec.NetworkRecImpl#getRecType()
     */
    @Override
    public NetworkRecType getRecType() {
        return TRACK_TO_2_SEGMENTS;
    }

}
