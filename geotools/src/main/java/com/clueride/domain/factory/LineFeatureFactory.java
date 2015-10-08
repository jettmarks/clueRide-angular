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
 * Created Aug 23, 2015
 */
package com.clueride.domain.factory;

import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.SegmentFeatureImpl;
import com.clueride.domain.EdgeImpl;
import com.clueride.domain.dev.TrackImpl;
import com.clueride.feature.Edge;
import com.clueride.feature.LineFeature;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

/**
 * Takes a SimpleFeature which is wrapping a LineString geometry, and turns it
 * into an implementation of the LineFeature interface.
 *
 * @author jett
 *
 */
public class LineFeatureFactory {

    public static LineFeature getInstance(SimpleFeature simpleLineStringFeature) {
        return new SegmentFeatureImpl(simpleLineStringFeature);
    }

    /**
     * When we have a Geometry that hasn't been checked for instanceof
     * LineString, this will check before preparing a "Proposed" segment.
     * 
     * @param geometry
     * @return
     */
    public static LineFeature getInstance(Geometry geometry) {
        if (!(geometry instanceof LineString)) {
            throw new IllegalArgumentException(
                    "Expected geometry of type LineString instead of "
                            + geometry.getClass().getName());
        }
        return getProposal((LineString) geometry);
    }

    /**
     * @param lsProposalForSegment
     * @return
     */
    public static Edge getProposal(
            LineString lineString) {

        TrackImpl emptyTrackImpl = new TrackImpl("Unnamed - new", "undefined");
        return (Edge) new EdgeImpl(emptyTrackImpl, lineString);
    }

}
