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
 * Created Sep 17, 2015
 */
package com.clueride.domain.dev;

import com.clueride.domain.GeoNode;

/**
 * Holds information about the proposed new node along with the data presented
 * to the user and the data that is needed to create the new portions of the
 * network to accommodate the new node.
 *
 * First stab will hold the following for the case where an off-network node has
 * a proposed segment (originating from a suitable track), the two ends of that
 * segment which both become new nodes in the network, the existing network
 * segment where the track intersects/crosses the network, and then the split
 * segments if the new node happens to split the segment into two sub-segments.
 * 
 * @author jett
 */
public class NodeEvaluationStatus {
    private Integer id = NodeEvaluationStatusIdentifier.getNextId();
    private Segment proposedSegmentFromTrack;
    private GeoNode startNode;
    private GeoNode endNode;
    private Segment intersectedSegment;
    private Segment subSegmentA;
    private Segment subSegmentB;

    /**
     * @return the proposedSegmentFromTrack
     */
    public Segment getProposedSegmentFromTrack() {
        return proposedSegmentFromTrack;
    }

    /**
     * @param proposedSegmentFromTrack
     *            the proposedSegmentFromTrack to set
     */
    public void setProposedSegmentFromTrack(Segment proposedSegmentFromTrack) {
        this.proposedSegmentFromTrack = proposedSegmentFromTrack;
    }

    /**
     * @return the intersectedSegment
     */
    public Segment getIntersectedSegment() {
        return intersectedSegment;
    }

    /**
     * @param intersectedSegment
     *            the intersectedSegment to set
     */
    public void setIntersectedSegment(Segment intersectedSegment) {
        this.intersectedSegment = intersectedSegment;
    }

    /**
     * @return the startNode
     */
    public GeoNode getStartNode() {
        return startNode;
    }

    /**
     * @return the endNode
     */
    public GeoNode getEndNode() {
        return endNode;
    }

    /**
     * @return the subSegmentA
     */
    public Segment getSubSegmentA() {
        return subSegmentA;
    }

    /**
     * @return the subSegmentB
     */
    public Segment getSubSegmentB() {
        return subSegmentB;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

}
