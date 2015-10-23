package com.clueride.geo;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.gpx.EasyTrack;
import com.clueride.gpx.TrackUtil;
import com.clueride.io.JsonUtil;
import com.jettmarks.gmaps.encoder.Track;
import com.jettmarks.gmaps.encoder.Trackpoint;
import com.vividsolutions.jts.geom.LineString;

public class IntersectionUtilTest {

    private LineString lineStringNExSW;
    private LineString lineStringEast;
    private LineString lineStringWest;
    private LineString lineStringTrack;

    @BeforeMethod
    public void setUp() {
        Track eastTrack = new EasyTrack(
                new Trackpoint(-83.0, 33.0),
                new Trackpoint(-83.0, 34.0));
        lineStringEast = TrackUtil.getLineString(eastTrack);

        Track westTrack = new EasyTrack(
                new Trackpoint(-84.0, 33.0),
                new Trackpoint(-84.0, 34.0));
        lineStringWest = TrackUtil.getLineString(westTrack);

        Track testTrack = new Track();
        double lat = 33.7;
        for (double lon = -84.05; lon < -82.92; lon += 0.1) {
            testTrack.addTrackpoint(new Trackpoint(lon, lat));
        }
        lineStringTrack = TrackUtil.getLineString(testTrack);
        assertEquals("Expecting certain length", 12, lineStringTrack
                .getNumPoints());
    }

    @Test
    public void findCrossingIndex() {
        // First check is to make sure we're working with crossing LineStrings
        assertTrue(lineStringTrack.crosses(lineStringEast));
        assertTrue(lineStringTrack.crosses(lineStringWest));

        int expected = 10;
        int actual = IntersectionUtil.findCrossingIndex(lineStringTrack,
                lineStringEast);
        assertEquals("Index check", expected, actual);

        expected = 0;
        actual = IntersectionUtil.findCrossingIndex(lineStringTrack,
                lineStringWest);
        assertEquals("Index check", expected, actual);
    }

    @Test
    public void findCrossingPair() {
        // Combination of the other two methods
    }

    @Test
    public void retrieveCrossingPair() {
        Track expectedTrack = new EasyTrack(
                new Trackpoint(-83.05, 33.7),
                new Trackpoint(-82.95, 33.7)
                );
        LineString expectedLineString = TrackUtil.getLineString(expectedTrack);
        String expectedJson = JsonUtil.toString(expectedLineString);

        LineString crossingPiece = IntersectionUtil.retrieveCrossingPair(
                lineStringTrack, 10);
        String actualJson = JsonUtil.toString(crossingPiece);
        assertEquals("Crossing Check", expectedJson, actualJson);

        System.out.println("For index 0: "
                + IntersectionUtil.retrieveCrossingPair(lineStringTrack, 0));
    }

    @Test
    public void findFirstIntersection() {

    }
}
