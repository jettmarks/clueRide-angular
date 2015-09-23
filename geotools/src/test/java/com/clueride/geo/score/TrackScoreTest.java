package com.clueride.geo.score;

import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;
import org.opengis.feature.simple.SimpleFeature;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.Segment;
import com.vividsolutions.jts.geom.LineString;

public class TrackScoreTest {

    private TrackScore toTest;

    @Mock
    private GeoNode mockGeoNode;

    @Mock
    private SimpleFeature mockTrack;

    @Mock
    private Segment mockSegment;

    @Mock
    private LineString mockSubLineString;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        toTest = new TrackScore(mockTrack, mockGeoNode);
    }

    @Test
    public void proposeBestScore() {
        // toTest.proposeBestScore(subTrackLineString, allSegments);
    }

    @Test
    public void score() {
        toTest.score(mockSegment, mockSubLineString);
    }
}
