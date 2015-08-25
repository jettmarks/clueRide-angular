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
 * Created Aug 15, 2015
 */
package com.clueride.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.dev.Node;
import com.clueride.domain.dev.NodeNetworkState;
import com.clueride.domain.dev.Segment;
import com.vividsolutions.jts.geom.Point;

/**
 * Description.
 *
 * @author jett
 *
 */
public class DefaultGeoNode implements GeoNode {
	private Point point;
	private NodeNetworkState nodeNetworkState = NodeNetworkState.UNDEFINED;
	private List<Segment> segments = new ArrayList<>();
	private List<GeoNode> nearByNodes;
	private List<SimpleFeature> tracks = new ArrayList<>();

	/**
	 * @return the point
	 */
	public Point getPoint() {
		return point;
	}

	/**
	 * @param point
	 *            the point to set
	 */
	public void setPoint(Point point) {
		this.point = point;
	}

	/**
	 * Checks that the underlying coordinates are the same for each instance.
	 * 
	 * @see com.clueride.domain.dev.Node#matchesLocation(com.clueride.domain.dev.Node)
	 */
	public boolean matchesLocation(Node node) {
		if (node instanceof DefaultGeoNode) {
			return (this.point.getCoordinate().equals2D(((GeoNode) node)
					.getPoint().getCoordinate()));
		} else {
			throw new IllegalArgumentException(
					"Expected DefaultGeoNode implementation, not "
							+ node.getClass());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultGeoNode [point=" + point + "@" + getElevation() + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.clueride.domain.dev.Node#getState()
	 */
	public NodeNetworkState getState() {
		return nodeNetworkState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.clueride.domain.dev.Node#getSegments()
	 */
	public List<Segment> getSegments() {
		return segments;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.clueride.domain.dev.Node#getTracks()
	 */
	public List<SimpleFeature> getTracks() {
		return tracks;
	}

	public Double getLat() {
		return getPoint().getY();
	};

	public Double getLon() {
		return getPoint().getX();
	};

	public Double getElevation() {
		return getPoint().getCoordinates()[0].z;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(getPoint()
				.getCoordinateSequence());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		GeoNode rhs = (GeoNode) obj;
		return (this.getLat().equals(rhs.getLat()) && this.getLon().equals(
				rhs.getLon()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.clueride.domain.dev.Node#setState(com.clueride.domain.dev.
	 * NodeNetworkState)
	 */
	public void setState(NodeNetworkState nodeNetworkState) {
		this.nodeNetworkState = nodeNetworkState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.clueride.domain.dev.Node#addSegment(com.clueride.domain.dev.Segment)
	 */
	public void addSegment(Segment segment) {
		segments.add(segment);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.clueride.domain.GeoNode#setNearbyNodes(java.util.Set)
	 */
	@Override
	public void setNearByNodes(List<GeoNode> nearestNodes) {
		nearByNodes = nearestNodes;
	}

	/**
	 * @see com.clueride.domain.GeoNode#getNearByNodes()
	 */
	@Override
	public List<GeoNode> getNearByNodes() {
		return nearByNodes;
	}

	/**
	 * @see com.clueride.domain.GeoNode#addTrack(org.opengis.feature.simple.SimpleFeature)
	 */
	@Override
	public void addTrack(SimpleFeature feature) {
		tracks.add(feature);
	}
}
