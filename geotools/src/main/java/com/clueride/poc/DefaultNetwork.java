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
 * Created Aug 18, 2015
 */
package com.clueride.poc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.geotools.feature.DefaultFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NodeNetworkState;
import com.clueride.domain.factory.NodeFactory;
import com.clueride.geo.TranslateUtil;
import com.clueride.poc.geotools.LoadService;
import com.clueride.poc.geotools.TrackStore;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

/**
 * Holds collection of segments that make up the full set of connected Features
 * that we know about for a given area.
 *
 * Memory copy is a FeatureCollection of LineString (Segments).
 * 
 * @author jett
 *
 */
public class DefaultNetwork implements Network {
	DefaultFeatureCollection allSegments;
	List<LineString> allLineStrings = new ArrayList<>();
	private Set<GeoNode> nodeSet;
	TrackStore trackStore;

	/**
	 * @param defaultFeatureCollection
	 */
	@Inject
	public DefaultNetwork(DefaultFeatureCollection defaultFeatureCollection) {
		allSegments = defaultFeatureCollection;
		init();
		try {
			trackStore = LoadService.getInstance().loadTrackStore();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void init() {
		nodeSet = new HashSet<>();
		allLineStrings = new ArrayList<>();
		for (SimpleFeature feature : allSegments) {
			LineString lineString = (LineString) feature.getDefaultGeometry();
			allLineStrings.add(lineString);
			nodeSet.add(NodeFactory.getInstance(lineString.getStartPoint()));
			nodeSet.add(NodeFactory.getInstance(lineString.getEndPoint()));
		}
	}

	private void refresh() {
		nodeSet.clear();
		nodeSet = null;
		allLineStrings.clear();
		allLineStrings = null;
		init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.clueride.poc.Network#canReach(com.clueride.domain.DefaultGeoNode)
	 */
	public boolean canReach(GeoNode connectedNode) {
		for (LineString lineString : allLineStrings) {
			if (lineString.buffer(0.00001).covers(connectedNode.getPoint())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * After having found this node is not on the network or a segment, we check
	 * the TrackStore for any tracks that cover both this point and the nearest
	 * nodes on the network.
	 * 
	 * Those are then added to the node.
	 * 
	 * @param geoNode
	 */
	private int findMatchingTracks(GeoNode geoNode) {
		// Find tracks covering the input node
		List<SimpleFeature> candidateTracks = new ArrayList<>();
		for (SimpleFeature feature : trackStore.getFeatures()) {
			LineString lineString = (LineString) feature.getDefaultGeometry();
			if (lineString.buffer(0.00001).covers(geoNode.getPoint())) {
				candidateTracks.add(feature);
			}
		}

		// Out of the covering tracks, which pass through our nearest nodes?
		for (SimpleFeature feature : candidateTracks) {
			boolean keepTrack = false;
			LineString lineString = (LineString) feature.getDefaultGeometry();
			// Check our list of nodes
			for (GeoNode node : geoNode.getNearByNodes()) {
				if (lineString.buffer(0.00001).covers(node.getPoint())) {
					keepTrack = true;
				}
			}
			// Check our list of Segments for intersections
			for (LineString segmentLineString : allLineStrings) {
				System.out.println("Checking " + feature.getAttribute("url")
						+ " against Segment of length "
						+ segmentLineString.getNumPoints());
				if (segmentLineString.crosses(lineString)
						|| segmentLineString.intersects(lineString)) {
					keepTrack = true;
				}
			}
			if (keepTrack) {
				geoNode.addTrack(feature);
			}
		}
		return geoNode.getTracks().size();
	}

	public boolean matchesNetworkNode(GeoNode geoNode) {
		return nodeSet.contains(geoNode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.clueride.poc.Network#add(org.opengis.feature.simple.SimpleFeature)
	 */
	@Override
	public void add(SimpleFeature simpleFeature) {
		allSegments.add(simpleFeature);
		refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.clueride.poc.Network#evaluateState(com.clueride.domain.Node)
	 */
	@Override
	public NodeNetworkState evaluateNodeState(GeoNode geoNode) {
		if (matchesNetworkNode(geoNode)) {
			geoNode.setState(NodeNetworkState.ON_NETWORK);
			return NodeNetworkState.ON_NETWORK;
		} else if (addCoveringSegments(geoNode)) {
			geoNode.setState(NodeNetworkState.ON_SEGMENT);
			return NodeNetworkState.ON_SEGMENT;
		} else {
			addNearestNodes(geoNode);
			int tracksFound = findMatchingTracks(geoNode);
			if (tracksFound == 1) {
				geoNode.setState(NodeNetworkState.ON_SINGLE_TRACK);
			} else if (tracksFound > 1) {
				geoNode.setState(NodeNetworkState.ON_MULTI_TRACK);
			} else {
				geoNode.setState(NodeNetworkState.OFF_NETWORK);
			}
		}
		return geoNode.getState();
	}

	/**
	 * Adds a "handful" of the nearest nodes currently on the network.
	 * 
	 * Here's where some exploring will occur:
	 * 
	 * <UL>
	 * <LI>Do we sort the list of nodes and then pick a few from the top?
	 * <LI>Do we apply a filter with a given distance?
	 * <LI>Other options?
	 * </UL>
	 * 
	 * We'll also be interested in a similar algorithm with Tracks and the
	 * intersection of those tracks with nearby segments containing the nodes.
	 * 
	 * @param geoNode
	 */
	private void addNearestNodes(GeoNode geoNode) {
		List<GeoNode> nearestNodes = new ArrayList<>();
		// Trying to sort the nodes
		List<GeoNode> orderedNodes = getSortedNodes(geoNode);
		geoNode.setNearByNodes(nearestNodes);
	}

	/**
	 * @param geoNode
	 * @return
	 */
	public List<GeoNode> getSortedNodes(GeoNode geoNode) {
		final Geometry referencePoint = geoNode.getPoint();
		List<GeoNode> sorted = new ArrayList<>();
		sorted.addAll(nodeSet);
		Collections.sort(sorted, new Comparator<GeoNode>() {

			@Override
			public int compare(GeoNode o1, GeoNode o2) {
				double dist1 = o1.getPoint().distance(referencePoint);
				double dist2 = o2.getPoint().distance(referencePoint);
				return (int) Math.signum(dist1 - dist2);
			}

		});

		return sorted;
	}

	/**
	 * After checking that this node is not already in the Network, we check
	 * existing segments to see if any of them cover the node.
	 * 
	 * @param geoNode
	 * @return true if we were able to find suitable segments.
	 */
	private boolean addCoveringSegments(GeoNode geoNode) {
		boolean result = false;
		for (SimpleFeature feature : allSegments) {

			LineString lineString = (LineString) feature.getDefaultGeometry();
			if (lineString.buffer(0.00001).covers(geoNode.getPoint())) {
				geoNode.addSegment(TranslateUtil.featureToSegment(feature));
				result = true;
			}
		}
		return result;
	}
}
