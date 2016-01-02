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
 * Created Oct 15, 2015
 */
package com.clueride.domain.dev;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.geotools.feature.DefaultFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.rec.NetworkRecImpl;
import com.clueride.geo.TranslateUtil;
import com.clueride.io.GeoJsonUtil;
import com.clueride.io.JsonStoreType;

/**
 * Implementation of NetworkProposal that involves adding a new Location to the
 * network.
 *
 * @author jett
 *
 */
public class NewLocProposal implements NetworkProposal {

    private static final Logger LOGGER = Logger.getLogger(NewLocProposal.class);

    private static Integer lastId = 1;
    private Integer id;
    private List<NetworkRecommendation> networkRecommendations = new ArrayList<>();
    private NodeNetworkState nodeNetworkState;

    private final GeoNode newNode;

    /**
     * @param node - GeoNode to be added to the network.
     */
    public NewLocProposal(GeoNode node) {
        if (node == null) {
            throw new IllegalArgumentException(
                    "Cannot provide null/empty Location");
        }

        this.newNode = node;
        setNodeNetworkState(NodeNetworkState.UNDEFINED);
        synchronized (lastId) {
            this.id = lastId++;
        }
    }

    /**
     * @see com.clueride.domain.dev.NetworkProposal#getId()
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * @see com.clueride.domain.dev.NetworkProposal#hasMultipleRecommendations()
     */
    @Override
    public boolean hasMultipleRecommendations() {
        return networkRecommendations.size() > 1;
    }

    /**
     * @see com.clueride.domain.dev.NetworkProposal#getRecommendations()
     */
    @Override
    public List<NetworkRecommendation> getRecommendations() {
        return networkRecommendations;
    }

    /**
     * @see com.clueride.domain.dev.NetworkProposal#toJson()
     */
    @Override
    public String toJson() {
        getNodeNetworkState();  // Triggers evaluation of the state
        GeoJsonUtil jsonRespWriter = new GeoJsonUtil(JsonStoreType.OTHER);
        DefaultFeatureCollection fcPoints = new DefaultFeatureCollection();
        DefaultFeatureCollection fcNonPoints = new DefaultFeatureCollection();
        // Feature for the New Location we're adding; Rec should not provide this
        fcPoints.add(TranslateUtil.geoNodeToFeature(newNode));
        for (NetworkRecommendation rec : networkRecommendations) {
            ((NetworkRecImpl) rec).logRecommendationSummary();
            for (SimpleFeature feature : rec.getFeatureCollection()) {
                if (feature.getFeatureType().getTypeName().contains("PointType")) {
                    fcPoints.add(feature);
                } else {
                    fcNonPoints.add(feature);
                }
            }
        }
        String pointResult = jsonRespWriter.toString(fcPoints);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(pointResult.substring(0, pointResult.length()-2));
        for (SimpleFeature feature : fcNonPoints) {
            stringBuffer.append(",").append(jsonRespWriter.toString(feature));
        }
        stringBuffer.append("]}");
        return stringBuffer.toString();
    }

    /**
     * @see com.clueride.domain.dev.NetworkProposal#getNodeNetworkState()
     */
    @Override
    public NodeNetworkState getNodeNetworkState() {
        if (hasMultipleRecommendations()) {
            setNodeNetworkState(NodeNetworkState.ON_MULTI_TRACK);
        }
        // TODO: This doesn't consider ON_NODE or ON_SEGMENT
        if (getRecommendations().size() == 1) {
            setNodeNetworkState(NodeNetworkState.ON_SINGLE_TRACK);
        }
        if (getRecommendations().size() == 0) {
            setNodeNetworkState(NodeNetworkState.OFF_NETWORK);
        }
        return nodeNetworkState;
    }

    /**
     * Clients "set" this by adding recommendations.
     * 
     * @param nodeNetworkState
     *            the nodeNetworkState to set
     */
    private void setNodeNetworkState(NodeNetworkState nodeNetworkState) {
        this.nodeNetworkState = nodeNetworkState;
        this.newNode.setState(nodeNetworkState);
    }

    /**
     * @param networkRecommendation
     */
    public void add(NetworkRecommendation networkRecommendation) {
        LOGGER.info(networkRecommendation);
        networkRecommendations.add(networkRecommendation);
    }

    public GeoNode getNode() {
        return newNode;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NewLocProposal [id=").append(id).append(
                ", numberNetworkRecs=").append(networkRecommendations.size())
                .append(", nodeNetworkState=").append(nodeNetworkState).append(
                        ", newNode=").append(newNode).append("]");
        return builder.toString();
    }
}
