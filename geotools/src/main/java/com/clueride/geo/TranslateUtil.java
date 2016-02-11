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
 * Created Aug 16, 2015
 */
package com.clueride.geo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.DefaultNodeGroup;
import com.clueride.domain.GeoNode;
import com.clueride.domain.SegmentFeatureImpl;
import com.clueride.domain.dev.NodeGroup;
import com.clueride.domain.factory.NodeFactory;
import com.clueride.feature.Edge;
import com.clueride.feature.FeatureType;
import com.clueride.feature.LineFeature;

/**
 * Methods to translate from one type to another.
 *
 * @author jett
 *
 */
public class TranslateUtil {

    private static SimpleFeatureBuilder segmentFeatureBuilder = new SimpleFeatureBuilder(
            FeatureType.SEGMENT_FEATURE_TYPE);

    /**
     * This uses the SegmentTypeBuilder which knows about Segments and their
     * specific features.
     * 
     * @param segments
     *            - input
     * @return
     * @deprecated - Move toward List<LineFeature>
     */
    public static DefaultFeatureCollection segmentsToFeatureCollection(
            List<Edge> segments) {
        // SimpleFeatureBuilder segmentFeatureBuilder = new
        // SimpleFeatureBuilder(
        // FeatureType.SEGMENT_FEATURE_TYPE);
        DefaultFeatureCollection features;
        features = new DefaultFeatureCollection();
        for (Edge segment : segments) {
            SimpleFeature feature = segmentToFeature(segment);
            features.add(feature);
        }
        return features;
    }

    /**
     * @param segments
     * @return
     * @deprecated - Move toward Set<LineFeature>
     */
    public static DefaultFeatureCollection segmentsToFeatureCollection(
            Set<Edge> segments) {
        List<Edge> segList = new ArrayList<>();
        segList.addAll(segments);
        return segmentsToFeatureCollection(segList);
    }

    /**
     * @param segment
     * @return
     * @deprecated - Move toward LineFeature
     */
    public static SimpleFeature segmentToFeature(Edge segment) {
        segmentFeatureBuilder.add(segment.getId());
        segmentFeatureBuilder.add(segment.getDisplayName());
        segmentFeatureBuilder.add(segment.getUrl());
        segmentFeatureBuilder.add(false);
        // segmentFeatureBuilder.add(((SegmentImpl) segment).getLineString());
        SimpleFeature feature = segmentFeatureBuilder.buildFeature(null);
        return feature;
    }

    /**
     * @param geoNode
     * @return
     */
    public static SimpleFeature geoNodeToFeature(GeoNode geoNode) {
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(
                FeatureType.NODE_FEATURE_TYPE);
        featureBuilder.add(geoNode.getId());
        featureBuilder.add(geoNode.getName());
        featureBuilder.add(geoNode.getState());
        featureBuilder.add(geoNode.isSelected());
        featureBuilder.add(geoNode.getPoint());
        return featureBuilder.buildFeature(null);
    }

    /**
     * @param nodes
     * @return
     */
    public static DefaultFeatureCollection nodesToFeatureCollection(
            Set<GeoNode> nodes) {
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
        for (GeoNode geoNode : nodes) {
            SimpleFeature feature = geoNodeToFeature(geoNode);
            featureCollection.add(feature);
        }
        return featureCollection;
    }

    /**
     * @param geoNode
     * @return
     */
    public static SimpleFeature groupNodeToFeature(DefaultNodeGroup geoNode) {
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(
                FeatureType.NODE_GROUP_FEATURE_TYPE);
        featureBuilder.add(geoNode.getId());
        featureBuilder.add(geoNode.getName());
        featureBuilder.add(geoNode.getState());
        featureBuilder.add(geoNode.isSelected());
        featureBuilder.add(geoNode.getRadius());
        featureBuilder.add(geoNode.getPoint());
        return featureBuilder.buildFeature(null);
    }

    /**
     * @param nodeGroups
     * @return
     */
    public static DefaultFeatureCollection groupNodesToFeatureCollection(
            Set<NodeGroup> nodeGroups) {
        DefaultFeatureCollection features = new DefaultFeatureCollection();
        for (NodeGroup nodeGroup : nodeGroups) {
            SimpleFeature feature = groupNodeToFeature((DefaultNodeGroup) nodeGroup);
            features.add(feature);
        }
        return features;
    }

    /**
     * TODO: Replace this with the Recommendation class structure which is
     * intended to carry all this information over to a JSON string to be sent
     * to the client.
     * 
     * @param geoNode
     * @return
     */
    public static DefaultFeatureCollection geoNodeToFeatureCollection(
            GeoNode geoNode) {
        DefaultFeatureCollection features = new DefaultFeatureCollection();
        features.add(geoNodeToFeature(geoNode));
        for (GeoNode nearByNode : geoNode.getNearByNodes()) {
            features.add(geoNodeToFeature(nearByNode));
        }
        if (geoNode.getSelectedNode() != null) {
            features.add(geoNodeToFeature(geoNode.getSelectedNode()));
        }
        // TODO: Find where these were being added; they're in the wrong spot
        // for (Edge segment : geoNode.getSegments()) {
        // features.add(segment.getFeature());
        // }
        for (SimpleFeature trackFeature : geoNode.getTracks()) {
            features.add(trackFeatureToFeature(trackFeature));
        }
        if (geoNode.hasProposedTrack()) {
            features.add(geoNode.getProposedTrack().getFeature());
        }
        return features;
    }

    /**
     * @param trackFeature
     * @return
     */
    private static SimpleFeature trackFeatureToFeature(
            SimpleFeature trackFeature) {
        segmentFeatureBuilder.add(trackFeature.getAttribute("id"));
        segmentFeatureBuilder.add(trackFeature.getAttribute("name"));
        segmentFeatureBuilder.add(trackFeature.getAttribute("url"));
        segmentFeatureBuilder.add(false);
        segmentFeatureBuilder.add((LineString) trackFeature
                .getDefaultGeometry());
        return segmentFeatureBuilder.buildFeature(null);
    }

    /**
     * @param feature
     * @return
     */
    public static GeoNode featureToGeoNode(SimpleFeature feature) {
        Integer id = retrieveIdFromFeature(feature, "pointId");
        Point point = (Point) feature.getDefaultGeometry();
        // TODO: Return an interface that has the geometry methods
        DefaultGeoNode node = (DefaultGeoNode) NodeFactory.getInstance(point);
        node.setId(id);
        return node;
    }

    /**
     * @param feature
     * @return
     */
    public static NodeGroup featureToNodeGroup(SimpleFeature feature) {
        String idName = "nodeGroupId";
        Integer id = retrieveIdFromFeature(feature, idName);
        Point point = (Point) feature.getDefaultGeometry();
        Double radius = (Double) feature.getAttribute("radius");

        // TODO: Return an interface that has the geometry methods
        DefaultNodeGroup nodeGroup = (DefaultNodeGroup) NodeFactory
                .getGroupInstance(point, radius);
        nodeGroup.setId(id);

        return nodeGroup;
    }

    /**
     * @param feature
     * @param idName
     * @return
     */
    public static Integer retrieveIdFromFeature(SimpleFeature feature,
            String idName) {
        Integer id = null;
        Object idObject = feature.getAttribute(idName);
        if (idObject instanceof Integer) {
            id = (Integer) idObject;
        }
        if (idObject instanceof Long) {
            id = ((Long) idObject).intValue();
        }
        return id;
    }

    /**
     * @param features
     * @return
     */
    public static Set<LineFeature> featureCollectionToLineFeatures(
            DefaultFeatureCollection features) {
        Set<LineFeature> lineFeatureSet = new HashSet<>();
        for (SimpleFeature feature : features) {
            lineFeatureSet.add(new SegmentFeatureImpl(feature));
        }
        return lineFeatureSet;
    }

    /**
     * @param featureCollection
     * @return
     */
    public static Set<GeoNode> featureCollectionToNodes(
            DefaultFeatureCollection featureCollection) {
        Set<GeoNode> set = new HashSet<>();
        for (SimpleFeature feature : featureCollection) {
            set.add(featureToGeoNode(feature));
        }
        return set;
    }

    /**
     * @param featureCollection
     * @return
     */
    public static Set<NodeGroup> featureCollectionToNodeGroups(
            DefaultFeatureCollection featureCollection) {
        Set<NodeGroup> nodeGroupSet = new HashSet<>();
        for (SimpleFeature feature : featureCollection) {
            nodeGroupSet.add(featureToNodeGroup(feature));
        }
        return nodeGroupSet;
    }

    /**
     * Splits the lineString at the point closest to c.
     * 
     * @param moveSplitToTarget
     *            true to move the split point to the target; false to leave the
     *            split point at the closest point on the line to the target
     */
    public static LineString[] split(LineString lineString, Coordinate target,
            boolean moveSplitToTarget) {
        LengthToPoint lengthToPoint = new LengthToPoint(lineString, target);
        LengthSubstring lengthSubstring = new LengthSubstring(lineString);
        LineString[] newLineStrings = new LineString[] {
                lengthSubstring.getSubstring(0, lengthToPoint.getLength()),
                lengthSubstring.getSubstring(lengthToPoint.getLength(),
                        lineString.getLength()) };
        if (moveSplitToTarget) {
            newLineStrings[1].getStartPoint().getCoordinate()
                    .setCoordinate((Coordinate) target.clone());
        }
        // Make sure the coordinates are absolutely equal [Jon Aquino
        // 11/21/2003]
        newLineStrings[0]
                .getEndPoint()
                .getCoordinate()
                .setCoordinate(
                        newLineStrings[1].getStartPoint().getCoordinate());

        return newLineStrings;
    }

    /**
     * @param featureList
     * @return
     */
    public static DefaultFeatureCollection featureListToCollection(
            List<SimpleFeature> featureList) {
        DefaultFeatureCollection features = new DefaultFeatureCollection();
        features.addAll(featureList);
        return features;
    }

    /**
     * Extracts the feature and geometry (which is a LineString) from a Segment.
     * 
     * @param segment
     * @return LineString corresponding to the Segment.
     */
    public static LineString segmentToLineString(Edge segment) {
        return (LineString) segmentToFeature(segment).getDefaultGeometry();
    }


    /**
     * @param allLineFeatures - Line Features to be translated.
     * @return Feature Collection populated from a list of LineFeatures.
     */
    public static DefaultFeatureCollection lineFeatureSetToFeatureCollection(
            Set<LineFeature> allLineFeatures) {
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
        for (LineFeature lineFeature : allLineFeatures) {
            featureCollection.add(lineFeature.getFeature());
        }
        return featureCollection;
    }
}
