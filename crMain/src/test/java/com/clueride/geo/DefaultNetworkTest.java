package com.clueride.geo;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.dao.LoadService;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NodeNetworkState;
import com.clueride.domain.factory.NodeFactory;
import com.clueride.poc.geotools.TrackStore;

public class DefaultNetworkTest {

    private Network toTest;

    // @Mock
    private GeoNode geoNode;

    private int trackId = 5457048;
    private List<Integer> trackIds = new ArrayList<>();

    private TrackStore trackStore;

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);

        trackIds.add(trackId);
        trackIds.add(1158079);
        // trackIds.add(6070240);

        LoadService service = LoadService.getInstance();
        // toTest = service.loadNetwork();
        // toTest = service.loadNetwork(trackId);
        toTest = service.loadNetwork(trackIds);
        trackStore = service.loadTrackStore();
    }

    /**
     * Point from the Network -- an end point of a Segment -- is already on the
     * network.
     */
    @Test
    public void evaluateStateNodeOnNetwork() {
        NodeNetworkState expected = NodeNetworkState.ON_NETWORK;

        geoNode = trackStore.getFirstPointOfTrack(trackId);

        NodeNetworkState actual = toTest.evaluateNodeState(geoNode)
                .getNodeNetworkState();
        assertEquals(actual, expected);
        assertEquals(geoNode.getState(), expected);
    }

    /**
     * Point is exactly one the midpoints of one of the Network's Segments, and
     * not an end point.
     */
    @Test
    public void evaluateStateNodeOnSegment() {
        NodeNetworkState expected = NodeNetworkState.ON_SEGMENT;

        geoNode = trackStore.getMidPoint(trackId);

        NodeNetworkState actual = toTest.evaluateNodeState(geoNode)
                .getNodeNetworkState();
        assertEquals(actual, expected);
        assertEquals(geoNode.getState(), expected);
        assertNotNull(geoNode.getSegments());
        assertEquals(geoNode.getSegments().get(0).getUrl(), trackId + "");
        assertEquals(geoNode.getSegments().size(), 1);
    }

    /**
     * Point is near one the midpoints of one of the Network's Segments.
     * 
     * Similar to "OnSegment", except that the lat/lon of the test node is not
     * exact, but within ~30 meters of a midpoint.
     */
    @Test
    public void evaluateStateNodeNearSegment() {
        NodeNetworkState expected = NodeNetworkState.ON_SEGMENT;

        geoNode = TransformUtil.adjustNode(trackStore.getMidPoint(trackId),
                0.000007, 0.000007);

        NodeNetworkState actual = toTest.evaluateNodeState(geoNode)
                .getNodeNetworkState();
        assertEquals(actual, expected);
        assertEquals(geoNode.getState(), expected);
        assertNotNull(geoNode.getSegments());
        assertEquals(geoNode.getSegments().get(0).getUrl(), trackId + "");
        assertEquals(geoNode.getSegments().size(), 1);
    }

    /**
     * Point is near nothing and off network.
     */
    @Test
    public void evaluateStateNodeNotNearSegment() {
        NodeNetworkState expected = NodeNetworkState.OFF_NETWORK;

        geoNode = TransformUtil.adjustNode(trackStore.getMidPoint(trackId),
                1.0000, 1.0000);

        NodeNetworkState actual = toTest.evaluateNodeState(geoNode)
                .getNodeNetworkState();
        assertEquals(actual, expected);
        assertEquals(geoNode.getState(), expected);
        assertEquals(geoNode.getSegments(), Collections.emptyList());
    }

    @Test
    public void dumpSortedNodes() {
        geoNode = TransformUtil.adjustNode(trackStore.getMidPoint(trackId),
                0.0000, 0.0000);
        List<GeoNode> sorted = toTest.getSortedNodes(geoNode);
        System.out.println("\nNodes sorted by distance from: " + geoNode);
        for (GeoNode node : sorted) {
            System.out.println(node.getPoint().distance(geoNode.getPoint())
                    + ": " + node);
        }
    }

    @Test
    public void evaluateStateNodeNearTrack() {
        NodeNetworkState expected = NodeNetworkState.ON_SINGLE_TRACK;

        // Start of Track 809889
        geoNode = NodeFactory.getInstance(-84.35873, 33.77385, 297.7);

        NodeNetworkState actual = toTest.evaluateNodeState(geoNode)
                .getNodeNetworkState();
        assertEquals(actual, expected);
        assertTrue(geoNode.getTracks().size() > 0);
        assertEquals(geoNode.getTracks().get(0).getAttribute("url"), "809889");
    }
}
