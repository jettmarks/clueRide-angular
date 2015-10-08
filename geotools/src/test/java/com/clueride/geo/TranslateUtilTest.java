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
    public void featureCollectionToNodeGroups() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void featureCollectionToNodes() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void featureCollectionToSegments() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void featureListToCollection() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void featureToGeoNode() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void featureToNodeGroup() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void featureToSegment() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void geoNodeToFeature() {
        GeoNode geoNode = new DefaultGeoNode();
        SimpleFeature feature = TranslateUtil.geoNodeToFeature(geoNode);
        System.out.println(feature.getClass().getName());
    }

    @Test
    public void geoNodeToFeatureCollection() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void groupNodeToFeature() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void groupNodesToFeatureCollection() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void lineFeaturesToFeatureCollection() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void lineStringToFeature() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void lineStringToSegmentMapTrackLineString() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void lineStringToSegmentLineString() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void nodesToFeatureCollection() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void retrieveIdFromFeature() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void segmentToFeature() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void segmentToLineString() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void segmentsToFeatureCollectionListSegment() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void segmentsToFeatureCollectionSetSegment() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void split() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void trackFeatureToFeature() {
        throw new RuntimeException("Test not implemented");
    }
}
