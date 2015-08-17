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
 * Created Aug 16, 2015
 */
package com.clueride.feature;

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.LineString;

/**
 * Support for the Feature Types used in this application.
 *
 * @author jett
 *
 */
public class FeatureType {
	public static final SimpleFeatureType SEGMENT_FEATURE_TYPE = buildSegmentInstance();

	public static SimpleFeatureType buildSegmentInstance() {

		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		builder.setName("SegmentFeatureType");
		builder.setCRS(DefaultGeographicCRS.WGS84_3D); // <- Coordinate
														// reference system

		// add attributes in order
		builder.add("id", Integer.class);
		builder.add("the_geom", LineString.class);
		builder.add("name", String.class);
		builder.add("url", String.class);

		// build the type
		final SimpleFeatureType featureType = builder.buildFeatureType();

		return featureType;
	}
}
