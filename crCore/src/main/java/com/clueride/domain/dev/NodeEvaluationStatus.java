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
import com.clueride.feature.Edge;

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
    private Edge proposedSegmentFromTrack;
    private GeoNode startNode;
    private GeoNode endNode;
    private Edge intersectedSegment;
    private Edge subSegmentA;
    private Edge subSegmentB;

    /**
     * @return the proposedSegmentFromTrack
     */
    public Edge getProposedSegmentFromTrack() {
        return proposedSegmentFromTrack;
    }

    /**
     * @param proposedSegmentFromTrack
     *            the proposedSegmentFromTrack to set
     */
    public void setProposedSegmentFromTrack(
            Edge proposedSegmentFromTrack) {
        this.proposedSegmentFromTrack = proposedSegmentFromTrack;
    }

    /**
     * @return the intersectedSegment
     */
    public Edge getIntersectedSegment() {
        return intersectedSegment;
    }

    /**
     * @param intersectedSegment
     *            the intersectedSegment to set
     */
    public void setIntersectedSegment(Edge intersectedSegment) {
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
    public Edge getSubSegmentA() {
        return subSegmentA;
    }

    /**
     * @return the subSegmentB
     */
    public Edge getSubSegmentB() {
        return subSegmentB;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NodeEvaluationStatus [id=").append(id).append(
                ", proposedSegmentFromTrack=").append(proposedSegmentFromTrack)
                .append(", startNode=").append(startNode).append(", endNode=")
                .append(endNode).append(", intersectedSegment=").append(
                        intersectedSegment).append(", subSegmentA=").append(
                        subSegmentA).append(", subSegmentB=").append(
                        subSegmentB).append("]");
        return builder.toString();
    }

}
