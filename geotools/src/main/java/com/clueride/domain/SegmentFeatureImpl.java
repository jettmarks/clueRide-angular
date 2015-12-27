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
package com.clueride.domain;

import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.dev.Segment;
import com.clueride.feature.LineFeature;
import com.clueride.feature.SegmentFeature;

/**
 * Implementation which understands the specific data we save with a
 * LineString-based SimpleFeature.
 * 
 * TODO: There may be a better way with setting up a specific type that knows
 * about these guys.
 *
 * @author jett
 *
 */
public class SegmentFeatureImpl extends EdgeImpl implements
        LineFeature, Segment, SegmentFeature {

    /**
     * Constructor where the SimpleFeature has the Segment information which
     * will allow us to instantiate the Segment portion of this instance.
     * 
     * Otherwise, use {@link SegmentFeatureImpl(Segment, SimpleFeature)}.
     * 
     * @param lineStringFeature
     */
    public SegmentFeatureImpl(SimpleFeature lineStringFeature) {
        super(lineStringFeature);
    }

    /**
     * Build a Feature from the Segment details and a Geometry instance. TODO:
     * Come back to this.
     * 
     * @param segment
     * @param lineString
     *            public SegmentFeatureImpl(Segment segment, LineString
     *            lineString) {
     * 
     *            }
     */

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SegmentFeatureImpl [id=");
        builder.append(getId());
        builder.append(", name=");
        builder.append(getDisplayName());
        builder.append(", lineString=");
        builder.append(getLineString());
        builder.append("]");
        return builder.toString();
    }

    /**
     * @see com.clueride.domain.MappableFeature#getGeoJson()
     */
    @Override
    public String getGeoJson() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see com.com.clueride.feature.SegmentFeature#isOneWay()
     */
    @Override
    public boolean isOneWay() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see com.com.clueride.feature.SegmentFeature#setOneWay(boolean)
     */
    @Override
    public void setOneWay(boolean oneWay) {
        // TODO Auto-generated method stub

    }

    /**
     * @see com.com.clueride.feature.Edge#getRating()
     */
    @Override
    public Rating getRating() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see com.com.clueride.feature.Edge#setRating(com.clueride.domain.Rating)
     */
    @Override
    public void setRating(Rating rating) {
        // TODO Auto-generated method stub

    }

    /**
     * @see com.com.clueride.feature.Edge#getDistanceMiles()
     */
    @Override
    public Double getDistanceMiles() {
        // TODO Auto-generated method stub
        return 0.0;
    }

    /**
     * @see com.clueride.domain.dev.Arc#getRating(com.clueride.domain.Profile)
     */
    @Override
    public Rating getRating(Profile profile) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see com.clueride.domain.dev.Arc#getDistanceMeters()
     */
    @Override
    public Double getDistanceMeters() {
        // TODO Auto-generated method stub
        return null;
    }

}
