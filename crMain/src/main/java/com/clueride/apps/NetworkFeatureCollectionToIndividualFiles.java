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
 * Created Oct 10, 2015
 */
package com.clueride.apps;

import java.util.Set;

import com.clueride.dao.JsonNetworkStore;
import com.clueride.dao.NetworkStore;
import com.clueride.domain.EdgeImpl;
import com.clueride.feature.Edge;
import com.clueride.feature.FeatureType;
import com.clueride.feature.LineFeature;
import com.clueride.io.GeoJsonUtil;
import com.clueride.io.JsonStoreType;

/**
 * Name of this class explains pretty well that we're taking a Feature
 * Collection that represents the full Network of Line Features, and storing
 * each Feature into its own file.
 * 
 * This is part of the effort to refactor all the file I/O into using proper
 * {@link FeatureType}s.
 *
 * @author jett
 *
 */
public class NetworkFeatureCollectionToIndividualFiles {

    /**
     * @param args
     */
    public static void main(String[] args) {

        // Working in the Test directory
        // JsonStoreLocation.setTestMode();

        // Pick up the Edge instances from the default location for Edges.
        NetworkStore networkStore = JsonNetworkStore.getInstance();
        // Set<Edge> edges = networkStore.getEdges();
        // if (edges == null) {
        // System.err.println("Not able to load the Edges from the Network");
        // System.exit(-1);
        // }
        Set<LineFeature> lineFeatures = networkStore.getLineFeatures();
        if (lineFeatures == null) {
            System.err
                    .println("Not able to load the Line Features from the Network");
            System.exit(-1);
        }

        // Instance for writing Edges out to disk (test directories for now)
        GeoJsonUtil jsonUtilEdgeOut = new GeoJsonUtil(JsonStoreType.EDGE);

        for (LineFeature lineFeature : lineFeatures) {
            Edge edge = new EdgeImpl(lineFeature.getFeature());
            String fileName = "edge-" + edge.getId() + ".geojson";
            jsonUtilEdgeOut.writeFeatureToFile(edge.getFeature(), fileName);
        }

    }
}
