/*
 * Copyright 2015 Jett Marks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by jett on 12/28/15.
 */
package com.clueride.service;

import java.util.List;

import com.google.inject.Inject;
import org.apache.log4j.Logger;

import com.clueride.dao.NodeStore;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkProposal;
import com.clueride.domain.dev.NetworkRecommendation;
import com.clueride.domain.dev.NewLocProposal;
import com.clueride.domain.dev.rec.OnTrack;
import com.clueride.domain.dev.rec.Rec;
import com.clueride.feature.TrackFeature;
import com.clueride.service.builder.NewLocRecBuilder;
import com.clueride.service.builder.TrackRecBuilder;

/**
 * Implementation of the Recommendation Service supporting the creation
 * and evaluation of recommendations to add to the Network.
 */
public class DefaultRecommendationService implements RecommendationService {
    private static final Logger LOGGER = Logger.getLogger(DefaultRecommendationService.class);
    private final NodeStore nodeStore;
    private static int countBuildNewLocRequests;

    @Inject
    public DefaultRecommendationService(NodeStore nodeStore) {
        this.nodeStore = nodeStore;
    }

    @Override
    public NetworkProposal buildProposalForNewNode(GeoNode newNode) {
        LOGGER.debug("start - buildProposalForNewNode(): "
                + countBuildNewLocRequests++);
        GeoEval geoEval = GeoEval.getInstance();
        // TODO: CA-23 - renaming Loc to Node
        NewLocProposal newNodeProposal = new NewLocProposal(newNode);
        NewLocRecBuilder recBuilder = new NewLocRecBuilder(newNode);

        // Check if our node happens to already be on the network list of nodes
        Integer matchingNodeId = geoEval.matchesNetworkNode(newNode);
        if (matchingNodeId > 0) {
            // Found matching node; add as a recommendation
            newNodeProposal.add(recBuilder.onNode(matchingNodeId));
            return newNodeProposal;
        }

        // Check if our node happens to be on the network list of edges
        Integer matchingSegmentId = geoEval.matchesSegmentId(newNode);
        if (matchingSegmentId > 0) {
            // TODO: Missing from here: splitting the segment on the node
            newNodeProposal.add(recBuilder.onSegment(matchingSegmentId));
            return newNodeProposal;
        }

        // Try a Track-based proposal
        List<TrackFeature> coveringTracks = geoEval.listCoveringTracks(newNode);
        for (TrackFeature track : coveringTracks) {
            TrackRecBuilder trackRecBuilder = new TrackRecBuilder(newNode,
                    track);
            Rec rec = trackRecBuilder.build();
            if (rec != null) {
                newNodeProposal.add(rec);
            }
        }
        logProposal(newNodeProposal);
        return newNodeProposal;
    }

    private void logProposal(NewLocProposal newLocProposal) {
        LOGGER.info("Proposal Summary: ");
        List<NetworkRecommendation> recommendations = newLocProposal.getRecommendations();
        LOGGER.info("Total of " + recommendations.size() + " recommendations");
        for (NetworkRecommendation rec : recommendations) {
            String baseMsg = "Rec ID: " + rec.getId() +
                            " of type " + rec.getRecType() +
                            " has " + rec.getFeatureCount() + " features";
            String fullMessage = baseMsg;
            if (rec instanceof OnTrack) {
                fullMessage += "; based on Track " + ((OnTrack) rec).getSourceTrackId();
            }
            LOGGER.info( fullMessage );
        }
    }

}
