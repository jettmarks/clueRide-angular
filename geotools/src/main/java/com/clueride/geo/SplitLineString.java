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
import com.clueride.domain.TrackFeatureImpl;
import com.clueride.domain.dev.Track;
import com.clueride.domain.dev.TrackImpl;
import com.clueride.feature.LineFeature;
import com.clueride.feature.TrackFeature;
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
    private LineFeature lineFeature;
    LineString[] lineStringPair;
    public static final int START = 0;
    public static final int END = 1;
    private static final String[] FEATURE_NAMES = {"-toStart", "-toEnd"};

    public SplitLineString(LineString track, GeoNode geoNode) {
        lineStringPair = TranslateUtil.split(track, geoNode
                .getPoint().getCoordinate(), true);
        toStart = (LineString) lineStringPair[START].reverse();
        toEnd = lineStringPair[END];
        lineStringPair[START] = toStart;
    }

    /**
     * Constructor accepting the LineFeature and the spot where we want to split
     * the line.
     * 
     * @param track
     *            - Has not only the geometry, but able to carry the Feature
     *            aspects of this geometry as well.
     * @param geoNode
     *            - The point (already verified to be on the track) where we'd
     *            like to split the track.
     */
    public SplitLineString(LineFeature track, GeoNode geoNode) {
        this(track.getLineString(), geoNode);
        this.lineFeature = track;
    }

    public LineString getSubLineString(int startOrEnd) {
        return lineStringPair[startOrEnd];
    }

    /**
     * @deprecated
     * @return
     */
    public LineString getLineStringToStart() {
        return toStart;
    }

    /**
     * @deprecated
     * @return
     */
    public LineString getLineStringToEnd() {
        return toEnd;
    }


    /**
     * Given the "parent" feature, construct a "child" feature based on that
     * parent.
     * 
     * The ID is tentative -- since we don't know here this child's future --
     * but we can propose a name derived from the parent and keep the URL to
     * serve as the source of data for this child.
     * 
     * @return - LineFeature representing the portion of the parent LineFeature
     *         that runs toward the start of the parent from the split point.
     */
    public LineFeature getLineFeatureToStart() {
        return getLineFeature("-toStart", getLineStringToStart());
    }

    /**
     * Given the "parent" feature, construct a "child" feature based on that
     * parent.
     * 
     * The ID is tentative -- since we don't know here this child's future --
     * but we can propose a name derived from the parent and keep the URL to
     * serve as the source of data for this child.
     * 
     * @return - LineFeature representing the portion of the parent LineFeature
     *         that runs toward the start of the parent from the split point.
     */
    public LineFeature getLineFeatureToEnd() {
        return getLineFeature("-toEnd", getLineStringToEnd());
    }

    /**
     * @param appendName
     * @param lineString
     * @return
     */
    private TrackFeature getLineFeature(String appendName, LineString lineString) {
        TrackFeature trackFeature = (TrackFeature) lineFeature;
        String displayName = trackFeature.getDisplayName() + appendName;
        String url = trackFeature.getUrl();
        Track childTrack = new TrackImpl(displayName, url);
        return new TrackFeatureImpl(childTrack, lineString);
    }

    public TrackFeature getSubLineFeature(int startOrEnd) {
        if (startOrEnd == START) {
            return getLineFeature(FEATURE_NAMES[startOrEnd], (LineString) getSubLineString(startOrEnd).reverse());
        } else {
            return getLineFeature(FEATURE_NAMES[startOrEnd], getSubLineString(startOrEnd));
        }
    }
}
