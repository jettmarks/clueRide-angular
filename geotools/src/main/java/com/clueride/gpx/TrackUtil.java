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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jettmarks.gmaps.encoder.ParseGPX;
import com.jettmarks.gmaps.encoder.Track;
import com.jettmarks.gmaps.encoder.Trackpoint;
import com.jettmarks.routes.common.ResourceManager;
import com.jettmarks.routes.server.rtsrc.RouteSource;
import com.jettmarks.routes.server.rtsrc.RouteSourceBase;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import org.geotools.geometry.jts.JTSFactoryFinder;

/**
 * Utilities for handling GPX resources.
 * 
 * This is largely a wrapper for the VisCat libraries which prepared GPX files
 * for Bike Train and Show Routes applications.
 *
 * @author jett
 *
 */
public class TrackUtil {

    /**
     * @param tag
     * @return
     */
    public static List<Track> getTracksForTag(String tag) {
        List<Track> tracks = new ArrayList<Track>();
        RouteSource localRouteSource = RouteSourceBase
                .getInstance("Local Drive");
        String[] tagList = new String[1];
        tagList[0] = tag;
        // TODO: Hook this up to an inbox that gets read instead of hard-coding the file names.
        String routeId = "12102921";
//        for (int routeId : LocalRoutes.getRouteIds(tag)) {
            String routeName = routeId + ".gpx";
            String sUrl = localRouteSource.getRouteURL(routeName, tagList);
            String rawGPX = ResourceManager.getResource(sUrl);
            ParseGPX parser = new ParseGPX();
            Track track = parser.getTrackFromGPX(rawGPX);
            track.setName("" + routeId);
            tracks.add(track);
//        }
        return tracks;
    }

    /**
     * @param track
     * @return
     */
    public static LineString getLineString(Track track) {
        LineString lineString = null;
        List<Coordinate> positions = new ArrayList<Coordinate>();
        for (Trackpoint trackPoint : track.getTrackpoints()) {
            Coordinate coordinate = new Coordinate(
                    trackPoint.getLonDouble(), trackPoint.getLatDouble(),
                    trackPoint.getAltitude());
            positions.add(coordinate);
        }
        // Hints hints = new Hints();
        // hints.put(Hints.CRS, DefaultGeographicCRS.WGS84_3D);

        Coordinate[] posArray = new Coordinate[positions.size()];
        GeometryFactory geometryFactory = JTSFactoryFinder
                .getGeometryFactory(null);
        lineString = geometryFactory.createLineString(positions
                .toArray(posArray));
        return lineString;
    }

    /**
     * @param tracks
     * @return
     */
    public static Map<Track, LineString> trackToLineString(List<Track> tracks) {
        Map<Track, LineString> linesByName = new HashMap<Track, LineString>();
        // int maxNumberOfTracks = 23;
        // int numberOfTracks = 0;
        for (Track track : tracks) {
            // if (numberOfTracks++ > maxNumberOfTracks)
            // break;
            LineString lineString = getLineString(track);
            linesByName.put(track, lineString);
        }
        return linesByName;
    }

}
