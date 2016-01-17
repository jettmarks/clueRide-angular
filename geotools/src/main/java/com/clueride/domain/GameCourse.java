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
 * Created by jett on 1/15/16.
 */
package com.clueride.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import com.clueride.domain.user.Location;
import com.clueride.domain.user.Path;
import com.clueride.service.IdProvider;
import com.clueride.service.MemoryBasedCourseIdProvider;

/**
 * Implementation of Course used for Games.
 */
public class GameCourse implements Course {
    private final Integer id;
//    private Location departure;
//    private Location destination;
    private List<Step> steps;
    private List<Integer> pathIds;
    private String description;
    private String name;

    private int stepIndex;
    private int lastStep;

    private GameCourse(Builder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.description = builder.getDescription();

        stepIndex = 0;
        this.steps = ImmutableList.copyOf(builder.getSteps());
        this.pathIds = ImmutableList.copyOf(builder.getPathIds());
//        this.departure = (Location) steps.get(0);
//        this.lastStep = steps.size() - 1;
//        this.destination = (Location) steps.get(lastStep);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<Integer> getPathIds() {
        return pathIds;
    }

    @Override
    public Step nextStep() {
        if (stepIndex == lastStep) {
            throw new IllegalStateException("Course is complete; no more steps");
        }
        return steps.get(stepIndex + 1);
    }

    @Override
    public Step currentStep() {
        return steps.get(stepIndex);
    }

    @Override
    public Step completeCurrentStep() {
        return steps.get(stepIndex++);
    }

    public static class Builder {
        private Integer id;
        private String name;
        private String description;

        private List<Step> steps;
        private IdProvider idProvider;
        private List<Integer> pathIds;

        private Builder() {
            idProvider = new MemoryBasedCourseIdProvider();
            id = idProvider.getId();
            steps = new ArrayList<>();
            pathIds = new ArrayList<>();
        }
        public static Builder getBuilder() {
            return new Builder();
        }
        public Integer getId() {
            return id;
        }
        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public List<Step> getSteps() {
            return steps;
        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder addPathId(int id) {
            pathIds.add(id);
            return this;
        }

        public Builder addLocation(Location location) {
            steps.add(location);
            return this;
        }

        public Builder addPath(Path path) {
            steps.add(path);
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public GameCourse build() {
            // Validations happen here
            return new GameCourse(this);
        }

        public List<Integer> getPathIds() {
            return pathIds;
        }
    }
}
