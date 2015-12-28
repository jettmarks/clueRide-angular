/*
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;
import org.geotools.feature.DefaultFeatureCollection;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.Node;
import com.clueride.domain.dev.NodeGroup;
import com.clueride.domain.dev.NodeNetworkState;
import com.clueride.domain.factory.NodeFactory;
import com.clueride.geo.TranslateUtil;
import com.clueride.io.GeoJsonUtil;
import com.clueride.io.JsonStoreLocation;
import com.clueride.io.JsonStoreType;

/**
 * Implementation of NodeStore using file system.
 *
 * @author jett
 */
@Singleton
public class DefaultNodeStore implements NodeStore {
    private static final Logger LOGGER = Logger.getLogger(DefaultNodeStore.class);

    /** Storage for Nodes; typically the endpoints of Segments. */
    private static final String LOCATIONS_FILE_NAME = "locations.geojson";
    /** Storage for LocGroup objects. */
    private static final String LOCATION_GROUPS_FILE_NAME = "locationGroups.geojson";

    private JsonStoreType ourStoreType = JsonStoreType.LOCATION;
    private static Integer maxNodeId = 0;
    private static Set<GeoNode> nodes = null;
    private static Set<NodeGroup> nodeGroups = new HashSet<>();
    private static Map<Integer, Node> nodeMap = new HashMap<>();

    private static DefaultNodeStore instance;

    /**
     * Singleton used for supplying Locations from our Store.
     * 
     * @return NodeStore instance for accessing Locations.
     */
    public static NodeStore getInstance() {
        if (instance == null) {
            instance = new DefaultNodeStore();
        }
        return instance;
    }

    /**
     * Use {@link:getInstance()}
     */
    @Inject
    public DefaultNodeStore() {
        if (nodes == null) {
            loadLocationsFromDefault();
        }
    }

    /**
     *
     */
    private void loadLocationsFromDefault() {
        GeoJsonUtil storageUtil = new GeoJsonUtil(ourStoreType);
        DefaultFeatureCollection featureCollection = null;
        try {
            featureCollection = storageUtil
                    .readFeatureCollection(LOCATIONS_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        nodes = TranslateUtil.featureCollectionToNodes(featureCollection);
        for (GeoNode node : nodes) {
            nodeMap.put(node.getId(), node);
            if (maxNodeId < node.getId()) {
                maxNodeId = node.getId();
            }
        }
        NodeFactory.setMaxNodeId(maxNodeId);
    }

    /**
     * @see NodeStore#getStoreLocation()
     */
    @Override
    public String getStoreLocation() {
        return JsonStoreLocation.toString(ourStoreType);
    }

    /**
     * @see NodeStore#persistAndReload()
     */
    @Override
    public void persistAndReload() throws IOException {
        persistAndReloadNodes();
        persistAndReloadGroups();
    }

    /**
     * Stores differences in the Memory and Disk copies before shoving this out
     * to disk; assumes that the in-memory copy is the official copy and we're just
     * taking a snapshot record (not to imply that previous copies are kept -- that
     * would have to happen outside this method/class).
     */
    @Override
    public void persist() {
        List<Integer> inMemoryIDs = new ArrayList<>();
        List<Integer> onDiskIDs = new ArrayList<>();

        for (Node node : nodes) {
            inMemoryIDs.add(node.getId());
        }
        dumpList("In Memory IDs", inMemoryIDs);

        GeoJsonUtil jsonUtilNodes = new GeoJsonUtil(JsonStoreType.LOCATION);
        Set<GeoNode> nodesOnDisk = Collections.EMPTY_SET;
        try {
            nodesOnDisk = TranslateUtil.featureCollectionToNodes(
                    jsonUtilNodes.readFeatureCollection(LOCATIONS_FILE_NAME)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Node node : nodesOnDisk) {
            onDiskIDs.add(node.getId());
        }
        dumpList("On Disk IDs", onDiskIDs);

        List<Integer> toBeAdded = new ArrayList<>();
        for (Integer rec : inMemoryIDs) {
            if (!onDiskIDs.contains(rec)) {
                toBeAdded.add(rec);
                LOGGER.info("Adding " + rec);
            }
        }

        List<Integer> toBeRemoved = new ArrayList<>();
        for (Integer rec : onDiskIDs) {
            if (!inMemoryIDs.contains(rec)) {
                toBeRemoved.add(rec);
                LOGGER.info("Removing " + rec);
            }
        }

        // CA-69 Cleanup of nodes before persisting
        for (GeoNode node : nodes) {
            node.setName(null);
            node.setState(NodeNetworkState.UNDEFINED);
        }

        try {
            persistAndReloadNodes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * To be used if I switch to individual files for Nodes.
        if (toBeAdded.isEmpty()) {
            LOGGER.info("No records to be Added");
        } else {
            // Add the instances to be Added
        }

        if (toBeRemoved.isEmpty()) {
            LOGGER.info("No records to be Removed");
        } else {
            // Delete files for the records to be removed
        }
         */
    }

    /**
     * Lists out in order the values in the passed list.
     *
     * Package visibility for diagnostics and testing.
     *
     * @param message to be displayed as a "header"
     * @param listToDump what we want to see listed out.
     */
    void dumpList(String message, List<Integer> listToDump) {
        System.out.println(message);
        List<Integer> sortedList = new ArrayList<>(listToDump);
        Collections.sort(sortedList);
        for (Integer id : sortedList) {
            System.out.println(id);
        }
    }

    /**
     * @throws IOException
     *
     */
    public void persistAndReloadNodes() throws IOException {
        GeoJsonUtil storageUtil = new GeoJsonUtil(ourStoreType);
        DefaultFeatureCollection featureCollection = TranslateUtil
                .nodesToFeatureCollection(nodes);
        storageUtil.writeFeaturesToFile(featureCollection,
                LOCATIONS_FILE_NAME);

        nodes.clear();

        featureCollection = storageUtil
                .readFeatureCollection(LOCATIONS_FILE_NAME);
        nodes = TranslateUtil
                .featureCollectionToNodes(featureCollection);

    }

    /**
     * @see NodeStore#persistAndReload()
     */
    public void persistAndReloadGroups() throws IOException {
        GeoJsonUtil storageUtil = new GeoJsonUtil(ourStoreType);
        DefaultFeatureCollection featureCollection = TranslateUtil
                .groupNodesToFeatureCollection(nodeGroups);
        storageUtil.writeFeaturesToFile(featureCollection,
                LOCATION_GROUPS_FILE_NAME);

        nodeGroups.clear();

        featureCollection = storageUtil
                .readFeatureCollection(LOCATION_GROUPS_FILE_NAME);
        nodeGroups = TranslateUtil
                .featureCollectionToNodeGroups(featureCollection);
    }

    /**
     * @see NodeStore#getNodes()
     */
    @Override
    public Set<GeoNode> getNodes() {
        if (nodes.isEmpty()) {
            loadLocationsFromDefault();
        }
        return nodes;
    }

    /**
     * @see NodeStore#getNodeGroups()
     */
    @Override
    public Set<NodeGroup> getNodeGroups() {
        if (nodeGroups.isEmpty()) {
            loadGroupsFromDefault();
        }
        return nodeGroups;
    }

    /**
     * 
     */
    private void loadGroupsFromDefault() {
        GeoJsonUtil storageUtil = new GeoJsonUtil(ourStoreType);
        DefaultFeatureCollection featureCollection = null;
        try {
            featureCollection = storageUtil
                    .readFeatureCollection(LOCATION_GROUPS_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        nodeGroups = TranslateUtil
                .featureCollectionToNodeGroups(featureCollection);
        for (NodeGroup nodeGroup : nodeGroups) {
            nodeMap.put(nodeGroup.getId(), nodeGroup);
            if (maxNodeId < nodeGroup.getId()) {
                maxNodeId = nodeGroup.getId();
            }
        }
    }

    /**
     * @see NodeStore#getNodeById(java.lang.Integer)
     */
    @Override
    public Node getNodeById(Integer id) {
        return nodeMap.get(id);
    }

    /**
     * Assumption is that this is a newly created node which has not yet
     * received an ID or has been placed in our datastore.
     * 
     * @see NodeStore#addNew(com.clueride.domain.dev.Node)
     */
    @Override
    public Integer addNew(Node node) {
        Integer nodeId = NodeFactory.getNextId();
        node.setId(nodeId);
        nodes.add((GeoNode) node);
        nodeMap.put(nodeId, node);
        return nodeId;
    }

    /**
     * @see NodeStore#addNew(com.clueride.domain.dev.NodeGroup)
     */
    @Override
    public Integer addNew(NodeGroup nodeGroup) {
        maxNodeId++;
        nodeGroup.setId(maxNodeId);
        nodeGroups.add(nodeGroup);
        nodeMap.put(maxNodeId, nodeGroup);
        return maxNodeId;
    }

}
