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
package com.clueride.geo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.Segment;
import com.clueride.domain.SegmentImpl;
import com.clueride.domain.factory.SegmentFactory;
import com.clueride.feature.FeatureType;
import com.jettmarks.gmaps.encoder.Track;
import com.vividsolutions.jts.geom.LineString;

/**
 * Methods to translate from one type to another.
 *
 * @author jett
 *
 */
public class TranslateUtil {

	/**
	 * This uses the SegmentTypeBuilder which knows about Segments and their
	 * specific features.
	 * 
	 * @param segments
	 *            - input
	 * @return
	 */
	public static DefaultFeatureCollection segmentsToFeatureCollection(
			List<Segment> segments) {
		SimpleFeatureBuilder segmentFeatureBuilder = new SimpleFeatureBuilder(
				FeatureType.SEGMENT_FEATURE_TYPE);
		DefaultFeatureCollection features;
		features = new DefaultFeatureCollection();
		for (Segment segment : segments) {
			SimpleFeature feature = segmentToFeature(segmentFeatureBuilder,
					segment);
			features.add(feature);
		}
		return features;
	}

	/**
	 * @param segmentFeatureBuilder
	 * @param segment
	 * @return
	 */
	public static SimpleFeature segmentToFeature(
			SimpleFeatureBuilder segmentFeatureBuilder, Segment segment) {
		segmentFeatureBuilder.add(segment.getId());
		segmentFeatureBuilder.add(((SegmentImpl) segment).getLineString());
		segmentFeatureBuilder.add(segment.getName());
		segmentFeatureBuilder.add(segment.getUrl());
		SimpleFeature feature = segmentFeatureBuilder.buildFeature(null);
		return feature;
	}

	/**
	 * @param linesByName
	 * @return
	 */
	public static List<Segment> lineStringToSegment(
			Map<Track, LineString> linesByName) {
		List<Segment> segments;
		segments = new ArrayList<Segment>();
		int index = 0;
		for (Entry<Track, LineString> trackEntry : linesByName.entrySet()) {
			index++;
			Segment segment = SegmentFactory.getInstance(trackEntry.getValue());
			segment.setId(index);
			segment.setName(trackEntry.getKey().getDisplayName());
			segment.setUrl(trackEntry.getKey().getName());
			segments.add(segment);
		}
		return segments;
	}

}
