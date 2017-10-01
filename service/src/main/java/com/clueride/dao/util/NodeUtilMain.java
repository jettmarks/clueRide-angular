/*
 * Copyright 2017 Jett Marks
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
 * Created by jett on 9/10/17.
 */
package com.clueride.dao.util;

import java.io.IOException;
import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

import com.clueride.dao.NodeStore;
import com.clueride.domain.GeoNode;
import com.clueride.domain.user.latlon.LatLon;
import com.clueride.domain.user.latlon.LatLonStore;
import com.clueride.infrastructure.ServiceGuiceModule;
import static java.lang.System.exit;

/**
 * Copies records between Stores.
 */
public class NodeUtilMain {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ServiceGuiceModule());

        NodeStore nodeStore = injector.getInstance(
                Key.get(
                        NodeStore.class
                )
        );

        LatLonStore latLonStore = injector.getInstance(
                Key.get(
                        LatLonStore.class
                )
        );

        Set<GeoNode> nodes = nodeStore.getNodes();

        for (GeoNode geoJsonNode : nodes) {
            try {
                LatLon node = new LatLon(
                        geoJsonNode.getLat(),
                        geoJsonNode.getLon()
                );
                node.setId(geoJsonNode.getId());
                latLonStore.addNew(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        exit(0);
    }
}
