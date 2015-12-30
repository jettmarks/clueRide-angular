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
 * Created Sep 27, 2015
 */
package com.clueride.domain.dev;

import java.util.List;

import org.geotools.feature.DefaultFeatureCollection;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.rec.NetworkRecType;

/**
 * Holds information regarding one particular Recommendation within a Proposal.
 * 
 * Identification is for the element which represents what the user would view
 * and select on the map.
 *
 * @author jett
 *
 */
public interface NetworkRecommendation {
    Integer getId();

    String getName();

    NetworkRecType getRecType();

    /**
     * @return
     */
    Double getScore();

    DefaultFeatureCollection getFeatureCollection();

    /** Provide a list of the Recommendations on the current LOGGER. */
    void logRecommendationSummary();

    /**
     * Count of the number of Features in the Recommendation.
     * @return count does not include the starting node (whether there is one or not).
     */
    int getFeatureCount();

    List<GeoNode> getNodeList();
}
