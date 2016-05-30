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
package com.clueride.domain.dev;

import java.util.List;

import com.clueride.domain.Rating;
import com.clueride.domain.account.Profile;

/**
 * Generalization of a specific route / path / segment between two points.
 * 
 * Most of the line-based geometries will have these properties.
 *
 * These are derived properties for the most part and are generally not persisted.
 *
 * @author jett
 */
public interface Arc {
    Node getStart();

    Node getEnd();

    Rating getRating();

    Rating getRating(Profile profile);

    Double getDistanceMiles();

    Double getDistanceMeters();

    List<Integer> getEdgeIds();
}
