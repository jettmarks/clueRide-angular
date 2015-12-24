/**
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
 * Created Sep 26, 2015
 */
package com.clueride.geo;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.clueride.config.GeoProperties;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * Common Methods for determining intersections of tracks and segments.
 *
 * TODO: Need test for the case we get all the way to the end of the line.
 * 
 * @author jett
 */
public class IntersectionUtil {

    private static final Logger LOGGER = Logger.getLogger(IntersectionUtil.class);

    public static int findCrossingIndex(LineString inputLineString,
            LineString fixedLineString) {
        int indexOfCrossing = -1;
        GeometryFactory factory = fixedLineString.getFactory();

        Coordinate[] coordinates = inputLineString.getCoordinates();
        int length = coordinates.length;

        for (int i = 0; i < (length - 1); i++) {
            LineString walkableLineString = factory.createLineString(Arrays
                    .copyOfRange(coordinates, i, length));
            // System.out.println("Checking index " + i + " : "
            // + walkableLineString);
            if (!walkableLineString.crosses(fixedLineString)) {
                System.out.println("Breaking out");
                break;
            }
            indexOfCrossing = i;
        }
        return indexOfCrossing;
    }

    /**
     * Given a LineString and an index, return the 2-point piece of LineString
     * that sits at the index.
     * 
     * @param walkableLineString
     *            - source of the coordinates and the factory for creating a new
     *            LineString.
     * @param indexOfCrossing
     *            - offset where the pair of interesting points resides.
     * @return 2-point LineString representing the crossing segment.
     */
    public static LineString retrieveCrossingPair(
            LineString walkableLineString,
            int indexOfCrossing) {
        Coordinate[] coordinates = walkableLineString.getCoordinates();
        Coordinate[] crossingPair = Arrays.copyOfRange(coordinates,
                indexOfCrossing, indexOfCrossing + 2);
        return walkableLineString.getFactory().createLineString(crossingPair);
    }

    /**
     * Handles walking the first LineString to find the pair of points which
     * cross the second LineString and returns a 2-point LineString containing
     * that pair of points.
     * 
     * @param lineStringA - First LineString to check.
     * @param lineStringB - Second LineString to check.
     * @return 2-point LineString from first LineString representing pair of
     *         points which straddle second LineString.
     */
    public static LineString findCrossingPair(LineString lineStringA,
            LineString lineStringB) {
        int indexOfCrossing = findCrossingIndex(lineStringA, lineStringB);
        if (indexOfCrossing < 0) {
            LOGGER.error("Problematic LineStrings: \n  " + lineStringA + "\n  "
                    + lineStringB);
            throw new RuntimeException("Crossing Index not found");
        }
        return retrieveCrossingPair(lineStringA, indexOfCrossing);
    }

    /**
     * Returns the Point at which the first LineString first meets (or crosses)
     * the second LineString.
     * 
     * Algorithm is to use a binary search along the length of the LineString to
     * locate the point at which the two first intersect.
     * 
     * @param walkingLineString - Candidate Track.
     * @param fixedLineString - Existing Network Edge.
     * @return Point where the two LineString first intersect.
     */
    public static Point findFirstIntersection(LineString walkingLineString,
            LineString fixedLineString) {
        int lastIndex = walkingLineString.getNumPoints();
        int interval = lastIndex / 2 + lastIndex % 2;
        int currentIndex = interval;
        interval = interval / 2 + currentIndex % 2;
        LOGGER.info("Begin search with length: "
                + walkingLineString.getNumPoints()
                + " interval of " + interval + " and a starting index of "
                + currentIndex);

        GeometryFactory factory = walkingLineString.getFactory();

        LineString lsTest = null;
        boolean done = false;
        while (!done) {
            LOGGER.info("CurrentIndex: " + currentIndex + " Interval: "
                    + interval);
            Coordinate[] coordinates = Arrays.copyOfRange(
                    walkingLineString.getCoordinates(), 0, currentIndex);
            lsTest = factory.createLineString(coordinates);
            if (lsTest.intersects(fixedLineString)
                    || lsTest.crosses(fixedLineString)) {

                if (interval > 1) {
                    currentIndex -= interval;
                    currentIndex = (currentIndex < 2 ? 2 : currentIndex);
                } else {
                    done = true;
                }
            } else {
                currentIndex += interval;
            }
            interval = interval / 2 + interval % 2;
        }

        LOGGER.debug("Candidate LineString (size " + lsTest.getNumPoints()
                + "): " + lsTest);
        // Intersection will be inside of this Geometry
        Geometry intersectionGeo = lsTest.intersection(fixedLineString);
        if (intersectionGeo.isEmpty()) {
            LOGGER.warn("Intersection Not found");
            return null;
        }
        if (!intersectionGeo.getGeometryType().equals("Point")) {
            LOGGER.warn("Unexpected Geometry: "
                    + intersectionGeo.getGeometryType());
            return null;
        }
        Point point = (Point) lsTest.intersection(fixedLineString);
        LOGGER.debug("Intersection at " + point);
        return point;
    }

    /**
     * Brute force determination of the best point to represent the intersection of two
     * LineString instances.
     *
     * First attempt is to walk the candidate string (walking) toward a network string (fixed)
     * and stop when the point is within the boundary of the network string.  However, there
     * may be better points if we walk just a bit further.  The revised algorithm involves
     * compiling a list of those within the boundary and then selecting which of those is the
     * best fit.
     * 
     * @param lsWalking - represents the candidate track.
     * @param lsFixed - represents a network edge.
     * @return Point where the two meet and would serve as a good candidate for a Splitting Node.
     */
    public static Point walkToIntersectingNode(LineString lsWalking,
            LineString lsFixed) {
        Point selectedPoint = null;
        Geometry lsBuffer = lsFixed.buffer(GeoProperties.BUFFER_TOLERANCE);
        Double minDistance = Double.MAX_VALUE;
        Point walkingPoint;
        int numberPoints = lsWalking.getNumPoints();
        for (int p = 0; p < numberPoints; p++) {
            walkingPoint = lsWalking.getPointN(p);
            if (lsBuffer.covers(walkingPoint)) {
                Double distance = walkingPoint.distance(lsFixed);
                if (distance < minDistance) {
                    minDistance = distance;
                    selectedPoint = walkingPoint;
                    LOGGER.info("We like point " + selectedPoint + " at a distance of " + distance);
                }
            }
        }
        return selectedPoint;
    }
}
