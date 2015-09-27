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
 * Holds the Proposal for adding/editing network elements (Segments and Nodes)
 * while the user is reviewing and then once selected is prepared for updating
 * the network.
 *
 * @author jett
 *
 */
public interface NetworkProposal {
    Integer getId();

    boolean hasMultipleRecommendations();

    List<NetworkRecommendation> getRecommendations();

    String toJson();
}
