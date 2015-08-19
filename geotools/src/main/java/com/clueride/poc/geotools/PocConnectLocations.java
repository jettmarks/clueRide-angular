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
package com.clueride.poc.geotools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clueride.domain.Node;
import com.clueride.poc.Network;

/**
 * Purpose of this Proof of Concept is to explore the code required to connect
 * two locations given the network (based on Segments) and a collection of
 * LineStrings based on Tracks.
 * 
 * Algorithm is roughly as follows for a given Location:
 * <OL>
 * <LI>Check to see if the Location is already intersecting with existing
 * network. If so, we're on network and nothing further to be done.
 * <LI>If Location is part of a Segment, but not at the endpoint, we will split
 * the Segment to accommodate the new Node/Location.
 * <LI>If Location is not part of any Segment, we find the closest Nodes within
 * the network as candidates for selecting from the list of Tracks. This
 * Location would be designated as off-network until a connection to the network
 * can be found.
 * <LI>For each pair formed by the Location and the network nodes, we check each
 * of the tracks to see if we have overlap with buffer. Candidate tracks are
 * displayed with the appropriate LineString connecting the two points
 * highlighted.
 * <LI>Appropriate portions of the Tracks may be selected and "committed" to the
 * network. These are added as non-rated Segments.
 * </OL>
 *
 * Services supporting these operations are what we're exploring here.
 * 
 * <UL>
 * <LI>Location Store
 * <LI>Track Store
 * <LI>Segment Store
 * </UL>
 * 
 * @author jett
 *
 */
public class PocConnectLocations {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Network network;
		TrackStore trackStore = null;
		List<Node> nodes = new ArrayList<>();
		LoadService service = LoadService.getInstance();

		network = service.loadNetwork();
		try {
			trackStore = service.loadTrackStore();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Node connectedNode = trackStore.getFirstPoint();
		// Once I get more fancy, try a point that is only *near* the network
		// Node newNode = NodeFactory.getInstance(point);
		System.out.println("Some Node on the network: " + connectedNode);
	}
}
