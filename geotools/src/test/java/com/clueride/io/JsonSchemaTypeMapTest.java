package com.clueride.io;

import org.geotools.feature.DefaultFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.*;

public class JsonSchemaTypeMapTest {

    @Test
    public void getClassName() {
        String expectedClassName = "TrackFeatureImpl";
        String actualClassName = JsonSchemaTypeMap.getClass(JsonStoreType.RAW)
                .getSimpleName();
        assertEquals(actualClassName, expectedClassName);
    }

    @Test
    public void getSchema() {
        String expectedSchemaName = "TrackFeatureType";
        String actualSchemaName = JsonSchemaTypeMap
                .getSchema(JsonStoreType.RAW).getTypeName();
        assertEquals(actualSchemaName, expectedSchemaName);
    }

    @Test
    public void showAllSchemaNames() {
        for (JsonStoreType storeType : JsonStoreType.values()) {
            SimpleFeatureType featureType = JsonSchemaTypeMap
                    .getSchema(storeType);
            if (featureType != null) {
                System.out
                        .println(storeType + ": " + featureType.getTypeName());
            } else {
                System.out.println(storeType + ": not available");
            }
        }
    }

    @Test
    public void getRawRecord() {
        JsonStoreType storeType = JsonStoreType.RAW;
        GeoJsonUtil geoJsonUtil = new GeoJsonUtil(storeType);
        JsonStoreLocation.setTestMode();
        String fullPath = JsonStoreLocation.toString(storeType)
                + File.separator + "514656.geojson";
        File file = new File(fullPath);
        assertTrue(file.canRead());
        SimpleFeature feature = geoJsonUtil.readFeature(file);
        assertNotNull(feature);
        assertEquals(feature.getFeatureType().getTypeName(), "TrackFeatureType");
        assertNotNull(feature.getAttribute("trackId"));
        assertNotNull(feature.getAttribute("url"));
        assertNotNull(feature.getAttribute("name"));
        assertNotNull(feature.getAttribute("the_geom"));
    }

    @Test
    public void getNetworkRecord() throws IOException {
        JsonStoreType storeType = JsonStoreType.NETWORK;
        GeoJsonUtil geoJsonUtil = new GeoJsonUtil(storeType);
        JsonStoreLocation.setTestMode();

        DefaultFeatureCollection featureCollection = geoJsonUtil
                .readFeatureCollection();
        assertNotNull(featureCollection);

        dumpFeatureCollection(featureCollection);
        SimpleFeature feature = featureCollection.features().next();
        feature = featureCollection.features().next();

        assertEquals(feature.getFeatureType().getTypeName(),
                "SegmentFeatureType");
        assertNotNull(feature.getAttribute("segId"));
        assertNotNull(feature.getAttribute("url"));
        assertNotNull(feature.getAttribute("name"));
        assertNotNull(feature.getAttribute("selected"));
        assertNotNull(feature.getAttribute("the_geom"));
    }

    /**
     * @param featureCollection
     */
    private void dumpFeatureCollection(
            DefaultFeatureCollection featureCollection) {

        for (SimpleFeature feature : featureCollection) {
            System.out.println(feature.getAttributes());
        }
    }
}
