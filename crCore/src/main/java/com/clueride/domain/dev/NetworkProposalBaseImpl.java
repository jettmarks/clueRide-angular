/*
 * Copyright 2016 Jett Marks
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
 * Created by jett on 1/14/16.
 */
package com.clueride.domain.dev;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Common Network Proposal implementations.
 *
 * Must be overridden to provide toJson implementation.
 */
public abstract class NetworkProposalBaseImpl implements NetworkProposal {
    private static final Logger LOGGER = Logger.getLogger(NetworkProposalBaseImpl.class);
    private static final Object LOCK_OBJECT = new Object();

    protected static Integer lastId = 1;
    protected Integer id;
    protected List<NetworkRecommendation> networkRecommendations = new ArrayList<>();

    protected NetworkProposalBaseImpl() {
        synchronized (LOCK_OBJECT) {
            this.id = lastId++;
        }
    }

    /**
     * @see NetworkProposal#getId()
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * @see NetworkProposal#hasMultipleRecommendations()
     */
    @Override
    public boolean hasMultipleRecommendations() {
        return networkRecommendations.size() > 1;
    }

    /**
     * @see NetworkProposal#getRecommendations()
     */
    @Override
    public List<NetworkRecommendation> getRecommendations() {
        return networkRecommendations;
    }

    @Override
    public NetworkRecommendation getRecommendation(Integer recId) {
        for (NetworkRecommendation rec : networkRecommendations) {
            if (recId.equals(rec.getId())) {
                return rec;
            }
        }
        return null;
    }

    @Override
    public abstract String toJson();

    @Override
    public NodeNetworkState getNodeNetworkState() {
        return null;
    }

    /**
     * Remove all recommendations in preparation to collapse and sort
     */
    @Override
    public void resetRecommendationList() {
        networkRecommendations.clear();
    }

    /**
     * @param networkRecommendation - Recommendation to be added to the Proposal.
     */
    public void add(NetworkRecommendation networkRecommendation) {
        LOGGER.info(networkRecommendation);
        networkRecommendations.add(networkRecommendation);
    }

    @Override
    public String toString() {
        return "NetworkProposalBaseImpl{" +
                "id=" + id +
                ", networkRecommendations=" + networkRecommendations +
                '}';
    }
}
