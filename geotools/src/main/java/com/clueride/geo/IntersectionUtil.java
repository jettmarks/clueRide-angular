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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

/**
 * Common Methods for determining intersections of tracks and segments.
 *
 * TODO: Need test for the case we get all the way to the end of the line.
 * 
 * @author jett
 */
public class IntersectionUtil {

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
     * @param index
     *            offset where the pair of interesting points resides.
     * @return
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
     * @param segmentLineString
     * @param trackPiece
     * @return 2-point LineString from first LineString representing pair of
     *         points which straddle second LineString.
     */
    public static LineString findCrossingPair(LineString lineStringA,
            LineString lineStringB) {
        int indexOfCrossing = findCrossingIndex(lineStringA, lineStringB);
        if (indexOfCrossing < 0)
            throw new RuntimeException("Crossing Index not found");
        return retrieveCrossingPair(lineStringA, indexOfCrossing);
    }

}
