/*
 * Copyright 2018 Jett Marks
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
 * Created by jett on 9/15/18.
 */
package com.clueride.domain.course;

import java.util.List;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.clueride.domain.Step;
import com.clueride.domain.game.GameState;
import com.clueride.domain.user.location.Location;

/**
 * Contains static information about a Course; persistable to DB and unchanging
 * over the time frame of an Outing.
 *
 * This serves as a reference to obtain information about the structure of the
 * course. The dynamic state information is held in the {@link GameState} instance.
 */
@Immutable
public class GameCourse implements Course {
    private final Integer id;
    private final String name;
    private final String description;
    private final Integer courseTypeId;
    private final List<Step> steps;
    private final List<Integer> pathIds;
    private final Location departure;
    private final Location destination;

    private GameCourse(Builder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.description = builder.getDescription();
        this.courseTypeId = builder.getCourseTypeId();
        this.steps = builder.getSteps();
        this.pathIds = builder.getPathIds();
        this.departure = builder.getDeparture();
        this.destination = builder.getDestination();
    }

    @Override
    public Integer getId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Integer getCourseTypeId() {
        return null;
    }

    @Override
    public List<Step> getSteps() {
        return null;
    }

    @Override
    public List<Integer> getPathIds() {
        return null;
    }

    @Override
    public Location getDeparture() {
        return null;
    }

    @Override
    public Location getDestination() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public static class Builder {
        private Integer id;
        private String name;
        private String description;
        private Integer courseTypeId;
        private List<Step> steps;
        private List<Integer> pathIds;
        private Location departure;
        private Location destination;

        public static Builder builder() {
            return new Builder();
        }

        public static Builder from(Course course) {
            return builder()
                    .withId(course.getId())
                    .withName(course.getName())
                    .withDescription(course.getDescription())
                    .withCourseTypeId(course.getCourseTypeId())
                    .withSteps(course.getSteps())
                    ;
        }

        public GameCourse build() {
            return new GameCourse(this);
        }

        public Integer getId() {
            return id;
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Integer getCourseTypeId() {
            return courseTypeId;
        }

        public Builder withCourseTypeId(Integer courseTypeId) {
            this.courseTypeId = courseTypeId;
            return this;
        }

        public List<Step> getSteps() {
            return steps;
        }

        public Builder withSteps(List<Step> steps) {
            this.steps = steps;
            return this;
        }

        public List<Integer> getPathIds() {
            return pathIds;
        }

        public Builder withPathIds(List<Integer> pathIds) {
            this.pathIds = pathIds;
            return this;
        }

        public Location getDeparture() {
            return departure;
        }

        public Builder withDeparture(Location departure) {
            this.departure = departure;
            return this;
        }

        public Location getDestination() {
            return destination;
        }

        public Builder withDestination(Location destination) {
            this.destination = destination;
            return this;
        }
    }

}
