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
 * Created by jett on 12/28/15.
 */
package com.clueride.service;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkProposal;

/**
 * Service that supports creation and persisting of new additions to the Network
 * through NetworkProposals made up of Recommendations.
 */
public interface RecommendationService {
    /**
     * Given a proposed New Location, find out how it connects to the network
     * and recommend a connection if one doesn't already exist.
     *
     * @param newNode - holds lat/lon pair of the proposed new Node (or perhaps
     *                sufficiently close to an existing node.
     * @return NetworkProposal which recommends how to attach the Node to the
     * existing Network.
     */
    NetworkProposal buildProposalForNewNode(GeoNode newNode);
}
