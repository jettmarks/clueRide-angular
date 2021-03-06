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
 * Created Sep 6, 2015
 */
package com.clueride.dao;

import java.io.IOException;
import java.util.Set;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.Node;
import com.clueride.domain.dev.NodeGroup;

/**
 * Manages the set of Nodes.
 *
 * @author jett
 *
 */
public interface NodeStore {

    /**
     * Tells us where the node data can be found.
     * 
     * @return - String representation of the Path of where the node data is stored.
     */
    String getStoreLocation();

    /**
     * Takes the current state of the nodes, saves it to the store's
     * location (with backup preferably), and then reloads from that store.
     * 
     * Can be used to initially load as well if the memory copy is empty.
     * 
     * @throws IOException
     */
    void persistAndReload() throws IOException;

    /**
     * This method follows the approach that the in-memory copy is the
     * "official" copy and when persisted, involves comparing what
     * is on disk with what is in memory to come up with a list of changes.
     *
     * This follows what is happening in the Edges/Segments.
     */
    void persist();

    /**
     * This holds the Locations -- if we need them, this is the place to go.
     * 
     * @return - Set of the Nodes in memory.
     */
    Set<GeoNode> getNodes();

    /**
     * This holds the Location Groups.
     * 
     * @return - Set of Location Groups in memory.
     */
    Set<NodeGroup> getNodeGroups();

    /**
     * Choose a particular Node by its ID.
     * 
     * @param id - unique identifier for the Node.
     * @return Node matching the ID (or null if not found).
     */
    Node getNodeById(Integer id);

    /**
     * Brings the geometry and other details for creation of a new Node along
     * with the assignment of an ID.
     * 
     * @param node which is newly created, ready to persist.
     * @return id of the newly created Node.
     */
    Integer addNew(Node node);

    /**
     * Adds a newly created NodeGroup to the store and returns the ID that is assigned here.
     * @param nodeGroup newly created and ready to persist
     * @return id of the newly added NodeGroup.
     */
    Integer addNew(NodeGroup nodeGroup);

    /**
     * Accepts a definition of a Node and writes it to disk, expecting that we're overwriting
     * an earlier version of the same Node.
     * @param newNode - Node we want to update on disk.
     */
    void persistNode(GeoNode newNode) throws IOException;
}
