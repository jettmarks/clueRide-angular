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
import com.clueride.domain.dev.NetworkRecommendation;

/**
 * Base Interface for a set of interfaces representing New Locations being added
 * to the Network.
 *
 * @author jett
 *
 */
public interface Rec extends NetworkRecommendation {
    GeoNode getNewNode();
}
