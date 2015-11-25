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
 * Created Sep 11, 2015
 */
package com.clueride.apps;

import java.io.IOException;
import java.util.Set;

import com.clueride.dao.DefaultNodeStore;
import com.clueride.dao.DefaultNetworkStore;
import com.clueride.dao.NodeStore;
import com.clueride.domain.GeoNode;
import com.clueride.feature.Edge;

/**
 * One-time utility to load up the segments, find their endpoints, assign IDs to
 * those nodes and then persist them as our initial set of nodes.
 *
 * @author jett
 *
 */
public class PersistInitialNodes {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Set<Edge> segments = DefaultNetworkStore.getInstance().getEdges();
        NodeStore nodeStore = DefaultNodeStore.getInstance();
        for (Edge segment : segments) {
            nodeStore.addNew((GeoNode) segment.getStart());
            nodeStore.addNew((GeoNode) segment.getEnd());
        }
        try {
            nodeStore.persistAndReload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
