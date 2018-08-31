/*
 * Copyright 2017 Jett Marks
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
 * Created by jett on 11/25/17.
 */
package com.clueride.domain.badge.event;

import java.security.Principal;
import java.util.Date;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.clueride.infrastructure.db.ClueRide;

/**
 * DTO for the data captured for Badge-worthy deeds.
 */
@Immutable
public class BadgeEvent {
    private Integer id;
    private Date timestamp;
    private Principal principal;
    private Integer memberId;
    private String methodName;
    private Class methodClass;
    private Object returnValue;

    private BadgeEvent(Builder builder) {
        this.id = builder.id;
        this.timestamp = builder.timestamp;
        this.principal = builder.principal;
        this.memberId = builder.memberId;
        this.methodClass = builder.methodClass;
        this.methodName = builder.methodName;
        this.returnValue = builder.returnValue;
    }

    public Integer getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class getMethodClass() {
        return methodClass;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Mutable instance of BadgeEvent.
     */
    @ClueRide
    @Entity(name="badge_event")
    public static class Builder {
        @Id
        @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "badge_event_pk_sequence")
        @SequenceGenerator(name="badge_event_pk_sequence", sequenceName = "badge_event_id_seq", allocationSize = 1)
        private Integer id;

        @Column
        private Date timestamp;

        @Transient
        private Principal principal;

        @Column(name="member_id")
        private Integer memberId;

        @Column(name="method_name")
        private String methodName;

        @Column(name="class_name")
        private String className;

        @Transient
        private Class methodClass;

        @Column(name="return_value")
        private String returnValueAsString;

        @Transient
        private Object returnValue;

        public BadgeEvent build() {
            return new BadgeEvent(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static Builder from(BadgeEvent badgeEvent) {
            return builder()
                    .withId(badgeEvent.id)
                    .withPrincipal(badgeEvent.principal)
                    .withMemberId(badgeEvent.memberId)
                    .withTimestamp(badgeEvent.timestamp)
                    .withMethodClass(badgeEvent.methodClass)
                    .withClassName(badgeEvent.methodClass.getCanonicalName())
                    .withMethodName(badgeEvent.methodName)
                    .withReturnValue(badgeEvent.returnValue)
                    .withReturnValueAsString(badgeEvent.returnValue.toString())
                    ;
        }

        public Integer getId() {
            return id;
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public Builder withTimestamp(Date timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Integer getMemberId() {
            return memberId;
        }

        public Builder withMemberId(Integer memberId) {
            this.memberId = memberId;
            return this;
        }

        public Principal getPrincipal() {
            return principal;
        }

        public Builder withPrincipal(Principal principal) {
            this.principal = principal;
            return this;
        }

        public String getMethodName() {
            return methodName;
        }

        public Builder withMethodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public Builder withClassName(String className) {
            this.className = className;
            return this;
        }

        public String getClassName() {
            return className;
        }

        public Class getMethodClass() {
            return methodClass;
        }

        public Builder withMethodClass(Class methodClass) {
            this.methodClass = methodClass;
            this.className = methodClass.getCanonicalName();
            return this;
        }

        public Object getReturnValue() {
            return returnValue;
        }

        public Builder withReturnValue(Object returnValue) {
            this.returnValue = returnValue;
            this.returnValueAsString = returnValue.toString();
            return this;
        }

        private Builder withReturnValueAsString(String returnValueAsString) {
            this.returnValueAsString = returnValueAsString;
            return this;
        }

        public String getReturnValueAsString() {
            return returnValueAsString;
        }

        @Override
        public boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj);
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

    }

}
