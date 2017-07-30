package com.clueride.domain.dev.rec;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.CoreGuiceModuleTest;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkRecommendation;
import com.clueride.domain.factory.LineFeatureFactory;
import com.clueride.domain.factory.PointFactory;
import com.clueride.feature.Edge;
import com.clueride.feature.TrackFeature;
import com.clueride.service.builder.RecommendationBuilder;
import static com.clueride.domain.dev.rec.NetworkRecType.OFF_NETWORK;
import static com.clueride.domain.dev.rec.NetworkRecType.ON_NODE;
import static com.clueride.domain.dev.rec.NetworkRecType.ON_SEGMENT;
import static com.clueride.domain.dev.rec.NetworkRecType.TRACK_TO_2_NODES;
import static com.clueride.domain.dev.rec.NetworkRecType.TRACK_TO_2_SEGMENTS;
import static com.clueride.domain.dev.rec.NetworkRecType.TRACK_TO_NODE;
import static com.clueride.domain.dev.rec.NetworkRecType.TRACK_TO_SEGMENT;
import static com.clueride.domain.dev.rec.NetworkRecType.TRACK_TO_SEGMENT_AND_NODE;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

@Guice(modules = CoreGuiceModuleTest.class)
public class RecommendationBuilderTest {

    private RecommendationBuilder toTest;

    private Edge networkSegment;
    private GeoNode networkNode;
    private GeoNode requestedNode;
    private TrackFeature track;

    private GeoNode singleNode;
    private GeoNode secondNode;

    private Edge singleSegment;
    private Edge secondSegment;

    private Point pointA;
    private Point pointB;

    private Point pointR;

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
        networkSegment = Mockito.mock(Edge.class);
        networkNode = Mockito.mock(GeoNode.class);
        requestedNode = Mockito.mock(GeoNode.class);
        // track = Mockito.mock(SimpleFeature.class);
        track = getTestTrack();

        pointA = PointFactory.getJtsInstance(33.0, -84.0, 300.0);
        pointB = PointFactory.getJtsInstance(33.0, -83.0, 300.0);
        pointR = PointFactory.getJtsInstance(33.0, -83.5, 300.0);

        singleNode = Mockito.mock(GeoNode.class);
        secondNode = Mockito.mock(GeoNode.class);
        singleSegment = Mockito.mock(Edge.class);
        secondSegment = Mockito.mock(Edge.class);

        toTest = new RecommendationBuilder();

        when(requestedNode.getId()).thenReturn(42);
        when(requestedNode.getPoint()).thenReturn(pointR);
        when(singleNode.getId()).thenReturn(84);
        when(singleNode.getPoint()).thenReturn(pointA);
    }

    /**
     * @return
     */
    private TrackFeature getTestTrack() {
        Coordinate[] coordinates = {
                new Coordinate(-84.0, 33.0, 300.0),
                new Coordinate(-84.0, 33.1, 300.0),
                new Coordinate(-84.0, 33.2, 300.0),
        };
        LineString lineString = new GeometryFactory()
                .createLineString(coordinates);
        // return TranslateUtil.lineStringToFeature(lineString);
        return (TrackFeature) LineFeatureFactory.getProposal(lineString);
    }

    @Test(expectedExceptions = IllegalStateException.class,
            expectedExceptionsMessageRegExp = ".* specify a [Nn]ode .*")
    public void buildEmpty() {
        toTest.build();
    }

    @Test(expectedExceptions = IllegalStateException.class,
            expectedExceptionsMessageRegExp = ".* specify a [Nn]ode .*")
    public void buildMissingRequestedNode() {
        toTest.addOnNetworkNode(networkNode)
                .addOnNetworkSegment(networkSegment)
                .build();
    }

    @Test(expectedExceptions = IllegalStateException.class,
            expectedExceptionsMessageRegExp = "Cannot specify more than one .*")
    public void buildOverSpecified() {
        toTest
                .requestNetworkNode(requestedNode)
                .addOnNetworkNode(networkNode)
                .addOnNetworkSegment(networkSegment)
                .addTrack(track)
                .build();
    }

    @Test(expectedExceptions = IllegalStateException.class,
            expectedExceptionsMessageRegExp = ".* specify a [Nn]ode .*")
    public void buildMissingReqGeoNode() {
        toTest.addOnNetworkNode(networkNode)
                .build();
    }

    @Test
    public void buildOffNetwork() {
        NetworkRecommendation actual = toTest
                .requestNetworkNode(requestedNode)
                .build();
        assertNotNull(actual);
        assertEquals(actual.getRecType(), OFF_NETWORK);
        assertTrue(actual instanceof OffNetwork);
    }

    @Test
    public void buildOnSegment() {
        NetworkRecommendation actual = toTest
                .addOnNetworkSegment(networkSegment)
                .requestNetworkNode(requestedNode)
                .build();
        assertNotNull(actual);
        assertEquals(actual.getRecType(), ON_SEGMENT);
        assertTrue(actual instanceof OnSegment);
    }

    @Test
    public void buildOnNode() {
        NetworkRecommendation actual = toTest
                .addOnNetworkNode(networkNode)
                .requestNetworkNode(requestedNode)
                .build();
        assertNotNull(actual);
        assertEquals(actual.getRecType(), ON_NODE);
        assertTrue(actual instanceof OnNode);
    }

    @Test(
            expectedExceptions = IllegalStateException.class,
            expectedExceptionsMessageRegExp = ".* existing network must be given .*")
    public void buildTrackOnlyException() {
        toTest
                .requestNetworkNode(requestedNode)
                .addTrack(track)
                .build();
    }

    @Test
    public void buildToNode() {
        NetworkRecommendation actual = toTest
                .requestNetworkNode(requestedNode)
                .addTrack(track)
                .addSingleNode(singleNode)
                .build();
        assertNotNull(actual);
        assertEquals(actual.getRecType(), TRACK_TO_NODE);
        assertTrue(actual instanceof ToNode);
    }

//    @Test
    public void buildToSegment() {
        NetworkRecommendation actual = toTest
                .requestNetworkNode(requestedNode)
                .addTrack(track)
                .addSingleSegment(singleSegment)
                .build();
        assertNotNull(actual);
        assertEquals(actual.getRecType(), TRACK_TO_SEGMENT);
        assertTrue(actual instanceof ToSegment);
    }

    @Test
    public void buildToTwoNodes() {
        NetworkRecommendation actual = toTest
                .requestNetworkNode(requestedNode)
                .addTrack(track)
                .addSingleNode(singleNode)
                .addSecondNode(secondNode)
                .build();
        assertNotNull(actual);
        assertEquals(actual.getRecType(), TRACK_TO_2_NODES);
        assertTrue(actual instanceof ToTwoNodes);
    }

//    @Test
    // This is deprecated; no longer care about these tests.
    public void buildToTwoSegments() {
        NetworkRecommendation actual = toTest
                .requestNetworkNode(requestedNode)
                .addTrack(track)
                .addSingleSegment(singleSegment)
                .addSecondSegment(secondSegment)
                .build();
        assertNotNull(actual);
        assertEquals(actual.getRecType(), TRACK_TO_2_SEGMENTS);
        assertTrue(actual instanceof ToTwoSegments);
    }

//    @Test
    public void buildToSegmentAndNode() {
        NetworkRecommendation actual = toTest
                .requestNetworkNode(requestedNode)
                .addTrack(track)
                .addSingleSegment(singleSegment)
                .addSingleNode(singleNode)
                .build();
        assertNotNull(actual);
        assertEquals(actual.getRecType(), TRACK_TO_SEGMENT_AND_NODE);
        assertTrue(actual instanceof ToSegmentAndNode);
    }

    @Test(
            expectedExceptions = IllegalStateException.class,
            expectedExceptionsMessageRegExp = ".* no more than two network connections .*")
    public void buildToTooMuch() {
        toTest
                .requestNetworkNode(requestedNode)
                .addTrack(track)
                .addSingleSegment(singleSegment)
                .addSingleNode(singleNode)
                .addSecondSegment(secondSegment)
                .build();
    }

//    @Test
    public void buildExpectScore() {
        NetworkRecommendation actual = toTest
                .requestNetworkNode(requestedNode)
                .addTrack(track)
                .addSingleSegment(singleSegment)
                .addSingleNode(singleNode)
                .build();
        assertNotNull(actual);
        assertEquals(actual.getRecType(), TRACK_TO_SEGMENT_AND_NODE);
        assertTrue(actual instanceof ToSegmentAndNode);
        assertTrue(actual.getScore() != null);
    }

    // Series of tests checking the JSON build
//    @Test
    public void buildToNodeFeatureCollection() {
        NetworkRecommendation actual = toTest
                .requestNetworkNode(requestedNode)
                .addTrack(track)
                .addSingleNode(singleNode)
                .build();
        assertNotNull(actual);
        assertEquals(actual.getFeatureCollection().size(), 3);
    }

//    @Test
    public void buildToSegmentFeatureCollection() {
        NetworkRecommendation actual = toTest
                .requestNetworkNode(requestedNode)
                .addTrack(track)
                .addSingleSegment(singleSegment)
                .addSplittingNode(singleNode)
                .build();
        assertNotNull(actual);

        // ReqNode, Track, Segment, SplittingNode
        assertEquals(actual.getFeatureCollection().size(), 4);
    }

}
