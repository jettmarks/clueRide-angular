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

import com.jettmarks.gmaps.encoder.Track;
import com.vividsolutions.jts.geom.LineString;

import com.clueride.dao.DefaultTrackStore;
import com.clueride.dao.TrackStore;
import com.clueride.domain.TrackFeatureImpl;
import com.clueride.domain.dev.GpxBasedTrackImpl;
import com.clueride.domain.dev.TrackImpl;
import com.clueride.feature.TrackFeature;
import com.clueride.gpx.TrackUtil;
import com.clueride.io.GeoJsonUtil;
import com.clueride.io.JsonStoreType;

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

    private static String tag = "inbox";
    private static TrackStore trackStore = DefaultTrackStore.getInstance();

    /**
     * Very useful utility, but I'll resurrect it when I need it.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length > 0)
        {
            tag = args[0];
        }
        System.out.println("Using the tag: " + tag);
        File directory = new File(tag);
        if (!directory.isDirectory()) {
            directory.mkdir();
        }

        // Load TrackStore so we know which IDs have been assigned
        trackStore.getLineFeatures();

        List<Track> tracks = TrackUtil.getTracksForTag(tag);
        System.out.println("Number of tracks: " + tracks.size());

        Map<Track, LineString> linesByName = TrackUtil
                .trackToLineString(tracks);

        GeoJsonUtil jsonUtilRaw = new GeoJsonUtil(JsonStoreType.SEGMENTS);
        for (Map.Entry<Track,LineString> entry : linesByName.entrySet()) {
            Track track = entry.getKey();
            TrackImpl trackDetails = new GpxBasedTrackImpl(
                    track.getDisplayName(),
                    track.getName()
            );
            TrackFeature trackFeature = new TrackFeatureImpl(trackDetails, entry.getValue());
            jsonUtilRaw.writeFeatureToFile(trackFeature.getFeature(), tag +
                    File.separator + track.getName() + ".geojson");
        }
    }
}
