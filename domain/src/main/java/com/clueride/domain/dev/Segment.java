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
 * Created Jul 29, 2015
 */
package com.clueride.domain.dev;

import com.clueride.domain.LineFeature;
import com.clueride.domain.Rating;

/**
 * This supports Paths and potential paths, by representing the string of
 * Lat/Lon pairs that correspond to following trails, roads, cut-throughs, or
 * whatever to get from one point to another.
 * 
 * That Segment will have a Facility Type which is scored according to certain
 * characteristics of that Facility. Essentially, the score is used to calculate
 * preferences of one Path over another between two Locations/Nodes.
 *
 * @author jett
 *
 */
public interface Segment extends LineFeature {

	/**
	 * @return the track
	 */
	public abstract Track getTrack();

	/**
	 * @param track
	 *            the track to set
	 */
	public abstract void setTrack(Track track);

	/**
	 * @return the oneWay
	 */
	public abstract boolean isOneWay();

	/**
	 * @param oneWay
	 *            the oneWay to set
	 */
	public abstract void setOneWay(boolean oneWay);

	/**
	 * @return the rating
	 */
	public abstract Rating getRating();

	/**
	 * @param rating
	 *            the rating to set
	 */
	public abstract void setRating(Rating rating);

	/**
	 * @return the distanceMiles
	 */
	public abstract double getDistanceMiles();

	/**
	 * @return
	 */
	public abstract String getUrl();

	public abstract void setUrl(String url);

}