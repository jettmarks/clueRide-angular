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
package com.clueride.domain.dev.rec;

import com.clueride.domain.GeoNode;
import com.clueride.feature.Edge;

/**
 * When a proposed track runs in two directions -- each hitting the network --
 * and one end hits a Node and the other end hits an Edge/Segment, this class
 * is used.
 *
 * The Edge captured here will still need to be split into two new Edges which
 * replace this original Edge.
 *
 * @author jett
 */
public interface ToSegmentAndNode extends OnTrack {
    GeoNode getNetworkNode();
    Edge getSegment();
    GeoNode getSplittingNode();
}
