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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * DTO for the data captured for Badge-worthy deeds.
 */
public class BadgeEvent {
    private Date timestamp;
    private Principal principal;
    private String methodName;
    private Class methodClass;
    private Object returnValue;

    private BadgeEvent(Builder builder) {
        this.timestamp = builder.timestamp;
        this.principal = builder.principal;
        this.methodClass = builder.methodClass;
        this.methodName = builder.methodName;
        this.returnValue = builder.returnValue;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Principal getPrincipal() {
        return principal;
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
    public static class Builder {
        private Date timestamp;
        private Principal principal;
        private String methodName;
        private Class methodClass;
        private Object returnValue;

        public BadgeEvent build() {
            return new BadgeEvent(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static Builder from(BadgeEvent badgeEvent) {
            return builder()
                    .withPrincipal(badgeEvent.principal)
                    .withTimestamp(badgeEvent.timestamp)
                    .withMethodClass(badgeEvent.methodClass)
                    .withMethodName(badgeEvent.methodName)
                    .withReturnValue(badgeEvent.returnValue);
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public Builder withTimestamp(Date timestamp) {
            this.timestamp = timestamp;
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

        public Class getMethodClass() {
            return methodClass;
        }

        public Builder withMethodClass(Class methodClass) {
            this.methodClass = methodClass;
            return this;
        }

        public Object getReturnValue() {
            return returnValue;
        }

        public Builder withReturnValue(Object returnValue) {
            this.returnValue = returnValue;
            return this;
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
