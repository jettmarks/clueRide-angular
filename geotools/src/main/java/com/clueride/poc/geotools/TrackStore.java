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
package com.clueride.poc.geotools;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureImpl;

import com.clueride.domain.Node;
import com.clueride.domain.factory.NodeFactory;
import com.vividsolutions.jts.geom.LineString;

/**
 * This holds the Tracks we've brought in from GPX, but they are stored in this
 * object as a FeatureCollection.
 *
 * @author jett
 *
 */
public class TrackStore {
	private FeatureCollection trackFeatures;

	/**
	 * @param features
	 */
	public TrackStore(DefaultFeatureCollection trackFeatures) {
		this.trackFeatures = trackFeatures;
	}

	/**
	 * Just to grab something that is in my bag of nodes.
	 * 
	 * @return
	 */
	public Node getFirstPoint() {
		SimpleFeatureImpl simpleFeature = (SimpleFeatureImpl) trackFeatures
				.toArray()[0];
		LineString geometry = (LineString) simpleFeature.getDefaultGeometry();
		return NodeFactory.getInstance(geometry.getPointN(0));
	}

}
