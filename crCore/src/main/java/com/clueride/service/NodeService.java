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

import com.vividsolutions.jts.geom.Point;

/**
 * Handles business logic around Nodes.
 */
public interface NodeService {

    /**
     * Given a Node's ID, return the point associated with that Node.
     * @param nodeId - Integer unique ID for the Node.
     * @return Point with the coordinates of the Node's coordinates.
     */
    Point getPointByNodeId(Integer nodeId);

    /**
     * Adding a candidate Node to the network which has yet been evaluated
     * against the network.
     *
     * @param lat
     * @param lon
     * @return
     */
    String addNewNode(Double lat, Double lon);

    /**
     * Accept the current Proposal.
     * @return
     */
    String confirmNewNode();

    String getNodeGroups();

    String setNodeGroup(Integer id, Double lat, Double lon);

    String showAllNodes();
}
