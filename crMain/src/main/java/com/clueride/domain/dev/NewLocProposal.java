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

    /**
     */
    public NewLocProposal() {
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
        return null;
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
    }

    /**
     * @param networkRecommendation
     */
    public void add(NetworkRecommendation networkRecommendation) {
        LOGGER.info(networkRecommendation);
        networkRecommendations.add(networkRecommendation);
    }

}