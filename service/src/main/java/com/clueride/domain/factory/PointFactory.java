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
 * Created Aug 22, 2015
 */
package com.clueride.domain.factory;

import java.text.ParseException;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.text.WKTParser;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Builds a JTS Point from a lat/lon with the default precision and spatial
 * reference.
 * 
 * For String parsing, we use the GeoTools API for creating a GeoTools Point and
 * then use that to create the JTS Point.
 *
 * @author jett
 */
public class PointFactory {
    public static Point getJtsInstance(double latitude, double longitude,
            double elevation) {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Point point = null;
        CoordinateSequenceFactory coordinateSequenceFactory = geometryFactory
                .getCoordinateSequenceFactory();
        CoordinateSequence coordinateSequence = coordinateSequenceFactory
                .create(1, 3);
        coordinateSequence.setOrdinate(0, CoordinateSequence.X, longitude);
        coordinateSequence.setOrdinate(0, CoordinateSequence.Y, latitude);
        coordinateSequence.setOrdinate(0, CoordinateSequence.Z, elevation);
        point = geometryFactory.createPoint(coordinateSequence);
        return point;
    }

    // public static Point getInstance(double lat, double lon, double elevation)
    // {
    // GeometryBuilder builder = new GeometryBuilder(
    // DefaultGeographicCRS.WGS84_3D);
    // return (Point) builder.createPoint(lat, lon, elevation);
    // }

    public static Point getInstance(String wktStringWithElevation)
            throws ParseException {
        WKTParser parser = new WKTParser(new GeometryBuilder(
                DefaultGeographicCRS.WGS84_3D));
        org.opengis.geometry.primitive.Point point = (org.opengis.geometry.primitive.Point) parser
                .parse(wktStringWithElevation);
        DirectPosition position = point.getDirectPosition();
        return getJtsInstance(position.getOrdinate(1), position.getOrdinate(0),
                position.getOrdinate(2));
    }
}
