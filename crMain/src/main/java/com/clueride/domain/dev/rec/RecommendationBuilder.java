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
 * Created Sep 28, 2015
 */
package com.clueride.domain.dev.rec;

import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkRecommendation;
import com.clueride.domain.dev.Segment;

/**
 * Builder for a Recommendation that becomes part of a Proposal.
 *
 * Specify the various parts, and then once the parts list is complete, invoke
 * the build method to obtain an appropriately sub-classed Recommendation.
 * 
 * At this time, we're implementing {@link NetworkRecommendation}.
 * 
 * @author jett
 */
public class RecommendationBuilder {
    /** The requested node which we're making a recommendation for. */
    private GeoNode requestedNode;
    /** Existing node, if the requested node is found within tolerance. */
    private GeoNode onNetworkNode;
    private Segment onNetworkSegment;
    private SimpleFeature onTrack;

    /** */

    /**
     * This goes through a couple of layers to determine what sort of instance
     * to create:
     * <OL>
     * <LI>What sort of object connects us to the network, and if we need a
     * Track (new segment),
     * <LI>how does that Track connect us to the network.
     * </OL>
     * 
     * @return
     */
    public NetworkRecommendation build() {
        if (requestedNode == null) {
            throw new IllegalStateException(
                    "Must specify a Node for this recommendation");
        }

        if (notOnNetwork()) {
            return new OffNetworkImpl(requestedNode);
        }

        if (overSpecified()) {
            throw new IllegalStateException(
                    "Cannot specify more than one of [OnNetworkNode, OnSegmentNode, OnTrack]");
        }

        if (onNetworkNode != null) {
            return new OnNodeImpl(requestedNode, onNetworkNode);
        }

        if (onNetworkSegment != null) {
            return new OnSegmentImpl(requestedNode, onNetworkSegment);
        }

        // That takes care of the first (simpler) layer of instances; if we
        // reach this point, we've got a track-based instance.

        return null;
    }

    /**
     * @return
     */
    private boolean overSpecified() {
        int specCount = 0;
        if (onNetworkNode != null)
            specCount++;
        if (onNetworkSegment != null)
            specCount++;
        if (onTrack != null)
            specCount++;
        return (specCount > 1);
    }

    /**
     * @return
     */
    private boolean notOnNetwork() {
        return (onNetworkNode == null && onNetworkSegment == null && onTrack == null);
    }

    /**
     * @param networkNode
     */
    public RecommendationBuilder addOnNetworkNode(GeoNode networkNode) {
        this.onNetworkNode = networkNode;
        return (this);
    }

    /**
     * @param networkSegment
     * @return
     */
    public RecommendationBuilder addOnNetworkSegment(Segment networkSegment) {
        this.onNetworkSegment = networkSegment;
        return this;
    }

    /**
     * @param requestedNode2
     * @return
     */
    public RecommendationBuilder requestNetworkNode(GeoNode requestedNode) {
        this.requestedNode = requestedNode;
        return this;
    }
}
