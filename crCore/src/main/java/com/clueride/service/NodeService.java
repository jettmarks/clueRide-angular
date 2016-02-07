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
     * @param lat - Latitude of the Node.
     * @param lon - Longitude of the Node.
     * @return JSON String listing the results of evaluating whether this Node is on
     * the Network or requires adding Edges to reach the network.
     */
    String addNewNode(Double lat, Double lon);

    // TODO: Diagnostic Service?  This is being used however to Edit Nodes.
    String showAllNodes();

    String getNodeGroups();

    String setNodeGroup(Integer id, Double lat, Double lon);

    // TODO: Probably would go over to the EdgeService
    String getMatchingSegments(Integer pointId);

    /**
     * Given a Node's ID and a new Lat/Lon position, edit the Edges connected
     * to that node and return a Feature Collection with the updated Edges (the
     * Node itself will be still active in the browser and the position is
     * already known by the client).
     * @param pointId - Unique Integer representing the Node to edit.
     * @param lat - Latitude of the new location.
     * @param lng - Longitude of the new location.
     * @return Feature Collection with the updated Edges.
     */
    // TODO: Probably would go over to the EdgeService
    String getEdgesAtNewLocation(Integer pointId, Double lat, Double lng);

    /**
     * Accepts the last recommended position for the Node as the one to be
     * committed to the database.
     * @param pointId - Unique Integer representing the Node to edit.
     * @return String "OK" to confirm.
     */
    // TODO: Probably would go over to the EdgeService
    String confirmEdgesAtNewLocation(Integer pointId);
}
