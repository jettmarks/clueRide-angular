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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

public class LineStringIntersect {

    static Coordinate[] coordinateArrayA = {
            new Coordinate(12.0, 2.0),
            new Coordinate(12.0, 13.0),
            new Coordinate(12.0, 19.0)
    };

    static Coordinate[] coordinateArrayB = {
            new Coordinate(2.0, 10.0),
            new Coordinate(10.0, 10.0),
            new Coordinate(21.0, 11.0)
    };

    static Coordinate[] coordinateArrayC = {
            new Coordinate(1.0, 1.0),
            new Coordinate(9.0, 9.0),
            new Coordinate(20.0, 20.0)
    };

    static GeometryFactory geometryFactory = new GeometryFactory();

    static LineString lineStringA = geometryFactory
            .createLineString(coordinateArrayA);
    static LineString lineStringB = geometryFactory
            .createLineString(coordinateArrayB);
    static LineString lineStringC = geometryFactory
            .createLineString(coordinateArrayC);

    static Geometry geometryAB = lineStringA.intersection(lineStringB);
    static Geometry geometryAC = lineStringA.intersection(lineStringC);
    static Geometry geometryBC = lineStringB.intersection(lineStringC);

    public static void main(String args[]) {
        System.out.println("AB: " + geometryAB);
        System.out.println("AC: " + geometryAC);
        System.out.println("BC: " + geometryBC);
    }

}
