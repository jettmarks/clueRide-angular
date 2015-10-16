package com.clueride.geo.score;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.domain.GeoNode;
import com.clueride.domain.factory.LineFeatureFactory;
import com.clueride.feature.Edge;
import com.clueride.feature.SegmentFeature;
import com.clueride.feature.TrackFeature;
import com.clueride.gpx.EasyTrack;
import com.clueride.gpx.TrackUtil;
import com.jettmarks.gmaps.encoder.Trackpoint;
import com.vividsolutions.jts.geom.LineString;

/**
 * TODO: Come back to this.
 *
 * @author jett
 *
 */
public class TrackScoreTest {

    private TrackScore toTest;
    private Edge mockSegmentEast;
    private LineString trackLineString;
    private LineString[] mockSubLineString;

    @Mock
    private GeoNode mockGeoNode;

    @Mock
    private TrackFeature mockTrack;

    @Mock
    private SegmentFeature mockSegmentNWxSE;
    private SegmentFeature mockSegmentNExSW;

    @Mock
    private SubTrackScore expected;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        com.jettmarks.gmaps.encoder.Track eastTrack = new EasyTrack(
                new Trackpoint(-83.0, 33.0),
                new Trackpoint(-83.0, 34.0));
        LineString eastLineString = TrackUtil.getLineString(eastTrack);
        // mockSegmentEast = TranslateUtil.lineStringToSegment(eastLineString);
        // mockSegmentEast = new SegmentFeatureImpl(eastLineString);
        mockSegmentEast = (Edge) LineFeatureFactory.getProposal(eastLineString);
        mockSegmentEast.setDisplayName("East");

        com.jettmarks.gmaps.encoder.Track testTrack = new EasyTrack(
                new Trackpoint(-84.0, 33.7),
                new Trackpoint(-83.0, 33.7));
        trackLineString = TrackUtil.getLineString(testTrack);
        when(mockTrack.getLineString()).thenReturn(trackLineString);

    }

    @Test
    public void proposeBestScore() {
        // toTest.proposeBestScore(subTrackLineString, allSegments);
    }

    // @Test
    // TODO: Come back to this
    // public void score() {
    // // when(expected.getBestSegment().getSegId()).thenReturn(6);
    // GeoNode geoNode1 = new DefaultGeoNode();
    // geoNode1.setPoint(PointFactory.getJtsInstance(33.7, -83.95, 0.0));
    //
    // mockSubLineString = TranslateUtil.split(trackLineString, geoNode1
    // .getPoint().getCoordinate(), true);
    // assertTrue("Expected candidate node to lie on the track",
    // !mockSubLineString[0].isEmpty()
    // && !mockSubLineString[1].isEmpty());
    //
    // toTest = new TrackScoreImpl(mockTrack, geoNode1);
    // SubTrackScore actual = toTest.score(mockSegmentEast,
    // mockSubLineString[0]);
    // System.out.println(actual);
    // assertTrue("Expected score to exist", actual.hasScore());
    // }
}
