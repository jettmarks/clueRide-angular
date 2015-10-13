package com.clueride.io;

import static org.testng.AssertJUnit.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.geotools.feature.DefaultFeatureCollection;
import org.opengis.feature.simple.SimpleFeatureType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class JsonUtilTest {

    private JsonUtil toTest;

    @BeforeMethod
    public void setup() {
        JsonStoreLocation.setTestMode();
    }

    @Test
    public void readSegmentSchema() {
        toTest = new JsonUtil(JsonStoreType.NETWORK);
        File file = new File("mainNetwork.geojson");
        SimpleFeatureType featureType = toTest.readSchema(file);
        assertNotNull(featureType);
        System.out.println(featureType);
    }

    @Test
    public void setSegmentSchema() {
        toTest = new JsonUtil(JsonStoreType.NETWORK);
        toTest.setSchemaType();
        DefaultFeatureCollection features = null;
        try {
            features = toTest
                    .readFeatureCollection("mainNetwork.geojson");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertNotNull(features);
        System.out.println(features.getSchema());
    }

    // @Test
    public void readTrackSchema() {
        toTest = new JsonUtil(JsonStoreType.RAW);
        File file = new File("514656.geojson");
        SimpleFeatureType featureType = toTest.readSchema(file);
        System.out.println(featureType);
    }
}
