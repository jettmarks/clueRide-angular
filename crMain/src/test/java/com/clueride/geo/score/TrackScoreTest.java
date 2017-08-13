package com.clueride.geo.score;

import javax.inject.Provider;

import com.google.inject.Inject;
import com.jettmarks.gmaps.encoder.Track;
import com.jettmarks.gmaps.encoder.Trackpoint;
import com.vividsolutions.jts.geom.LineString;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.CrMainGuiceModuleTest;
import com.clueride.domain.GeoNode;
import com.clueride.domain.factory.LineFeatureFactory;
import com.clueride.feature.Edge;
import com.clueride.feature.SegmentFeature;
import com.clueride.feature.TrackFeature;
import com.clueride.gpx.TrackUtil;
import com.clueride.service.builder.TrackBuilder;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * TODO: Come back to this.
 *
 * @author jett
 *
 */
@Guice(modules = CrMainGuiceModuleTest.class)
public class TrackScoreTest {

    // TODO: Mock services; instantiate data

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

    @Inject
    private Provider<TrackBuilder> trackBuilderProvider;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        Track eastTrack = trackBuilderProvider.get()
                .withBegin(new Trackpoint(-83.0, 33.0))
                .withEnd(new Trackpoint(-83.0, 34.0))
                .build();

        LineString eastLineString = TrackUtil.getLineString(eastTrack);
        // mockSegmentEast = TranslateUtil.lineStringToSegment(eastLineString);
        // mockSegmentEast = new SegmentFeatureImpl(eastLineString);
        mockSegmentEast = (Edge) LineFeatureFactory.getProposal(eastLineString);
        mockSegmentEast.setDisplayName("East");

        Track testTrack = trackBuilderProvider.get()
                .withBegin(new Trackpoint(-84.0, 33.7))
                .withEnd(new Trackpoint(-83.0, 33.7))
                .build();

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
