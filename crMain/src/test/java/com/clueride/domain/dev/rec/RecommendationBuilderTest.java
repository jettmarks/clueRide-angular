package com.clueride.domain.dev.rec;

import static com.clueride.domain.dev.rec.NetworkRecType.OFF_NETWORK;
import static com.clueride.domain.dev.rec.NetworkRecType.ON_NODE;
import static com.clueride.domain.dev.rec.NetworkRecType.ON_SEGMENT;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import org.mockito.Mockito;
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

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
        networkSegment = Mockito.mock(Segment.class);
        networkNode = Mockito.mock(GeoNode.class);
        requestedNode = Mockito.mock(GeoNode.class);

        toTest = new RecommendationBuilder();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void buildEmpty() {
        NetworkRecommendation actual = toTest.build();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void buildOverSpecified() {
        NetworkRecommendation actual = toTest.addOnNetworkNode(networkNode)
                .addOnNetworkSegment(networkSegment)
                .build();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void buildMissingReqGeoNode() {
        NetworkRecommendation actual = toTest.addOnNetworkNode(networkNode)
                .build();
    }

    @Test
    public void buildOffNetwork() {
        when(requestedNode.getId()).thenReturn(42);
        NetworkRecommendation actual = toTest.requestNetworkNode(requestedNode)
                .build();
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
        assertEquals(actual.getRecType(), ON_NODE);
        assertTrue(actual instanceof OnNode);
    }

}
