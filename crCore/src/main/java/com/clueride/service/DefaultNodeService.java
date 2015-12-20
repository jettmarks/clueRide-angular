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

import com.clueride.dao.NodeStore;
import com.clueride.domain.GeoNode;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Point;

/**
 * Default Implementation of NodeService.
 */
public class DefaultNodeService implements NodeService {

    private final NodeStore nodeStore;

    @Inject
    public DefaultNodeService(NodeStore nodeStore) {
        this.nodeStore = nodeStore;
    }
    @Override
    public Point getPointByNodeId(Integer nodeId) {
        GeoNode node = (GeoNode) nodeStore.getNodeById(nodeId);
        return node.getPoint();
    }
}
