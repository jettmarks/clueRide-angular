package com.clueride.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.feature.DefaultFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.feature.Edge;
import com.clueride.feature.SegmentFeature;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

public class GeoJsonUtilTest {

    private GeoJsonUtil toTest;

    @BeforeMethod
    public void setup() {
        JsonStoreLocation.setTestMode();
    }

    /**
     * Exercises the creation of network edges in memory from the files on disk.
     */
    @Test
    public void readEdges() {
        toTest = new GeoJsonUtil(JsonStoreType.EDGE);
        try {
            List<Edge> edges = toTest.readEdges();
            assertNotNull(edges);

            int edgeCount = edges.size();
            System.out.println("Number of Edges read is "+edgeCount);
            assertTrue(edgeCount > 0);

            Edge edgeToTest = edges.get(0);
            SimpleFeature feature = edgeToTest.getFeature();
            assertNotNull(feature);

            FeatureType featureType = feature.getFeatureType();
            assertEquals("http://clueride.com/:EdgeFeatureType", featureType.getName().toString());

            Geometry geometry = edgeToTest.getGeometry();
            assertNotNull(geometry);
            assertNotNull(geometry.getGeometryType());
            assertEquals("LineString", geometry.getGeometryType().toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exercises the creation of network segments in memory from the files on disk.
     */
    @Test
    public void readSegments() {
        toTest = new GeoJsonUtil(JsonStoreType.NETWORK);
        try {
            List<SegmentFeature> segments = toTest.readSegments();
            assertNotNull(segments);
            System.out.println("Number of Segments read is " + segments.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Test
    public void readSegmentSchema() {
        toTest = new GeoJsonUtil(JsonStoreType.LOCATION);
        File file = new File("loc-00001/loc-00001.geojson");
        SimpleFeatureType featureType = toTest.readSchema(file);
        assertNotNull(featureType);
        System.out.println(featureType);
    }

//    @Test
    public void setSegmentSchema() {
        toTest = new GeoJsonUtil(JsonStoreType.LOCATION);
        toTest.setSchemaType();
        DefaultFeatureCollection features = null;
        try {
            features = toTest.readFeatureCollection("loc-00001/loc-00001.geojson");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertNotNull(features);
        System.out.println(features.getSchema());
    }

//     @Test
    public void readTrackSchema() {
        toTest = new GeoJsonUtil(JsonStoreType.RAW);
        File file = new File("514656.geojson");
        SimpleFeatureType featureType = toTest.readSchema(file);
        System.out.println(featureType);
    }
}
