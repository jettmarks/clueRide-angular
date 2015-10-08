package com.clueride.domain;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.clueride.domain.factory.NodeFactory;
import com.clueride.domain.factory.PointFactory;
import com.clueride.geo.TransformUtil;
import com.vividsolutions.jts.geom.Point;

public class NodeImplTest {

    @Test
    public void testEquals() {
        Point point1 = PointFactory.getJtsInstance(33.12345, -84.12345, 333.0);
        Point point2 = PointFactory.getJtsInstance(33.12345, -84.12345, 333.0);
        GeoNode expected = (GeoNode) NodeFactory.getInstance(point1);
        GeoNode actual = (GeoNode) NodeFactory.getInstance(point2);
        assertEquals(expected, actual);

        actual = TransformUtil.adjustNode(actual, 0.00000, 0.00000);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        Point point1 = PointFactory.getJtsInstance(-84.12345, 33.12345, 333.0);
        Point point2 = PointFactory.getJtsInstance(-84.12345, 33.12345, 333.0);
        GeoNode expected = (GeoNode) NodeFactory.getInstance(point1);
        GeoNode actual = (GeoNode) NodeFactory.getInstance(point2);
        assertEquals(expected.hashCode(), actual.hashCode());
    }
}
