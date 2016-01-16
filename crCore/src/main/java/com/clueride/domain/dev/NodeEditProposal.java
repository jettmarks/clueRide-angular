/*
 * Copyright 2016 Jett Marks
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
 * Created by jett on 1/14/16.
 */
package com.clueride.domain.dev;

import org.geotools.feature.DefaultFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.io.GeoJsonUtil;
import com.clueride.io.JsonStoreType;

/**
 * Proposal for changing a Node along with the Edges connected at that Node.
 */
public class NodeEditProposal extends NetworkProposalBaseImpl {
    @Override
    public String toJson() {
        GeoJsonUtil jsonRespWriter = new GeoJsonUtil(JsonStoreType.OTHER);
        DefaultFeatureCollection fcPoints = new DefaultFeatureCollection();
        DefaultFeatureCollection fcNonPoints = new DefaultFeatureCollection();
        for (NetworkRecommendation rec : networkRecommendations) {
            rec.logRecommendationSummary();
            for (SimpleFeature feature : rec.getFeatureCollection()) {
                if (feature.getFeatureType().getTypeName().contains("PointType")) {
                    fcPoints.add(feature);
                } else {
                    fcNonPoints.add(feature);
                }
            }
        }
        String pointResult = jsonRespWriter.toString(fcPoints);
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(pointResult.substring(0, pointResult.length()-2));

        boolean firstTimeThrough = true;
        for (SimpleFeature feature : fcNonPoints) {
            if (!firstTimeThrough) {
                stringBuffer.append(",");
            }
            stringBuffer.append(jsonRespWriter.toString(feature));
            firstTimeThrough = false;
        }
        stringBuffer.append("]}");
        return stringBuffer.toString();
    }
}
