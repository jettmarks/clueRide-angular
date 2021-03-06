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
 * When a proposed track runs from a new Node and meets an existing Segment/Edge,
 * the additional pieces for an "OnTrack" recommendation will be the Edge on the
 * network -- which still needs to be split to be useful -- and the "splitting"
 * node at which that split occurs; this interface provides for those two pieces
 * of information.
 *
 * @author jett
 */
public interface ToSegment extends OnTrack {
    Edge getSegment();
    GeoNode getSplittingNode();
}
