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
 * Created Oct 5, 2015
 */
package com.clueride.feature;

import com.clueride.domain.dev.UnratedSegment;

/**
 * This interface steps in at the point we'd be calling this an
 * "UnratedSegmentFeature", but this name is a whole lot easier to get the head
 * wrapped around.
 * 
 * The name is also something that makes more sense to someone who is used to
 * working with networks: an edge connects two vertices or {@link Node}s in our
 * domain.
 *
 * @author jett
 *
 */
public interface Edge extends UnratedSegment, LineFeature {

}
