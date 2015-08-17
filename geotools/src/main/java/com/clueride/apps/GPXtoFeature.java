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
package com.clueride.apps;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.Segment;
import com.clueride.feature.FeatureType;
import com.clueride.geo.TranslateUtil;
import com.clueride.gpx.TrackUtil;
import com.clueride.io.JsonStoreType;
import com.clueride.io.JsonUtil;
import com.jettmarks.gmaps.encoder.Track;
import com.vividsolutions.jts.geom.LineString;

/**
 * This reads GPX files and turns them into a JSON Feature file for each.
 * 
 * Give this program a string representing the "tag" and it will create a
 * subdirectory containing a matching GeoJSON Feature file for each of the
 * tracks found in that directory.
 *
 * @author jett
 *
 */
public class GPXtoFeature {

	private static String tag = "bikeTrain";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			tag = args[0];
		}
		System.out.println("Using the tag: " + tag);
		File directory = new File(tag);
		if (!directory.isDirectory()) {
			directory.mkdir();
		}

		List<Track> tracks = TrackUtil.getTracksForTag(tag);
		System.out.println("Number of tracks: " + tracks.size());

		Map<Track, LineString> linesByName = TrackUtil
				.trackToLineString(tracks);

		List<Segment> segments = TranslateUtil.lineStringToSegment(linesByName);

		SimpleFeatureBuilder segmentFeatureBuilder = new SimpleFeatureBuilder(
				FeatureType.SEGMENT_FEATURE_TYPE);
		JsonUtil jsonUtilRaw = new JsonUtil(JsonStoreType.SEGMENTS);
		for (Segment segment : segments) {
			SimpleFeature feature = TranslateUtil.segmentToFeature(
					segmentFeatureBuilder, segment);
			jsonUtilRaw.writeFeatureToFile(feature, tag + File.separator
					+ segment.getUrl() + ".geojson");
		}

	}
}
