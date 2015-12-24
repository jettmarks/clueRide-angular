/*
 *   Copyright 2015 Jett Marks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created Nov 03, 2015
 */
package com.clueride.service;

import com.vividsolutions.jts.geom.Point;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.config.GeoProperties;
import com.clueride.dao.DefaultTrackStore;
import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.GeoNode;
import com.clueride.domain.factory.PointFactory;
import com.clueride.feature.Edge;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.SplitLineString;
import static com.clueride.geo.SplitLineString.END;
import static com.clueride.geo.SplitLineString.START;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Exercises the TrackEval class, particularly the determination of recommendable
 * tracks and the "splitting node".
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
        System.out.println("Node Distance: " + toTest.getNodeDistance());
    }

    @Test
    public void testGetEdgeDistance() throws Exception {
        System.out.println("Edge Distance: " + toTest.getEdgeDistance());
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
    public void testGetSplittingNodePiedmontPark() throws Exception {
        Point point = PointFactory.getJtsInstance(33.78639, -84.377575, 270.4);
        GeoNode newNode = new DefaultGeoNode(point);
        TrackFeature track = DefaultTrackStore.getInstance().getTrackById(20);
        assertNotNull(track);
        SplitLineString splitLineString = new SplitLineString(track, newNode);
        toTest = new TrackEval(splitLineString.getSubTrackFeature(START));

        Edge nearestEdge = toTest.getNearestNetworkEdge();
        assertNotNull(nearestEdge);
        System.out.println("Nearest Edge: " + nearestEdge);

        GeoNode splittingNode = toTest.getSplittingNode();
        assertNotNull(splittingNode);
        System.out.println("Splitting Node as Point: "+splittingNode.getPoint());

        TrackFeature proposedTrack = toTest.getProposedTrack();
        assertEquals(splittingNode.getPoint(), proposedTrack.getLineString().getEndPoint());

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