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
 * Created by jett on 3/5/16.
 */
package com.clueride.domain;

import java.util.Date;

import com.clueride.service.IdProvider;
import com.clueride.service.MemoryBasedOutingIdProvider;

/**
 * Model for the Outing: a particular {@link Team} running a particular {@link Course}
 * at a scheduled time.
 */
public class Outing {
    private final Integer id;
    private Integer teamId;
    private Integer courseId;
    private Date scheduledTime;
    private Integer guideMemberId;

    public Outing(Builder builder) {
        this.id = builder.id;
        this.teamId = builder.getTeamId();
        this.courseId = builder.getCourseId();
        this.scheduledTime = builder.getScheduledTime();
        this.guideMemberId = builder.getGuideMemberId();
    }

    public Integer getId() {
        return id;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Integer getGuideMemberId() {
        return guideMemberId;
    }

    public static final class Builder implements com.clueride.domain.common.Builder<Outing> {
        private Integer id;
        private Integer teamId;
        private Integer courseId;
        private Date scheduledTime;
        private Integer guideMemberId;

        /**
         * Builder pattern implements interface to allow construction of a given type.
         */
        /* For other users of an Outing. */
        public static Builder builder() {
            return new Builder();
        }

        public static Builder from(Outing outing) {
            return builder()
                    .withId(outing.getId())
                    .withTeamId(outing.getTeamId())
                    .withCourseId(outing.getCourseId());
        }

        public Outing build() {
            if (scheduledTime == null) {
                scheduledTime = new Date();
            }
            return new Outing(this);
        }

        private static IdProvider idProvider = new MemoryBasedOutingIdProvider();

        public Integer getCourseId() {
            return courseId;
        }

        public Builder withCourseId(Integer courseId) {
            this.courseId = courseId;
            return this;
        }

        public Integer getTeamId() {
            return teamId;
        }

        public Builder withTeamId(Integer teamId) {
            this.teamId = teamId;
            return this;
        }

        /* ID is interesting because it can pick-up from a new instance (assigned), or have been persisted for existing
         * instances. */
        public Integer getId() {
            return id;
        }

        /* Also for Jackson when accepting a fully-populated Outing (ID already assigned). */
        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        /* For Jackson when creating a new Outing. */
        public Builder() {
            id = idProvider.getId();
        }

        public Date getScheduledTime() {
            return scheduledTime;
        }

        public Builder withScheduledTime(Date scheduledTime) {
            this.scheduledTime = scheduledTime;
            return this;
        }

        public Integer getGuideMemberId() {
            return guideMemberId;
        }

        public Builder withGuideMemberId(Integer guideMemberId) {
            this.guideMemberId = guideMemberId;
            return this;
        }

    }
}
