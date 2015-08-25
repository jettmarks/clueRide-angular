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
 * Created Jul 28, 2015
 */
package com.clueride.domain.factory;

import com.clueride.domain.SegmentImpl;
import com.clueride.domain.dev.Segment;
import com.clueride.domain.dev.Track;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

/**
 * Creates a Segment from a GPS Track.
 *
 * @author jett
 *
 */
public class SegmentFactory {

	/**
	 * @param gpxTrack
	 * @return
	 * @deprecated
	 */
	public static Segment getSegmentFromTrack(Track gpxTrack) {
		Segment segment = new SegmentImpl();
		segment.setTrack(gpxTrack);
		return segment;
	}

	public static Segment getInstance(Geometry geometry) {
		if (!(geometry instanceof LineString)) {
			throw new IllegalArgumentException(
					"Expected type LineString instead of "
							+ geometry.getGeometryType());
		}
		LineString lineString = (LineString) geometry;
		SegmentImpl segment = new SegmentImpl();
		segment.setLineString(lineString);
		segment.setStart(NodeFactory.getInstance(lineString.getPointN(0)));
		segment.setEnd(NodeFactory.getInstance(lineString.getPointN(lineString
				.getNumPoints() - 1)));
		return segment;
	}

}
