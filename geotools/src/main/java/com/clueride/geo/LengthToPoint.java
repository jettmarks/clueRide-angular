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
 * Created Aug 30, 2015
 */
package com.clueride.geo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.LineString;

/**
 * Description.
 *
 * @author jett
 *
 */

/**
 * Computes the length along a LineString to the point on the line nearest a
 * given point.
 */
public class LengthToPoint {
    public static double lengthAlongSegment(LineSegment seg, Coordinate pt) {
        double projFactor = seg.projectionFactor(pt);
        double len = 0.0;
        if (projFactor <= 0.0)
            len = 0.0;
        else if (projFactor <= 1.0)
            len = projFactor * seg.getLength();
        else
            len = seg.getLength();
        return len;
    }

    /**
     * Computes the length along a LineString to the point on the line nearest a
     * given point.
     */
    public static double length(LineString line, Coordinate inputPt) {
        LengthToPoint lp = new LengthToPoint(line, inputPt);
        return lp.getLength();
    }

    private double minDistanceToPoint;
    private double locationLength;

    public LengthToPoint(LineString line, Coordinate inputPt) {
        computeLength(line, inputPt);
    }

    public double getLength() {
        return locationLength;
    }

    private void computeLength(LineString line, Coordinate inputPt) {
        minDistanceToPoint = Double.MAX_VALUE;
        double baseLocationDistance = 0.0;
        Coordinate[] pts = line.getCoordinates();
        LineSegment seg = new LineSegment();
        for (int i = 0; i < pts.length - 1; i++) {
            seg.p0 = pts[i];
            seg.p1 = pts[i + 1];
            updateLength(seg, inputPt, baseLocationDistance);
            baseLocationDistance += seg.getLength();

        }
    }

    private void updateLength(LineSegment seg, Coordinate inputPt,
            double segStartLocationDistance) {
        double dist = seg.distance(inputPt);
        if (dist > minDistanceToPoint)
            return;
        minDistanceToPoint = dist;
        // found new minimum, so compute location distance of point
        double projFactor = seg.projectionFactor(inputPt);
        if (projFactor <= 0.0)
            locationLength = segStartLocationDistance;
        else if (projFactor <= 1.0)
            locationLength = segStartLocationDistance + projFactor
                    * seg.getLength();
        else
            locationLength = segStartLocationDistance + seg.getLength();
    }
}
