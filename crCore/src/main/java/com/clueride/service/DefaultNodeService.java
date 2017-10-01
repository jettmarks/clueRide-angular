/*
 * Copyright 2015 Jett Marks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by jett on 12/13/15.
 */
package com.clueride.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.dao.NetworkProposalStore;
import com.clueride.dao.NodeStore;
import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.DefaultNodeGroup;
import com.clueride.domain.EdgeImpl;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkProposal;
import com.clueride.domain.dev.NetworkRecommendation;
import com.clueride.domain.dev.NewNodeProposal;
import com.clueride.domain.dev.NodeEditProposal;
import com.clueride.domain.dev.NodeGroup;
import com.clueride.domain.dev.NodeNetworkState;
import com.clueride.domain.dev.rec.DiagnosticRec;
import com.clueride.domain.dev.rec.Rec;
import com.clueride.domain.factory.PointFactory;
import com.clueride.domain.user.latlon.LatLonStore;
import com.clueride.feature.Edge;
import com.clueride.feature.LineFeature;
import com.clueride.geo.TranslateUtil;
import com.clueride.io.GeoJsonUtil;
import com.clueride.io.JsonStoreType;

/**
 * Default Implementation of NodeService.
 */
public class DefaultNodeService implements NodeService {
    private static final Logger LOGGER = Logger.getLogger(DefaultNodeService.class);

    private final NodeStore jsonNodeStore;
    private final LatLonStore latLonStore;
    private final RecommendationService recommendationService;

    /**
     * Injectable constructor.
     * @param jsonNodeStore - persistence of Nodes.
     * @param recommendationService - supports requests to add to the Network.
     */
    @Inject
    public DefaultNodeService(
            NodeStore jsonNodeStore,
            LatLonStore latLonStore,
            RecommendationService recommendationService
    ) {
        this.jsonNodeStore = jsonNodeStore;
        this.latLonStore = latLonStore;
        this.recommendationService = recommendationService;
    }

    @Override
    public Point getPointByNodeId(Integer nodeId) {
        GeoNode node = (GeoNode) jsonNodeStore.getNodeById(nodeId);
        return node.getPoint();
    }

    @Override
    public String addNewNode(Double lat, Double lon) {
        GeoNode newNode;
        newNode = getCandidateNode(lat, lon);
        NetworkProposal networkProposal = recommendationService.buildProposalForNewNode(newNode);
        NetworkProposalStore.add(networkProposal);
        ProposalSummary proposalSummary = new ProposalSummary((NewNodeProposal) networkProposal);
        return proposalSummary.toJson();
    }

    /* Node Group Endpoint. */

    @Override
    public String getNodeGroups() {
        List<SimpleFeature> featureList = new ArrayList<>();

        Set<NodeGroup> nodeGroups = jsonNodeStore.getNodeGroups();
        for (NodeGroup nodeGroup : nodeGroups) {
            featureList.add(TranslateUtil
                    .groupNodeToFeature((DefaultNodeGroup) nodeGroup));
        }

        GeoJsonUtil geoJsonUtil = new GeoJsonUtil(JsonStoreType.LOCATION);
        return (geoJsonUtil.toString(featureList));
    }

    @Override
    // TODO: Copy/Pasted in without testing
    public String setNodeGroup(Integer id, Double lat, Double lon) {
        NodeGroup nodeGroup = (NodeGroup) jsonNodeStore.getNodeById(id);
        nodeGroup.setLat(lat);
        nodeGroup.setLon(lon);
        try {
            jsonNodeStore.persistAndReload();
        } catch (IOException e) {
            e.printStackTrace();
            return "{status: failed}";
        }
        return "{status: ok}";
    }

    @Override
    public String showAllNodes() {
        LOGGER.info("Requesting All Nodes");
        NetworkProposal networkProposal = buildAllNodesProposal();
        return networkProposal.toJson();
    }

    @Override
    public String getMatchingSegments(Integer pointId) {
        LOGGER.info("Requesting Matching Edges for Node ID " + pointId);
        NetworkProposal networkProposal = buildAdjustedEdgesProposal(pointId);
        return networkProposal.toJson();
    }

    @Override
    public String getEdgesAtNewLocation(Integer pointId, Double lat, Double lng) {
        LOGGER.info("Requesting updated Edges for Node ID " + pointId +
                " at new location (" + lat + "," + lng + ")");
        NetworkProposal networkProposal = buildAdjustedEdgesProposal(pointId, lat, lng);
        return networkProposal.toJson();
    }

    @Override
    public String confirmEdgesAtNewLocation(Integer pointId) {
        LOGGER.info("Confirming updated Edges for Node ID " + pointId);
        NetworkProposal networkProposal = NetworkProposalStore
                .getLastProposal();

        if (networkProposal == null) {
            return "{\"status\": \"No Proposal Found\"}";
        }

        Rec rec = (Rec) networkProposal.getRecommendations().get(0);
        if (rec == null) {
            return "{\"status\": \"No Recommendation Found\"}";
        } else {
            // Node first
            try {
                rec.getNewNode().setId(pointId);
                jsonNodeStore.persistNode(rec.getNewNode());
            } catch (IOException e) {
                LOGGER.error("Unable to persist updated Node");
                e.printStackTrace();
                return "{\"status\": \"Exception while persisting Node\"}";
            }

            for (SimpleFeature feature : rec.getFeatureCollection()) {
                LOGGER.debug(feature);
                SegmentService.updateSegment(feature);
            }
            return "{\"status\": \"OK\"}";
        }
    }

    /**
     * Turns lat/lon pair into skeleton GeoNode instance.
     * @param lat - Latitude of Node.
     * @param lon - Longitude of Node.
     * @return GeoNode representing a point being considered.
     */
    private GeoNode getCandidateNode(Double lat, Double lon) {
        GeoNode newLocation;
        Point point = PointFactory.getJtsInstance(lat, lon, 0.0);
        newLocation = new DefaultGeoNode(point);
        newLocation.setName("candidate");
        return newLocation;
    }

    /** Builds a proposal that contains all the points on our Network. */
    private NetworkProposal buildAllNodesProposal() {
        NewNodeProposal newNodeProposal = new NewNodeProposal(new DefaultGeoNode());
        DiagnosticRec diagnosticRec = new DiagnosticRec(null);
        for (GeoNode geoNode : jsonNodeStore.getNodes()) {
            diagnosticRec.addFeature(TranslateUtil.geoNodeToFeature(geoNode));
        }
        newNodeProposal.add(diagnosticRec);
        return newNodeProposal;
    }

    /**
     * Given a Node's Point ID, find all segments which are connected to that Node.
     * @param pointId - Integer representing the Node of interest.
     * @return NetworkProposal containing a list of connected Segments.
     */
    private NetworkProposal buildAdjustedEdgesProposal(Integer pointId) {
        NodeEditProposal nodeEditProposal = new NodeEditProposal();
        DiagnosticRec diagnosticRec = new DiagnosticRec(null);
        Network networkService = DefaultNetwork.getInstance();
        List<LineFeature> edgesPerNode = networkService.getLineFeaturesForNodeId(pointId);
        for (LineFeature lineFeature : edgesPerNode) {
            diagnosticRec.addFeature(lineFeature.getFeature());
        }
        nodeEditProposal.add(diagnosticRec);
        return nodeEditProposal;
    }

    /**
     * Given a Node's Point ID and a new location for that point, adjust the edges
     * which share that Node so the point matches the new location, and return that
     * set of adjusted edges.
     * @param pointId - Integer representing the Node being moved.
     * @param lat - New Latitude.
     * @param lng - New Longitude.
     * @return NetworkProposal containing list of adjusted edges.
     */
    private NetworkProposal buildAdjustedEdgesProposal(Integer pointId, Double lat, Double lng) {
        NodeEditProposal nodeEditProposal = new NodeEditProposal();
        Point adjustedEndPoint = PointFactory.getJtsInstance(lat, lng, 0.0);
        GeoNode adjustedNode = new DefaultGeoNode(adjustedEndPoint);
        DiagnosticRec diagnosticRec = new DiagnosticRec(adjustedNode);
        Network networkService = DefaultNetwork.getInstance();
        List<LineFeature> edgesPerNode = networkService.getLineFeaturesForNodeId(pointId);
        for (LineFeature lineFeature : edgesPerNode) {
            SimpleFeature adjustedFeature = adjustFeature(lineFeature, adjustedEndPoint);
            diagnosticRec.addFeature(adjustedFeature);
        }
        nodeEditProposal.add(diagnosticRec);
        NetworkProposalStore.add(nodeEditProposal);
        return nodeEditProposal;
    }

    /**
     * Accepts a SimpleFeature representing an existing Edge whose endpoint -- which one is
     * figured out here -- is being moved to the new lat/lng.
     * @param feature - Generally an Edge; should be renamed.
     * @return New SimpleFeature with the location adjusted; ID is unchanged.
     */
    private SimpleFeature adjustFeature(LineFeature feature, Point adjustedEndPoint) {
        Coordinate[] coordinates = feature.getLineString().getCoordinates();
        Double lat = adjustedEndPoint.getY();
        Double lng = adjustedEndPoint.getX();
        Double distanceToStart = adjustedEndPoint.distance(feature.getGeoStart().getPoint());
        Double distanceToEnd = adjustedEndPoint.distance(feature.getGeoEnd().getPoint());
        if (distanceToStart < distanceToEnd) {
            coordinates[0].setOrdinate(0,lng);
            coordinates[0].setOrdinate(1,lat);
        } else {
            int index = coordinates.length - 1;
            coordinates[index].setOrdinate(0, lng);
            coordinates[index].setOrdinate(1,lat);
        }
        LineString adjustedLineString = new GeometryFactory().createLineString(coordinates);
        return  (new EdgeImpl((Edge)feature, adjustedLineString)).getFeature();
    }

    private static class ProposalSummary {
        private NodeNetworkState type;
        private List<RecKey> recs = new ArrayList<>();
        private Integer defaultRecId = -1;

        public NodeNetworkState getType() {
            return type;
        }

        @SuppressWarnings("unused")     // Is used by Jackson
        public List<RecKey> getRecs() {
            return recs;
        }

        @SuppressWarnings("unused")     // Is used by Jackson
        public Integer getDefaultRecId() {
            return defaultRecId;
        }

        ProposalSummary(NewNodeProposal networkProposal) {
            type = networkProposal.getNodeNetworkState();
            for (NetworkRecommendation rec : networkProposal.getRecommendations()) {
                if (defaultRecId == -1) {
                    defaultRecId = rec.getId();
                }
                DefaultRecommendationService.PointKey pointKey = new DefaultRecommendationService.PointKey(rec.getNodeList());
                RecKey recKey = new RecKey();
                recKey.id = rec.getId();
                recKey.name = pointKey.getKey();
                recs.add(recKey);
            }
        }

        public String toJson() {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                return ow.writeValueAsString(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        private class RecKey {
            Integer id;
            String name;

            public Integer getId() {
                return id;
            }

            public String getName() {
                return name;
            }
        }
    }
}
