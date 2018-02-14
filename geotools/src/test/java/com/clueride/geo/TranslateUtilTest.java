package com.clueride.geo;

import org.opengis.feature.simple.SimpleFeature;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.GeoNode;

public class TranslateUtilTest {
    @BeforeMethod
    public void beforeMethod() {
    }

    @Test
    public void geoNodeToFeature() {
        GeoNode geoNode = new DefaultGeoNode();
        SimpleFeature feature = TranslateUtil.geoNodeToFeature(geoNode);
        System.out.println(feature.getClass().getName());
    }
}
