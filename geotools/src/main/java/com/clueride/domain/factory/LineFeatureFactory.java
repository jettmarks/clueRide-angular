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
 * Created Aug 23, 2015
 */
package com.clueride.domain.factory;

import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.DefaultLineFeature;
import com.clueride.domain.LineFeature;

/**
 * Takes a SimpleFeature which is wrapping a LineString geometry, and turns it
 * into an implementation of the LineFeature interface.
 *
 * @author jett
 *
 */
public class LineFeatureFactory {

	public static LineFeature getInstance(SimpleFeature simpleLineStringFeature) {
		return new DefaultLineFeature(simpleLineStringFeature);
	}

}
