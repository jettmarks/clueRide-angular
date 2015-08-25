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
package com.clueride.domain;

import com.clueride.domain.dev.Node;
import com.clueride.domain.dev.Segment;
import com.clueride.domain.dev.Track;
import com.vividsolutions.jts.geom.LineString;

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
public class SegmentImpl implements Segment {
	private Track track;
	private boolean oneWay;
	private Rating rating;
	private double distanceMiles;
	private LineString lineString;

	// From Track
	private String url;

	// From LineString
	private Node startNode;
	private Node endNode;
	private String name;
	private int id;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.clueride.domain.Segment#getTrack()
	 */
	public Track getTrack() {
		return track;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.clueride.domain.Segment#setTrack(com.clueride.domain.Track)
	 */
	public void setTrack(Track track) {
		this.track = track;
	}

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
	public double getDistanceMiles() {
		return distanceMiles;
	}

	/**
	 * @param lineString
	 */
	public void setLineString(LineString lineString) {
		this.lineString = lineString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.clueride.domain.LineFeature#getId()
	 */
	public int getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.clueride.domain.LineFeature#setId(int)
	 */
	public void setId(int id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.clueride.domain.LineFeature#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.clueride.domain.LineFeature#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * From LineString.
	 * 
	 * @see com.clueride.domain.LineFeature#getStart()
	 */
	public Node getStart() {
		return startNode;
	}

	public void setStart(Node node) {
		startNode = node;
	}

	/**
	 * From LineString.
	 * 
	 * @see com.clueride.domain.LineFeature#getEnd()
	 */
	public Node getEnd() {
		return endNode;
	}

	public void setEnd(Node node) {
		endNode = node;
	}

	/** * @return */
	public LineString getLineString() {
		return lineString;
	}

	/**
	 * @see com.clueride.domain.dev.Segment#getUrl()
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @see com.clueride.domain.dev.Segment#setUrl(java.lang.String)
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
