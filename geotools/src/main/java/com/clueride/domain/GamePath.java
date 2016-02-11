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
 * Created by jett on 1/16/16.
 */
package com.clueride.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import com.clueride.dao.JsonNetworkStore;
import com.clueride.dao.JsonNodeStore;
import com.clueride.domain.dev.Segment;
import com.clueride.domain.user.Path;
import com.clueride.feature.Edge;
import com.clueride.service.IdProvider;
import com.clueride.service.MemoryBasedPathIdProvider;
import com.clueride.service.NetworkEval;
import com.clueride.service.NetworkEvalImpl;
import static java.util.Objects.requireNonNull;

/**
 * Representation of a Path between two Locations and made up of an ordered set of Edges/Segments.
 *
 * At an abstract level, the Path is an association between two Node IDs.  Those two Node IDs
 * are most useful when they are associated with Locations, but a lot of use can be
 * obtained from simply defining a "Path" between two Nodes.
 *
 * At a level just a bit more concrete, the Path is an ordered sequence of Edges/Segments.
 */
public class GamePath implements Path {
    private Integer id;
    private List<Edge> edges;
    private List<Integer> edgeIds;
    private Integer startNodeId;
    private Integer endNodeId;
    private Integer startLocationId;
    private Integer endLocationId;

    /**
     * Used by Builder to construct our instances.
     * @param builder - Builder holding the data and knowledge of how to validate that data.
     */
    private GamePath(Builder builder) {
        this.id = builder.getId();
        this.startNodeId = builder.getStartNodeId();
        this.endNodeId = builder.getEndNodeId();
        this.edgeIds = ImmutableList.copyOf(builder.getEdgeIds());
        this.startLocationId = builder.getStartLocationId();
        this.endLocationId = builder.getEndLocationId();
    }

    @Override
    public SortedSet<Segment> getSegments() {
        return null;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Integer getStartNodeId() {
        return startNodeId;
    }

    @Override
    public Integer getEndNodeId() {
        return endNodeId;
    }

    @Override
    public List<Integer> getEdgeIds() {
        return edgeIds;
    }

    @Override
    public Integer getStartLocationId() {
        return startLocationId;
    }

    @Override
    public Integer getEndLocationId() {
        return endLocationId;
    }

    public static class Builder implements Path {
        private Integer id;
        private List<Edge> edges;
        private List<Integer> edgeIds;
        private IdProvider idProvider;
        private Integer startNodeId;
        private Integer endNodeId;
        private NetworkEval networkEval;
        private Integer startLocationId;
        private Integer endLocationId;

        public Builder() {
            this(new NetworkEvalImpl(
                    JsonNodeStore.getInstance(),
                    JsonNetworkStore.getInstance()
            ));
        }

        private Builder(@Nonnull NetworkEval networkEval) {
            this.networkEval = requireNonNull(networkEval);
            idProvider = new MemoryBasedPathIdProvider();
            id = idProvider.getId();
            edges = new ArrayList<>();
            edgeIds = new ArrayList<>();
        }

        public Builder withStartNodeId(Integer startNodeId) {
            this.startNodeId = startNodeId;
            return this;
        }

        public Builder withEndNodeId(Integer endNodeId) {
            this.endNodeId = endNodeId;
            return this;
        }

        public Builder addEdgeId(Integer edgeId) {
            this.edgeIds.add(edgeId);
            return this;
        }

        public Builder addEdge(Edge edge) {
            this.edges.add(edge);
            return this;
        }

        public GamePath build() {
            // TODO: Validations
            validateEdgeSequence();
            return new GamePath(this);
        }

        private void validateEdgeSequence() {
            networkEval.checkPathEdgesFromStartToEnd(this);
        }

        @Override
        public SortedSet<Segment> getSegments() {
            return null;
        }

        @Override
        public Integer getId() {
            return id;
        }

        public List<Edge> getEdges() {
            return edges;
        }

        @Override
        public Integer getStartNodeId() {
            return startNodeId;
        }

        @Override
        public Integer getEndNodeId() {
            return endNodeId;
        }

        @Override
        public List<Integer> getEdgeIds() {
            return edgeIds;
        }

        @Override
        public Integer getStartLocationId() {
            return startLocationId;
        }

        @Override
        public Integer getEndLocationId() {
            return endLocationId;
        }

        public static Builder getBuilder(NetworkEval networkEval) {
            return new Builder(networkEval);
        }

        public Builder setEdgeIds(List<Integer> edgeIds) {
            this.edgeIds = edgeIds;
            return this;
        }

        public Builder withStartLocationId(Integer startLocationId) {
            this.startLocationId = startLocationId;
            return this;
        }

        public Builder withEndLocationId(Integer endLocationId) {
            this.endLocationId = endLocationId;
            return this;
        }
    }
}
