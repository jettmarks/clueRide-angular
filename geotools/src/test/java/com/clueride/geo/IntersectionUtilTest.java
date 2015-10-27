package com.clueride.geo;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.dao.DefaultNetworkStore;
import com.clueride.dao.DefaultTrackStore;
import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.GeoNode;
import com.clueride.domain.factory.PointFactory;
import com.clueride.feature.Edge;
import com.clueride.feature.TrackFeature;
import com.clueride.gpx.EasyTrack;
import com.clueride.gpx.TrackUtil;
import com.clueride.io.JsonUtil;
import com.jettmarks.gmaps.encoder.Track;
import com.jettmarks.gmaps.encoder.Trackpoint;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

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

    /**
     * For the first Intersection, we have a few scenarios we want to check:
     * <UL>
     * <LI>Intersection between first two coordinates.
     * <LI>Intersection between last two coordinates.
     * <LI>Even number of points.
     * <LI>Odd number of points.
     * <LI>Two points.
     * <LI>Three points.
     * <LI>Three points where the middle point is the intersection.
     * <LI>Trouble pairs of track and edge.
     * </UL>
     * May come up with more as we run through real data.
     */
    @Test
    public void findFirstIntersectionFirstTwoPoints() {
        Point point = IntersectionUtil.findFirstIntersection(lineStringTrack,
                lineStringWest);
        assertNotNull(point);
    }

    @Test
    public void findFirstIntersectionLastTwoPoints() {
        Point point = IntersectionUtil.findFirstIntersection(lineStringTrack,
                lineStringEast);
        assertNotNull(point);
    }

    @Test
    public void findFirstIntersectionSpecificPair() {
        TrackFeature track = DefaultTrackStore.getInstance().getTrackById(75);
        assertNotNull(track);
        Point pointOK = PointFactory.getJtsInstance(33.77481, -84.35870, 300.0);
        GeoNode splitPointOK = new DefaultGeoNode(pointOK);
        SplitLineString pair = new SplitLineString(track, splitPointOK);
        Edge edge = DefaultNetworkStore.getInstance().getEdgeById(4);
        assertNotNull(edge);
        Point point = IntersectionUtil.findFirstIntersection(
                pair.getLineStringToEnd(), edge.getLineString());
        assertNotNull(point);

        Point pointBad = PointFactory
                .getJtsInstance(33.77385, -84.35873, 300.0);
        GeoNode splitPointBad = new DefaultGeoNode(pointBad);
        pair = new SplitLineString(track, splitPointBad);
        point = IntersectionUtil.findFirstIntersection(
                pair.getLineStringToEnd(), edge.getLineString());
        assertNotNull(point);
    }
}
