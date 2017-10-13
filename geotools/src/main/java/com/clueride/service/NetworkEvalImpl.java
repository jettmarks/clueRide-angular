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

import javax.inject.Inject;

import com.clueride.dao.NetworkStore;
import com.clueride.dao.NodeStore;
import com.clueride.domain.dev.Node;
import com.clueride.domain.user.path.Path;
import com.clueride.feature.Edge;
import static java.util.Objects.requireNonNull;

/**
 * Implementation of NetworkEval using in memory copies of the current network.
 */
public class NetworkEvalImpl implements NetworkEval {
    private final NodeStore nodeStore;
    private final NetworkStore networkStore;

    @Inject
    public NetworkEvalImpl(
            NodeStore nodeStore,
            NetworkStore networkStore
    ) {
        this.nodeStore = nodeStore;
        this.networkStore = networkStore;
    }

    @Override
    public void checkPathEdgesFromStartToEnd(Path path) {
        Integer walkingNodeId = path.getStartNodeId();
        // These checks may get pushed elsewhere
        Node startNode = nodeStore.getNodeById(walkingNodeId);
        requireNonNull(startNode);
        Node endNode = nodeStore.getNodeById(path.getEndNodeId());
        requireNonNull(endNode);

        for (Integer edgeId : path.getEdgeIds()) {
            Edge edge = networkStore.getEdgeById(edgeId);
            if (edge == null) {
                String message = "Missing Edge (" + edgeId + ") in path (" + path.getId() + ")";
                System.err.println(message);
                throw new IllegalStateException(message);
            }
            if (walkingNodeId.equals(edge.getStart().getId())) {
                walkingNodeId = edge.getEnd().getId();
            } else if (walkingNodeId.equals(edge.getEnd().getId())) {
                walkingNodeId = edge.getStart().getId();
            } else {
                String message = "Gap found in path " + path.getId()
                                + ": Last matching Node: " + walkingNodeId
                                + ": Edge ID " + edgeId + " not connected";
                System.err.println(message);
                throw new IllegalStateException(message);
            }
        }
    }
}
