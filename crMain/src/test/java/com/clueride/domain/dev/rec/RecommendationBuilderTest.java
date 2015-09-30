package com.clueride.domain.dev.rec;

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

import org.mockito.Mockito;
import org.opengis.feature.simple.SimpleFeature;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkRecommendation;
import com.clueride.domain.dev.Segment;

public class RecommendationBuilderTest {

    private RecommendationBuilder toTest;

    private Segment networkSegment;
    private GeoNode networkNode;
    private GeoNode requestedNode;
    private SimpleFeature track;

    private GeoNode singleNode;
    private GeoNode secondNode;
    private Segment singleSegment;
    private Segment secondSegment;

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
        networkSegment = Mockito.mock(Segment.class);
        networkNode = Mockito.mock(GeoNode.class);
        requestedNode = Mockito.mock(GeoNode.class);
        track = Mockito.mock(SimpleFeature.class);

        singleNode = Mockito.mock(GeoNode.class);
        secondNode = Mockito.mock(GeoNode.class);
        singleSegment = Mockito.mock(Segment.class);
        secondSegment = Mockito.mock(Segment.class);

        toTest = new RecommendationBuilder();
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
        when(requestedNode.getId()).thenReturn(42);
        NetworkRecommendation actual = toTest
                .requestNetworkNode(requestedNode)
                .build();
        assertNotNull(actual);
        assertEquals(actual.getRecType(), OFF_NETWORK);
        assertTrue(actual instanceof OffNetwork);
    }

    @Test
    public void buildOnSegment() {
        when(requestedNode.getId()).thenReturn(42);
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
        when(requestedNode.getId()).thenReturn(42);
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
        when(requestedNode.getId()).thenReturn(42);
        toTest
                .requestNetworkNode(requestedNode)
                .addTrack(track)
                .build();
    }

    @Test
    public void buildToNode() {
        when(requestedNode.getId()).thenReturn(42);
        NetworkRecommendation actual = toTest
                .requestNetworkNode(requestedNode)
                .addTrack(track)
                .addSingleNode(singleNode)
                .build();
        assertNotNull(actual);
        assertEquals(actual.getRecType(), TRACK_TO_NODE);
        assertTrue(actual instanceof ToNode);
    }

    @Test
    public void buildToSegment() {
        when(requestedNode.getId()).thenReturn(42);
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
        when(requestedNode.getId()).thenReturn(42);
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

    @Test
    public void buildToTwoSegments() {
        when(requestedNode.getId()).thenReturn(42);
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

    @Test
    public void buildToSegmentAndNode() {
        when(requestedNode.getId()).thenReturn(42);
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
        when(requestedNode.getId()).thenReturn(42);
        toTest
                .requestNetworkNode(requestedNode)
                .addTrack(track)
                .addSingleSegment(singleSegment)
                .addSingleNode(singleNode)
                .addSecondSegment(secondSegment)
                .build();
    }

    @Test
    public void buildExpectScore() {
        when(requestedNode.getId()).thenReturn(42);
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
}
