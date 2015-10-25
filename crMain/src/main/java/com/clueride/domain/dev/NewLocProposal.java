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

import com.clueride.domain.GeoNode;
import com.clueride.geo.TranslateUtil;
import com.clueride.io.JsonStoreType;
import com.clueride.io.JsonUtil;

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

    private final GeoNode newLoc;

    /**
     * @param newLoc
     */
    public NewLocProposal(GeoNode newLoc) {
        if (newLoc == null) {
            throw new IllegalArgumentException(
                    "Cannot provide null/empty Location");
        }

        this.newLoc = newLoc;
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
        JsonUtil jsonRespWriter = new JsonUtil(JsonStoreType.LOCATION);
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
        featureCollection.add(TranslateUtil.geoNodeToFeature(newLoc));
        return jsonRespWriter.toString(featureCollection);
    }

    /**
     * @see com.clueride.domain.dev.NetworkProposal#getNodeNetworkState()
     */
    @Override
    public NodeNetworkState getNodeNetworkState() {
        return nodeNetworkState;
    }

    /**
     * @param nodeNetworkState
     *            the nodeNetworkState to set
     */
    public void setNodeNetworkState(NodeNetworkState nodeNetworkState) {
        this.nodeNetworkState = nodeNetworkState;
        this.newLoc.setState(nodeNetworkState);
    }

    /**
     * @param networkRecommendation
     */
    public void add(NetworkRecommendation networkRecommendation) {
        LOGGER.info(networkRecommendation);
        networkRecommendations.add(networkRecommendation);
    }

    /**
     * If we've got a list, we can add them all at once.
     * 
     * @param recList
     *            - List of recommendations appropriate to the proposal.
     */
    public void addAll(List<NetworkRecommendation> recList) {
        networkRecommendations.addAll(recList);
    }

}
