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

    public Outing(Builder builder) {
        this.id = builder.id;
        this.teamId = builder.getTeamId();
        this.courseId = builder.getCourseId();
        this.scheduledTime = null;
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

//    public Date getScheduledTime() {
//        return scheduledTime;
//    }
//
//    public void setScheduledTime(Date scheduledTime) {
//        this.scheduledTime = scheduledTime;
//    }

    public static final class Builder {
        private Integer id;
        private Integer teamId;

        public Integer getCourseId() {
            return courseId;
        }

        public Builder setCourseId(Integer courseId) {
            this.courseId = courseId;
            return this;
        }

        public Integer getTeamId() {
            return teamId;
        }

        public Builder setTeamId(Integer teamId) {
            this.teamId = teamId;
            return this;
        }

        private Integer courseId;

        private static IdProvider idProvider = new MemoryBasedOutingIdProvider();

        /* For Jackson when creating a new Outing. */
        public Builder() {
            id = idProvider.getId();
        }

        /* Also for Jackson when accepting a fully-populated Outing (ID already assigned). */
        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        /* For other users of an Outing. */
        public static Builder builder() {
            return new Builder();
        }

        public Outing build() {
            return new Outing(this);
        }
    }
}
