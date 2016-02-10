/*
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
 * Created Aug 18, 2015
 */
package com.clueride.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import org.apache.log4j.Logger;
import org.geotools.feature.DefaultFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.config.GeoProperties;
import com.clueride.dao.DefaultNetworkStore;
import com.clueride.dao.DefaultNodeStore;
import com.clueride.dao.NetworkStore;
import com.clueride.dao.NodeStore;
import com.clueride.domain.DefaultNodeGroup;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NodeGroup;
import com.clueride.feature.LineFeature;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.LengthToPoint;
import com.clueride.geo.TranslateUtil;
import com.clueride.io.GeoJsonUtil;
import com.clueride.io.JsonStoreType;

/**
 * Holds collection of segments that make up the full set of connected Features
 * that we know about for a given area.
 *
 * Memory copy is a FeatureCollection of LineString (Segments) which is refreshed
 * when underlying Stores are refreshed.
 * 
 * @author jett
 */
public class DefaultNetwork implements Network {
    private static final Logger LOGGER = Logger.getLogger(DefaultNetwork.class);

    private static final Double LOC_GROUP_RADIUS_DEG = (Double) GeoProperties
            .getInstance().get("group.radius.degrees");

    // TODO: CA-64 Move to Guice Dependency injection
    private static DefaultNetwork instance = null;
    /** FeatureCollection remains the 'published' record, although the allLineFeatures
     * holds the "official" (transitional) data. */
    private DefaultFeatureCollection featureCollection;

    private Set<LineFeature> allLineFeatures;
    private List<LineString> allLineStrings = new ArrayList<>();
    private Set<GeoNode> nodeSet;
    private NetworkStore networkStore;
    private NodeStore nodeStore;
    private Map<Integer,Set<LineFeature>> lineFeaturesPerNodeId = new HashMap<>();

    public static DefaultNetwork getInstance() {
        // TODO: This is a big chunk of work to bite off within a synchronized block
        synchronized (DefaultNetwork.class) {
            if (instance == null) {
                instance = new DefaultNetwork();
            }
        }
        return (instance);
    }

    /**
     * Constructor that loads itself from stored location.
     * Opened up for Guice to get to.
     */
    public DefaultNetwork() {
        networkStore = DefaultNetworkStore.getInstance();
        allLineFeatures = networkStore.getLineFeatures();

        /* "Publish" the LineFeatures. */
        featureCollection = TranslateUtil
                .lineFeatureSetToFeatureCollection(allLineFeatures);

        nodeStore = DefaultNodeStore.getInstance();
        init();
    }

    /**
     * Currently sets up - List of Nodes (but not those with IDs) - List of Line
     * Strings (but Segments would be better) - Dumps a string summarizing what
     * we've got.
     *
     * Moving toward nodes with IDs, it would be good to persist the segments
     * that way, but we're not quite there yet. Dropping in a verification
     * process that checks the endpoints against the NodeStore's idea of our
     * node set.
     * @deprecated - only useful if the FeatureCollection comes before the LineFeatures;
     * no longer happening.
     * Well, I am dependent on the validateConnections now since it is building one
     * of my maps (as of CA-90).
     */
    private void init() {
        nodeSet = nodeStore.getNodes();
        allLineStrings = new ArrayList<>();

        for (SimpleFeature feature : featureCollection) {
            LineString lineString = (LineString) feature.getDefaultGeometry();
            if (lineString != null) {
                allLineStrings.add(lineString);
            }
        }
        for (LineFeature lineFeature : allLineFeatures) {
            validateConnections(lineFeature);
        }
        LOGGER.debug("Initialized : " + this);
    }

    /**
     * @param defaultFeatureCollection - if we have a FeatureCollection before we have a set of
     *                                 LineFeatures, this might be useful.
     * @deprecated Use the getInstance method instead.
     */
    DefaultNetwork(DefaultFeatureCollection defaultFeatureCollection) {
        featureCollection = defaultFeatureCollection;
        nodeStore = DefaultNodeStore.getInstance();
        init();
    }

    private void refresh() {
        /* Expects that the Line Strings come from the Feature Collection instead of the other way around. */
        nodeSet.clear();
        nodeSet = null;
        allLineStrings.clear();
        allLineStrings = null;
        init();
    }

    /**
     * @deprecated - deal in edges and segmentFeatures rather than simpleFeatures.
     */
    @Override
    public void add(TrackFeature trackFeature) {
        // TODO: CA-67 Coming out with the LoadService
        featureCollection.add(trackFeature.getFeature());
        refresh();
    }

    /**
     * This implementation takes features placed in the allLineFeatures
     * List, and brings them over to the FeatureCollection which is used
     * to serve many of the service requests -- particularly getNetworkForDisplay().
     */
    @Override
    public void storesReadyForPublishing() {
        try {
            // TODO: Is this what I want?
            networkStore.persist();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nodeStore.persist();
        featureCollection = TranslateUtil
                .lineFeatureSetToFeatureCollection(allLineFeatures);
    }

    @Override
    public List<LineFeature> getLineFeaturesForNodeId(Integer pointId) {
        return new ArrayList<>(lineFeaturesPerNodeId.get(pointId));
    }

    @Override
    public void refreshIndices() {
        lineFeaturesPerNodeId = new HashMap<>();
        for (LineFeature lineFeature : allLineFeatures) {
            validateConnections(lineFeature);
        }
    }

    /**
     * Current (12/26/2015) support for the "show the Network" REST end point.
     *
     * Stand-alone Nodes are brought back separately; the nodes are implied by the
     * ends of the Edges/Segments contained within.
     * @return JSON String representing a FeatureCollection with all the Segments.
     */
    public String getNetworkForDisplay() {
        LOGGER.debug("Requesting network for display");
        String result = "";
        GeoJsonUtil geoJsonUtil = new GeoJsonUtil(JsonStoreType.LOCATION);
        result = geoJsonUtil.toString(featureCollection);
        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DefaultNetwork [allSegmentsCount=" + featureCollection.size()
                + ", allLineStringsCount=" + allLineStrings.size()
                + ", nodeSetCount=" + nodeSet.size() + "]";
    }

    /* Bike Parking for methods on their way someplace more Bike Friendly. */
    /* Many of these have been deprecated. */

    /**
     * TODO: Find a better location for this.
     * @param geoNode
     * @return
     */
    public List<GeoNode> getSortedNodes(GeoNode geoNode) {
        final Geometry referencePoint = geoNode.getPoint();
        List<GeoNode> sorted = new ArrayList<>();
        sorted.addAll(nodeSet);
        Collections.sort(sorted, new Comparator<GeoNode>() {

            @Override
            public int compare(GeoNode o1, GeoNode o2) {
                double dist1 = o1.getPoint().distance(referencePoint);
                double dist2 = o2.getPoint().distance(referencePoint);
                return (int) Math.signum(dist1 - dist2);
            }

        });

        return sorted;
    }

    /**
     * TODO: Find a better location for this.
     * @param lineStringToStart
     * @param distanceToNode
     * @param node
     */
    public void determineDistance(LineString lineStringToStart,
                                  Map<GeoNode, Double> distanceToNode, GeoNode node) {
        if (lineStringToStart.buffer(GeoProperties.BUFFER_TOLERANCE).covers(
                node.getPoint())) {
            double distance = LengthToPoint.length(lineStringToStart, node
                    .getPoint().getCoordinate());
            LOGGER.debug("Node: " + node.getName()
                    + " at a distance of "
                    + distance);
            distanceToNode.put(node, distance);
        }
    }

    /**
     * TODO: Find a better location for this.
     * @param lineStringToStart
     * @param lineStringToEnd
     * @param nodesToMeasure
     * @return
     * @deprecated
     */
    public Map<GeoNode, Double> evaluateDistanceMapPerNode(
            LineString lineStringToStart,
            LineString lineStringToEnd,
            List<GeoNode> nodesToMeasure
    ) {
        Map<GeoNode, Double> distanceToNode = new HashMap<>();
        for (GeoNode node : nodesToMeasure) {
            determineDistance(lineStringToStart, distanceToNode, node);
            determineDistance(lineStringToEnd, distanceToNode, node);
        }
        return distanceToNode;
    }

    /**
     * TODO: Move this to a class which would perform the evaluation (start with NodeService impl's build Proposal).
     * @param geoNode
     * @return
     * @deprecated
     */
    private Integer withinLocationGroup(GeoNode geoNode) {
        Integer matchingId = -1;
        Set<NodeGroup> locGroups = DefaultNodeStore.getInstance()
                .getNodeGroups();
        for (NodeGroup nodeGroup : locGroups) {
            Point point = ((DefaultNodeGroup) nodeGroup).getPoint();
            if (point.buffer(LOC_GROUP_RADIUS_DEG).covers(geoNode.getPoint())) {
                matchingId = nodeGroup.getId();
            }
        }
        return matchingId;
    }

    /* Node & Edge endpoint validation; geotools service package is a better spot. */

    /**
     * Checks that each end of the Line Feature is a registered Node.
     *
     * Has the added responsibility of adding the Line Features to the
     * Map of features per Node by ID and adding the verified Nodes to the Feature.
     */
    private boolean validateConnections(LineFeature lineFeature) {
        Integer startNodeId = verifyNodeIdAssignment(lineFeature.getGeoStart());
        Integer endNodeId = verifyNodeIdAssignment(lineFeature.getGeoEnd());

        addNodesFeature(lineFeature, startNodeId);
        addNodesFeature(lineFeature, endNodeId);

        if (startNodeId > 0 && endNodeId > 0) {
            return true;
        } else {
            LOGGER.error("Line Feature " + lineFeature.getId() + " has unregistered nodes");
            return false;
        }
    }

    private void addNodesFeature(LineFeature lineFeature, Integer nodeId) {
        if (nodeId < 0) {
            return;
        }

        Set<LineFeature> featuresForNode;
        if (!lineFeaturesPerNodeId.containsKey(nodeId)) {
            featuresForNode = new HashSet<>();
            lineFeaturesPerNodeId.put(nodeId, featuresForNode);
        } else {
            featuresForNode = lineFeaturesPerNodeId.get(nodeId);
        }
        featuresForNode.add(lineFeature);
    }

    /**
     * Checks that the Endpoint Node provided lies within the pool of known Nodes.
     * TODO: Factor this into a separate class.  See GeoEval class in geotools module.
     *
     * @param endPointNode - either start or end of a LineString.
     */
    public Integer verifyNodeIdAssignment(GeoNode endPointNode) {
        Integer nodeId = matchesNetworkNode(endPointNode);
        if (nodeId > 0) {
            endPointNode.setId(nodeId);
        } else {
            LOGGER.error("Node " + endPointNode
                    + " doesn't match stored list of nodes");
        }
        return nodeId;
    }

    public Integer matchesNetworkNode(GeoNode geoNode) {
        for (GeoNode node : nodeSet) {
            if (node.matchesLocation(geoNode)) {
                return node.getId();
            }
        }
        return -1;
    }
}
