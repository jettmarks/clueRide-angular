package com.clueride.service;

import com.clueride.config.GeoProperties;
import com.clueride.dao.DefaultTrackStore;
import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.GeoNode;
import com.clueride.domain.factory.PointFactory;
import com.clueride.feature.Edge;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.SplitLineString;
import com.vividsolutions.jts.geom.Point;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.clueride.geo.SplitLineString.END;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by jett on 11/3/15.
 */
public class TrackEvalTest {

    private TrackEval toTest;

    @BeforeMethod
    public void setUp() throws Exception {
        Point point = PointFactory.getJtsInstance(33.79682, -84.35004, 295.0);
        GeoNode newLoc = new DefaultGeoNode(point);
        TrackFeature track = DefaultTrackStore.getInstance().getTrackById(25);
        assertNotNull(track);
        SplitLineString splitLineString = new SplitLineString(track, newLoc);
        toTest = new TrackEval(splitLineString.getSubTrackFeature(END));
        System.out.println("TrackEval: "+toTest);
    }

    @Test
    public void testGetNearestNetworkNode() throws Exception {
        GeoNode nearestNode = toTest.getNearestNetworkNode();
        assertNotNull(nearestNode);

        Edge nearestEdge = toTest.getNearestNetworkEdge();
        assertEquals(nearestNode.getPoint(), nearestEdge.getLineString().getStartPoint());
        System.out.println("NearestNode as Point: "+nearestNode.getPoint());
    }

    @Test
    public void testGetNearestNetworkEdge() throws Exception {
        Edge nearestEdge = toTest.getNearestNetworkEdge();
        assertNotNull(nearestEdge);
    }

    @Test
    public void testGetNodeDistance() throws Exception {
        System.out.println("Node Distance: "+toTest.getNodeDistance());
    }

    @Test
    public void testGetEdgeDistance() throws Exception {
        System.out.println("Edge Distance: "+toTest.getEdgeDistance());
    }

    @Test
    public void testGetNetworkNode() throws Exception {

    }

    @Test
    public void testGetNetworkEdge() throws Exception {

    }

    @Test
    public void testGetTrackEvalType() throws Exception {
        TrackEvalType evalType = toTest.getTrackEvalType();
        assertEquals(evalType, TrackEvalType.NODE);
    }

    @Test
    public void testGetSplittingNode() throws Exception {
        Edge nearestEdge = toTest.getNearestNetworkEdge();
        assertNotNull(nearestEdge);

        GeoNode splittingNode = toTest.getSplittingNode();
        assertNotNull(splittingNode);
        System.out.println("Splitting Node as Point: "+splittingNode.getPoint());

//        TrackFeature proposedTrack = toTest.getProposedTrack();
//        assertEquals(splittingNode.getPoint(), proposedTrack.getLineString().getEndPoint());

        boolean coverage = nearestEdge.getLineString().buffer(GeoProperties.BUFFER_TOLERANCE).covers(splittingNode.getPoint());
        assertTrue(coverage);
    }

    @Test
    public void testGetDistancePerNode() throws Exception {

    }

    @Test
    public void testGetDistancePerEdge() throws Exception {

    }

    @Test
    public void testGetSplitPerEdge() throws Exception {

    }

    @Test
    public void testGetProposedTrack() throws Exception {

    }
}