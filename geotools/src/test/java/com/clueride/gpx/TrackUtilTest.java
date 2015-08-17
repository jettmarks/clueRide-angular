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
package com.clueride.gpx;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.jettmarks.gmaps.encoder.Track;

/**
 * Description.
 *
 * @author jett
 *
 */
public class TrackUtilTest {

	/**
	 * Test method for
	 * {@link com.clueride.gpx.TrackUtil#getTracksForTag(java.lang.String)}.
	 */
	@Test
	public void testGetTracksForTag() {
		List<Track> tracks = TrackUtil.getTracksForTag("bikeTrain");
		Track track = tracks.get(0);
		assertNotNull(track);
		assertNotNull(track.getName());
	}

	/**
	 * Test method for
	 * {@link com.clueride.gpx.TrackUtil#getLineString(com.jettmarks.gmaps.encoder.Track)}
	 * .
	 */
	@Test
	public void testGetLineString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.clueride.gpx.TrackUtil#trackToLineString(java.util.List)}.
	 */
	@Test
	public void testTrackToLineString() {
		fail("Not yet implemented");
	}

}
