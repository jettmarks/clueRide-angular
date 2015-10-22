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
 * Created Oct 20, 2015
 */
package com.clueride.domain.user;

import java.util.SortedSet;

import com.clueride.domain.dev.Arc;
import com.clueride.domain.dev.Segment;

/**
 * Represents a particular choice of Segments to travel between two
 * {@link Location}s.
 *
 * @author jett
 *
 */
public interface Path extends Arc {
    SortedSet<Segment> getSegments();

    Location getDeparture();

    Location getDestination();
}
