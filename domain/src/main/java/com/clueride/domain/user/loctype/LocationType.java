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
 * Created by jett on 10/4/17.
 */
package com.clueride.domain.user.loctype;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Domain object that can be persisted, but generally is read-only.
 */
@Immutable
public class LocationType {
    private Integer id;
    private String name;
    private String description;
    private String icon;

    private LocationType(Builder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.description = builder.getDescription();
        this.icon = builder.getIcon();
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

    public String getIcon() {
        return icon;
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

    @Entity(name="location_type")
    public static final class Builder {
        @Id
        @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="location_type_pk_sequence")
        @SequenceGenerator(name="location_type_pk_sequence", sequenceName="location_type_id_seq", allocationSize=1)
        private Integer id;

        private String name;
        private String icon;
        private String description;

        /**
         * Generate instance from Builder's values.
         * @return instance of LocationType based on Builder's values.
         */
        public LocationType build() {
            return new LocationType(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        /**
         * Creates Builder from a given instance of LocationType.
         * @param locationType the instance whose data to use.
         * @return Mutable instance of the Builder for the Location Type.
         */
        public static Builder from(LocationType locationType) {
            return builder()
                    .withId(locationType.getId())
                    .withName(locationType.getName())
                    .withDescription(locationType.getDescription())
                    .withIcon(locationType.getIcon())
                    ;
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

        public String getIcon() {
            return icon;
        }

        public Builder withIcon(String icon) {
            this.icon = icon;
            return this;
        }

    }
}
