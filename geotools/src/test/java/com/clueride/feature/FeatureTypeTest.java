package com.clueride.feature;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.testng.annotations.Test;

public class FeatureTypeTest {

    @Test
    public void buildSegmentInstance() {
        SimpleFeatureBuilder segmentFeatureBuilder = new SimpleFeatureBuilder(
                FeatureType.SEGMENT_FEATURE_TYPE);
        segmentFeatureBuilder.add(12);
        segmentFeatureBuilder.add("Display Name");
        segmentFeatureBuilder.add("http://ridewithgps.com");
        segmentFeatureBuilder.add(false);
        SimpleFeature feature = segmentFeatureBuilder.buildFeature(null);

        System.out.println(feature);
    }
}
