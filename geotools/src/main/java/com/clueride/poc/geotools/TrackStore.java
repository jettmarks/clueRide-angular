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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.GeoNode;
import com.clueride.domain.LineFeature;
import com.clueride.domain.factory.LineFeatureFactory;
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
	private Map<Integer, SimpleFeature> trackPerId = new HashMap<>();

	/**
	 * @param features
	 */
	public TrackStore(DefaultFeatureCollection trackFeatures) {
		this.trackFeatures = trackFeatures;
		for (Object object : trackFeatures.toArray()) {
			if (object instanceof SimpleFeature) {
				SimpleFeature feature = (SimpleFeature) object;
				trackPerId.put(
						Integer.parseInt((String) feature.getAttribute("url")),
						feature);
			}
		}
	}

	/**
	 * Just to grab something that is in my bag of nodes.
	 * 
	 * @return
	 */
	public GeoNode getFirstPoint() {
		LineString lineString = getFirstLineString();
		return NodeFactory.getInstance(lineString.getPointN(0));
	}

	public GeoNode getFirstPointOfTrack(Integer id) {
		LineString lineString = getLineString(id);
		return NodeFactory.getInstance(lineString.getPointN(0));
	}

	/**
	 * @return
	 */
	public LineString getFirstLineString() {
		return (LineString) getFirstFeature().getDefaultGeometry();
	}

	public LineString getLineString(Integer id) {
		return (LineString) trackPerId.get(id).getDefaultGeometry();
	}

	public LineFeature getFirstLineFeature() {
		return LineFeatureFactory.getInstance(getFirstFeature());
	}

	public SimpleFeature getFirstFeature() {
		return (SimpleFeature) trackFeatures.toArray()[0];
	}

	/**
	 * @return
	 */
	public GeoNode getMidPoint() {
		LineString lineString = getFirstLineString();
		int n = lineString.getNumPoints() / 2;
		return NodeFactory.getInstance(lineString.getPointN(n));
	}

	/**
	 * @param index
	 * @return
	 */
	public SimpleFeature getTrackPerId(int index) {
		return trackPerId.get(index);
	}

	/**
	 * @param trackId
	 * @return
	 */
	public GeoNode getMidPoint(int trackId) {
		LineString lineString = getLineString(trackId);
		int n = lineString.getNumPoints() / 2;
		return NodeFactory.getInstance(lineString.getPointN(n));
	}

	/**
	 * @return
	 */
	public Collection<SimpleFeature> getFeatures() {
		return trackPerId.values();
	}

}
