/*
 * Copyright 2016 Jett Marks
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
 * Created by jett on 1/16/16.
 */
package com.clueride.service;

import java.util.Set;

import com.vividsolutions.jts.geom.Point;

import com.clueride.config.GeoProperties;
import com.clueride.dao.DefaultNodeStore;
import com.clueride.dao.NodeStore;
import com.clueride.domain.DefaultNodeGroup;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.Node;
import com.clueride.domain.dev.NodeGroup;
import com.clueride.exception.UnmatchedPointException;

/**
 * Based on Timing of when the stores can be loaded, this Eval will only request
 * that the Nodes be loaded into the Store prior to instantiation.
 */
public class NodeEval {

    private static final NodeStore NODE_STORE = DefaultNodeStore.getInstance();
    private static final Double LOC_GROUP_RADIUS_DEG = (Double) GeoProperties
            .getInstance().get("group.radius.degrees");

    /** Hide the default constructor; use {@link #getInstance()}. */
    private NodeEval() {
    }

    public static NodeEval getInstance() {
        return new NodeEval();
    }

    /**
     * Given a GeoNode, find the Integer ID of a defined Node that matches the position.
     * @param geoNode - GeoNode containing the Point of interest.
     * @return Integer representing the Node ID of a node if found, and -1 otherwise.  Valid
     * Node IDs are positive.
     */
    public Integer matchesNetworkNodeId(GeoNode geoNode) {
        Point point = geoNode.getPoint();
        return getMatchingNodeId(point);
    }

    public Integer getMatchingNodeId(Point point) {
        // Check Network Nodes first
        Set<GeoNode> nodeSet = NODE_STORE.getNodes();
        for (GeoNode node : nodeSet) {
            if (node.getPoint().buffer(GeoProperties.NODE_TOLERANCE).covers(
                    point)) {
                return node.getId();
            }
        }

        // Exhausted the Network Nodes, check the location groups
        Set<NodeGroup> locGroups = NODE_STORE.getNodeGroups();
        for (NodeGroup nodeGroup : locGroups) {
            Point checkPoint = ((DefaultNodeGroup) nodeGroup).getPoint();
            if (checkPoint.buffer(LOC_GROUP_RADIUS_DEG).covers(point)) {
                return nodeGroup.getId();
            }
        }

        // Nothing matched; give up and return Node ID indicating no match
        return -1;
    }
    /**
     * Given a Point, find the defined Node that matches the location.
     * @param point - Lat/Lng of the location to be checked.
     * @return Node from the NodeStore matching that location.
     */
    public Node getMatchingNode(Point point) {
        Integer nodeId = getMatchingNodeId(point);
        if (nodeId > 0) {
            return NODE_STORE.getNodeById(nodeId);
        } else {
            throw new UnmatchedPointException("Point not found amongst Nodes: " + point );
        }
    }

}
