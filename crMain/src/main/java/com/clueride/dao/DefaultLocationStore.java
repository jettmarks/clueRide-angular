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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.geotools.feature.DefaultFeatureCollection;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.Node;
import com.clueride.domain.dev.NodeGroup;
import com.clueride.domain.factory.NodeFactory;
import com.clueride.geo.TranslateUtil;
import com.clueride.io.JsonStoreLocation;
import com.clueride.io.JsonStoreType;
import com.clueride.io.JsonUtil;

/**
 * Implementation of LocationStore using file system.
 *
 * @author jett
 *
 */
public class DefaultLocationStore implements LocationStore {

    /** Storage for Locations; typically the endpoints of Segments. */
    private static final String LOCATIONS_FILE_NAME = "locations.geojson";
    /** Storage for LocGroup objects. */
    private static final String LOCATION_GROUPS_FILE_NAME = "locationGroups.geojson";

    private JsonStoreType ourStoreType = JsonStoreType.LOCATION;
    private Integer maxNodeId = 0;
    private Set<GeoNode> nodes = new HashSet<>();
    private Set<NodeGroup> nodeGroups = new HashSet<>();
    private Map<Integer, Node> nodeMap = new HashMap<>();

    private static DefaultLocationStore instance;

    /**
     * Singleton used for supplying Locations from our Store.
     * 
     * @return LocationStore instance for accessing Locations.
     */
    public static LocationStore getInstance() {
        if (instance == null) {
            instance = new DefaultLocationStore();
        }
        return instance;
    }

    /**
     * Use {@link:getInstance()}
     */
    private DefaultLocationStore() {
    }

    /**
     * @see com.clueride.dao.LocationStore#getStoreLocation()
     */
    @Override
    public String getStoreLocation() {
        return JsonStoreLocation.toString(ourStoreType);
    }

    /**
     * @see com.clueride.dao.LocationStore#persistAndReload()
     */
    @Override
    public void persistAndReload() throws IOException {
        // TODO: persistAndReloadLocations();
        persistAndReloadGroups();
    }

    /**
     * @see com.clueride.dao.LocationStore#persistAndReload()
     */
    public void persistAndReloadGroups() throws IOException {
        JsonUtil storageUtil = new JsonUtil(ourStoreType);
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
     * @see com.clueride.dao.LocationStore#getLocations()
     */
    @Override
    public Set<GeoNode> getLocations() {
        if (nodes.isEmpty()) {
            loadLocationsFromDefault();
        }
        return nodes;
    }

    /**
     * 
     */
    private void loadLocationsFromDefault() {
        JsonUtil storageUtil = new JsonUtil(ourStoreType);
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
     * @see com.clueride.dao.LocationStore#getLocationGroups()
     */
    @Override
    public Set<NodeGroup> getLocationGroups() {
        if (nodeGroups.isEmpty()) {
            loadGroupsFromDefault();
        }
        return nodeGroups;
    }

    /**
     * 
     */
    private void loadGroupsFromDefault() {
        JsonUtil storageUtil = new JsonUtil(ourStoreType);
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
     * @see com.clueride.dao.LocationStore#getNodeById(java.lang.Integer)
     */
    @Override
    public Node getNodeById(Integer id) {
        return nodeMap.get(id);
    }

    /**
     * Assumption is that this is a newly created node which has not yet
     * received an ID or has been placed in our datastore.
     * 
     * @see com.clueride.dao.LocationStore#addNew(com.clueride.domain.dev.Node)
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
     * @see com.clueride.dao.LocationStore#addNew(com.clueride.domain.dev.NodeGroup)
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
