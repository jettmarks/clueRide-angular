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
 * Created Jul 28, 2015
 */
package com.clueride.domain.dev;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.clueride.domain.Profile;
import com.clueride.domain.Rating;

/**
 * Portion of a Leg with identical characteristics (and thus rating) over the
 * entire length of the Segment.
 * 
 * Consists of
 * <UL>
 * <LI>a directed list of Nodes (a Track)
 * <LI>whether it can be traversed in both directions or only one direction
 * <LI>Rating
 * <LI>Distance
 * </UL>
 *
 * @author jett
 *
 */
public class SegmentImpl extends UnratedSegmentImpl implements Segment {
    /**
     * @param displayName
     * @param url
     */
    public SegmentImpl(String displayName, String url) {
        super(displayName, url);
    }

    private boolean oneWay;
    private Rating rating;
    private double distanceMiles;

    /*
     * (non-Javadoc)
     * 
     * @see com.clueride.domain.Segment#isOneWay()
     */
    public boolean isOneWay() {
        return oneWay;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.clueride.domain.Segment#setOneWay(boolean)
     */
    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.clueride.domain.Segment#getRating()
     */
    public Rating getRating() {
        return rating;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.clueride.domain.Segment#setRating(com.clueride.domain.Rating)
     */
    public void setRating(Rating rating) {
        this.rating = rating;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.clueride.domain.Segment#getDistanceMiles()
     */
    public Double getDistanceMiles() {
        return distanceMiles;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SegmentImpl [url=" + url + ", displayName=" + displayName
                + ", id=" + id
                + "]";
    }

    /**
     * @see com.clueride.domain.dev.Arc#getRating(com.clueride.domain.Profile)
     */
    public Rating getRating(Profile profile) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see com.clueride.domain.dev.Arc#getDistanceMeters()
     */
    public Double getDistanceMeters() {
        // TODO Auto-generated method stub
        return null;
    }

}
