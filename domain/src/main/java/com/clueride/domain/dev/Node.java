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
 * Created Jul 28, 2015
 */
package com.clueride.domain.dev;

import java.util.List;

/**
 * Description.
 *
 * @author jett
 *
 */
public interface Node {
    boolean matchesLocation(Node node);

    /**
     * Summary of the Node's relationship to a particular Network.
     * 
     * @return
     */
    NodeNetworkState getState();

    void setState(NodeNetworkState nodeNetworkState);

    List<UnratedSegment> getSegments();

    void addSegment(UnratedSegment segment);

    Integer getId();

    void setId(Integer id);

}
