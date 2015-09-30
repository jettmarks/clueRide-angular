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

import static com.clueride.domain.dev.rec.NetworkRecType.TRACK_TO_SEGMENT_AND_NODE;

import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.Segment;

/**
 * Description.
 *
 * @author jett
 */
public class ToSegmentAndNodeImpl extends TrackImpl implements
        ToSegmentAndNode {

    private Segment segment;
    private GeoNode node;

    public ToSegmentAndNodeImpl(GeoNode reqNode, SimpleFeature track,
            Segment segment, GeoNode node) {
        super(reqNode, track);
        this.segment = segment;
        this.node = node;
    }

    /**
     * @see com.clueride.domain.dev.NetworkRecommendation#getRecType()
     */
    @Override
    public NetworkRecType getRecType() {
        return TRACK_TO_SEGMENT_AND_NODE;
    }

    /**
     * @return the segment
     */
    public Segment getSegment() {
        return segment;
    }

    /**
     * @return the node
     */
    public GeoNode getNode() {
        return node;
    }

}
