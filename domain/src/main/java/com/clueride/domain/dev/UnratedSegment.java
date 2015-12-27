/*
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
 * Created Oct 4, 2015
 */
package com.clueride.domain.dev;

/**
 * As Tracks are prepared to be turned into Segments, an intermediate step is to
 * propose a list of points which connect the network at the ends and at the
 * ends only; this represents a Segment which is not yet fully qualified as a
 * Segment.
 *
 * @author jett
 *
 */
public interface UnratedSegment extends Track {

    /**
     * Node is a generic network connectivity concept which may or may not be
     * realized by a Geometry, but certainly has an ID which is shared with
     * other Segments and Locations.
     * 
     * @return Node representing the "start" end of the Segment.
     */
    Node getStart();

    /**
     * @return Node representing the "tail" end of the Segment.
     */
    Node getEnd();
}
