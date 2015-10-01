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
 * Created Sep 30, 2015
 */
package com.clueride.poc.geotools;

import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

/**
 * Demonstration of intersect and crosses.
 * 
 * Only, everything is intersecting just fine.
 *
 * @author jett
 *
 */
public class CopyOfLineStringIntersect {

    static Coordinate[] coordinateArrayA = {
            new Coordinate(0.000001, 0.0),
            new Coordinate(2.0, 2.0)
    };

    static Coordinate[] coordinateArrayB = {
            new Coordinate(2.0, 0.0),
            new Coordinate(0.0, 2.0)
    };
    static Coordinate[] coordinateArrayX = {
            new Coordinate(1.0, 0.0),
            new Coordinate(1.0, 2.0)
    };

    static Coordinate[] coordinateArrayY = {
            new Coordinate(0.0, 1.0),
            new Coordinate(2.0, 1.00000000001)
    };

    static Coordinate[] coordinateArrayC = {
            new Coordinate(2.0, 0.0),
            new Coordinate(0.0, 2.000001)
    };

    static GeometryFactory geometryFactory = JTSFactoryFinder
            .getGeometryFactory(null);
    static LineString lineStringA = geometryFactory
            .createLineString(coordinateArrayA);
    static LineString lineStringB = geometryFactory
            .createLineString(coordinateArrayB);
    static LineString lineStringC = geometryFactory
            .createLineString(coordinateArrayC);

    static LineString lineStringX = geometryFactory
            .createLineString(coordinateArrayX);
    static LineString lineStringY = geometryFactory
            .createLineString(coordinateArrayY);

    static Geometry geometryOK = lineStringX.intersection(lineStringY);
    static Geometry geometryStillOK = lineStringA.intersection(lineStringC);

    static boolean intersectAB = lineStringA.intersects(lineStringB);
    static boolean intersectAC = lineStringA.intersects(lineStringC);
    static boolean intersectXY = lineStringX.intersects(lineStringY);

    public static void main(String args[]) {
        System.out.println(geometryOK);
        System.out.println(geometryStillOK);
        System.out.println("AB Intersect: " + intersectAB);
        System.out.println("AC Intersect: " + intersectAC);
        System.out.println("XY Intersect: " + intersectXY);
    }

}
