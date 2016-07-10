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
 * Created by jett on 7/10/16.
 */
package com.clueride.domain;

import com.clueride.domain.account.Member;

/**
 * Represents a flattened Invitation where all references to sub-types are resolved into the actual instances.
 *
 * This is suitable for returning a fully-populated invitation given just the token for the invitation. It is
 * a read-only object assembled from multiple services/dataStores.  The Outing contains references, but the
 * actual instances pointed to by those references are brought back up to the same level as the other
 * fields of this object.
 *
 * This instance will not be populated directly from the JSON and will instead be assembled by
 * the Invitation Service.
 */
public class InvitationFull {
    private final String token;
    private final Integer id; // Not clear how this would be used
    private final Outing outing;
    private final Course course;
    private final CourseType courseType;
    private final Team team;
    private final Member member;
    // TODO: Perhaps the Schedule too?

    public String getToken() {
        return token;
    }

    public Integer getId() {
        return id;
    }

    public Outing getOuting() {
        return outing;
    }

    public Course getCourse() {
        return course;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public Team getTeam() {
        return team;
    }

    public Member getMember() {
        return member;
    }

    public InvitationFull(Builder builder) {
        token = builder.getToken();
        id = builder.getId();
        outing = builder.getOuting();
        course = builder.getCourse();
        courseType = builder.getCourseType();
        team = builder.getTeam();
        member = builder.getMember();
    }

    public static final class Builder {
        private String token;
        private Integer id; // Not clear how this would be used
        private Outing outing;
        private Course course;
        private CourseType courseType;
        private Team team;
        private Member member;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public InvitationFull build() {
            return new InvitationFull(this);
        }

        public String getToken() {
            return token;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Integer getId() {
            return id;
        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Outing getOuting() {
            return outing;
        }

        public Builder setOuting(Outing outing) {
            this.outing = outing;
            return this;
        }

        public Course getCourse() {
            return course;
        }

        public Builder setCourse(Course course) {
            this.course = course;
            return this;
        }

        public CourseType getCourseType() {
            return courseType;
        }

        public Builder setCourseType(CourseType courseType) {
            this.courseType = courseType;
            return this;
        }

        public Team getTeam() {
            return team;
        }

        public Builder setTeam(Team team) {
            this.team = team;
            return this;
        }

        public Member getMember() {
            return member;
        }

        public Builder setMember(Member member) {
            this.member = member;
            return this;
        }

    }

}
