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

    /** If constructed with one of these, we're able to create "child" tracks. */
    private TrackFeature trackFeature;
    private final LineString[] lineStringPair;

    /** When true, the getSub* methods will maintain the order of the Start portion. */
    private boolean maintainStartOrder = false;

    /** Constants representing start and end of the LineString. */
    public static final int START = 0;

    public static final int END = 1;
    private static final String[] FEATURE_NAMES = {"-toStart", "-toEnd"};
    /**
     * Accepts a LineString and location where the arc should be split into
     * two arcs.
     *
     * This constructor won't have the information required to prepare
     * LineFeatures and for that reason, those calls will thrown RuntimeExceptions
     * if the constructor {@link SplitLineString(LineFeature,GeoNode)} is not used.
     * @param lineString  representing the arc to be split.
     * @param geoNode location where the arc should be split.
     */
    public SplitLineString(LineString lineString, GeoNode geoNode) {
        lineStringPair = TranslateUtil.split(lineString, geoNode
                .getPoint().getCoordinate(), true);
        LineString toStart = (LineString) lineStringPair[START].reverse();
        lineStringPair[START] = toStart;
    }

    /**
     * Constructor accepting a TrackFeature and the spot where we want to split
     * the line.
     *
     * @param trackFeature
     *            - Has not only the geometry, but able to carry the Feature
     *            aspects of this geometry as well, in particular, the DisplayName.
     * @param geoNode
     *            - The point (already verified to be on the trackFeature) where we'd
     *            like to split the trackFeature.
     */
    public SplitLineString(TrackFeature trackFeature, GeoNode geoNode) {
        this(trackFeature.getLineString(), geoNode);
        this.trackFeature = trackFeature;
    }

    public boolean isMaintainStartOrder() {
        return maintainStartOrder;
    }

    public void setMaintainStartOrder(boolean maintainStartOrder) {
        this.maintainStartOrder = maintainStartOrder;
    }

    public LineString getSubLineString(int startOrEnd) {
        return lineStringPair[startOrEnd];
    }

    /**
     * Starting with the Track data provided during construction, create a "child"
     * feature based on that data along with the LineString provided as a parameter.
     *
     * The ID is tentative -- since we don't know here this child's future --
     * but we can propose a name derived from the parent and keep the URL to
     * serve as the source of data for this child.
     *
     * @param appendName is added to the end of the parent's name to derive the
     *                   child's name.
     * @param lineString is the new stretch of points derived from the parent.
     * @return TrackFeature which has meshed data from the parent and the LineString
     * representing the (usually) shortened portion of the parent's LineString.
     */
    private TrackFeature getTrackFeature(String appendName, LineString lineString) {
        if (trackFeature == null) {
            throw new RuntimeException("Unable to provide Track when this " +
                    "instance wasn't created using a Track");
        }
        String displayName = trackFeature.getDisplayName() + appendName;
        String url = trackFeature.getUrl();
        Track childTrack = new TrackImpl(displayName, url);
        return new TrackFeatureImpl(childTrack, lineString);
    }

    /**
     * Creates a child TrackFeature from the parent and which portion is given by the
     * pass parameter.
     *
     * Normally, the two sub tracks will diverge from the splitting node out to
     * the ends of the parent track.  In some instances however, we want to
     * maintain the original direction of the start track, in which case, we have
     * to reverse the LineString before creating the TrackFeature.
     *
     * @param startOrEnd either START or END to represent which end of the track.
     * @return TrackFeature based on parent and the substring on either the START
     * or the END side of the split.
     */
    public TrackFeature getSubTrackFeature(int startOrEnd) {
        if (startOrEnd == START && maintainStartOrder) {
            return getTrackFeature(FEATURE_NAMES[startOrEnd], (LineString) getSubLineString(startOrEnd).reverse());
        } else {
            return getTrackFeature(FEATURE_NAMES[startOrEnd], getSubLineString(startOrEnd));
        }
    }
}
