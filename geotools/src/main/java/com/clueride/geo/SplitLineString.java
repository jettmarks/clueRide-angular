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
 * Created Sep 21, 2015
 */
package com.clueride.geo;

import com.clueride.domain.GeoNode;
import com.clueride.feature.LineFeature;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.LineString;

/**
 * Instantiated using a LineString and a GeoNode we understand lies on that path
 * and turns it into a pair of LineStrings that are each ordered from the
 * GeoNode to each end, the start and the end.
 *
 * @author jett
 *
 */
public final class SplitLineString {

    private final LineString toStart, toEnd;

    @Inject
    public SplitLineString(LineString track, GeoNode geoNode) {
        LineString[] lineStringPair = TranslateUtil.split(track, geoNode
                .getPoint().getCoordinate(), true);
        toStart = (LineString) lineStringPair[0].reverse();
        toEnd = lineStringPair[1];
    }

    /**
     * @param track
     * @param geoNode
     */
    public SplitLineString(LineFeature track, GeoNode geoNode) {
        this(track.getLineString(), geoNode);
    }

    public LineString getLineStringToStart() {
        return toStart;
    }

    public LineString getLineStringToEnd() {
        return toEnd;
    }
}
