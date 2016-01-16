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

/**
 * Holds the Proposal for adding/editing network elements (Edges and Nodes)
 * while the user is reviewing and then once selected is prepared for updating
 * the network.
 *
 * @author jett
 *
 */
public interface NetworkProposal {
    /**
     * Unique identifier for this Network Proposal, generated when the NetworkProposal is generated.
     * @return Identifier for this Network Proposal.
     */
    Integer getId();

    /**
     * Shortcut method to determine if there are more than one recommendations in the list.
     * @return true if there are more than one recommendations.
     */
    boolean hasMultipleRecommendations();

    /**
     * Provides list of all the recommendations that are part of this Proposal.
     * @return List of all the proposal's recommendations, may be empty, but generally has a single Rec, and
     * in some instances, multiple recs the client should choose amongst.
     */
    List<NetworkRecommendation> getRecommendations();

    /**
     * Returns the specific recommendation matching the ID provided.
     * @param recId - ID of a recommendation which originated from this proposal.
     * @return Subclass of NetworkRecommendation matching the unique recId or null if
     * the record is not found.
     */
    NetworkRecommendation getRecommendation(Integer recId);

    /**
     * Recommendations are returned as JSON and more specifically, Feature Collections
     * which the Leaflet client-side mapping library can understand and display on a map.
     * @return String GeoJSON representing a Feature Collection of the Recommendation(s).
     */
    String toJson();

    /**
     * @return Representation of the evaluation of a new Node request; rather bewhiskered.
     */
    NodeNetworkState getNodeNetworkState();

    void resetRecommendationList();
}
