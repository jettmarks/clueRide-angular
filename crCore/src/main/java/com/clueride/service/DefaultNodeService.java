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
 * Created by jett on 12/13/15.
 */
package com.clueride.service;

import com.clueride.dao.NetworkProposalStore;
import com.clueride.dao.NodeStore;
import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkProposal;
import com.clueride.domain.dev.NewLocProposal;
import com.clueride.domain.dev.rec.Rec;
import com.clueride.domain.factory.PointFactory;
import com.clueride.feature.TrackFeature;
import com.clueride.service.builder.NewLocRecBuilder;
import com.clueride.service.builder.TrackRecBuilder;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Point;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Default Implementation of NodeService.
 */
public class DefaultNodeService implements NodeService {
    private static final Logger LOGGER = Logger.getLogger(DefaultNodeService.class);

    private final NodeStore nodeStore;
    private static int countBuildNewLocRequests;

    @Inject
    public DefaultNodeService(NodeStore nodeStore) {
        this.nodeStore = nodeStore;
    }

    @Override
    public Point getPointByNodeId(Integer nodeId) {
        GeoNode node = (GeoNode) nodeStore.getNodeById(nodeId);
        return node.getPoint();
    }

    @Override
    public String addNewNode(Double lat, Double lon) {
        String result = "";

        GeoNode newNode;
        newNode = getCandidateNode(lat, lon);
        NetworkProposal networkProposal = buildProposalForNewNode(newNode);
        NetworkProposalStore.add(networkProposal);
        result = networkProposal.toJson();
        return result;
    }

    @Override
    public String confirmNewNode() {
        return null;
    }

    @Override
    public String getNodeGroups() {
        return null;
    }

    @Override
    public String setNodeGroup(Integer id, Double lat, Double lon) {
        return null;
    }

    @Override
    public String showAllNodes() {
        return null;
    }
    /**
     * @param lat
     * @param lon
     * @return
     */
    private GeoNode getCandidateNode(Double lat, Double lon) {
        GeoNode newLocation;
        Point point = PointFactory.getJtsInstance(lat, lon, 0.0);
        newLocation = new DefaultGeoNode(point);
        newLocation.setName("candidate");
        return newLocation;
    }

    /**
     * Given a proposed New Location, find out how it connects to the network
     * and recommend a connection if one doesn't already exist.
     *
     * Replacing the Network.evaluatNodeState() method.
     *
     * @return
     */
    protected NetworkProposal buildProposalForNewNode(GeoNode newLoc) {
        LOGGER.debug("start - buildProposalForNewNode(): "
                + countBuildNewLocRequests++);
        GeoEval geoEval = GeoEval.getInstance();
        NewLocProposal newLocProposal = new NewLocProposal(newLoc);
        NewLocRecBuilder recBuilder = new NewLocRecBuilder(newLoc);

        // Check if our node happens to already be on the network list of nodes
        Integer matchingNodeId = geoEval.matchesNetworkNode(newLoc);
        if (matchingNodeId > 0) {
            // Found matching node; add as a recommendation
            newLocProposal.add(recBuilder.onNode(matchingNodeId));
            return newLocProposal;
        }

        // Check if our node happens to be on the network list of edges
        Integer matchingSegmentId = geoEval.matchesSegmentId(newLoc);
        if (matchingSegmentId > 0) {
            // TODO: Missing from here: splitting the segment on the node
            newLocProposal.add(recBuilder.onSegment(matchingSegmentId));
            return newLocProposal;
        }

        // Try a Track-based proposal
        List<TrackFeature> coveringTracks = geoEval.listCoveringTracks(newLoc);
        for (TrackFeature track : coveringTracks) {
            TrackRecBuilder trackRecBuilder = new TrackRecBuilder(newLoc,
                    track);
            Rec rec = trackRecBuilder.build();
            if (rec != null) {
                newLocProposal.add(rec);
            }
        }
        return newLocProposal;
    }
}
