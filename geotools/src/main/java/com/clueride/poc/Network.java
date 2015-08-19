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
 * Created Aug 18, 2015
 */
package com.clueride.poc;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;

/**
 * Holds collection of segments that make up the full set of connected Features
 * that we know about for a given area.
 *
 * Memory copy is a FeatureCollection of LineString (Segments).
 * 
 * @author jett
 *
 */
public class Network {
	FeatureCollection allSegments;

	/**
	 * @param defaultFeatureCollection
	 */
	public Network(DefaultFeatureCollection defaultFeatureCollection) {
		allSegments = defaultFeatureCollection;
	}

}
