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

    private JsonStoreType ourStoreType = JsonStoreType.LOCATION;
    private Integer maxNodeId = 0;
    private Set<Node> nodes = new HashSet<>();
    private Set<NodeGroup> nodeGroups = new HashSet<>();
    private Map<Integer, Node> nodeMap = new HashMap<>();

    private static DefaultLocationStore instance;

    public static LocationStore getInstance() {
        if (instance == null) {
            instance = new DefaultLocationStore();
        }
        return instance;
    }

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
                "locationGroups.geojson");

        nodeGroups.clear();

        featureCollection = storageUtil
                .readFeatureCollection("locationGroups.geojson");
        nodeGroups = TranslateUtil
                .featureCollectionToNodeGroups(featureCollection);
    }

    /**
     * @see com.clueride.dao.LocationStore#getLocations()
     */
    @Override
    public Set<GeoNode> getLocations() {
        // TODO Auto-generated method stub
        return null;
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
                    .readFeatureCollection("locationGroups.geojson");
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
     * @see com.clueride.dao.LocationStore#addNew(com.clueride.domain.dev.Node)
     */
    @Override
    public Integer addNew(Node node) {
        // TODO Auto-generated method stub
        return null;
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
