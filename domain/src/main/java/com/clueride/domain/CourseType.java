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

import java.net.URL;

/**
 * Model for the Type of Course: Enumeration of Type is linked to a specific Course and
 * holds details of that Course Type:<ul>
 *     <li>ID</li>
 *     <li>Type (name)</li>
 *     <li>Description</li>
 *     <li>URL of web page holding further details.</li>
 * </ul>
 */
public class CourseType {
    private final Integer id;
    private final String type;
    private final String description;
    private final URL url;

    public CourseType(Builder builder) {
        id = builder.getId();
        type = builder.getType();
        description = builder.getDescription();
        url = builder.getUrl();
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public URL getUrl() {
        return url;
    }

    /**
     * Builder pattern implements interface to allow construction of a given type.
     */
    public static final class Builder implements com.clueride.domain.common.Builder<CourseType> {
        private Integer id;
        private String type;
        private String description;
        private URL url;

        @Override
        public CourseType build() {
            return new CourseType(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static Builder from(CourseType instance) {
            return builder()
                    .setId(instance.id)
                    .setDescription(instance.description)
                    .setType(instance.type)
                    .setUrl(instance.url)
                    ;
        }

        public Integer getId() {
            return id;
        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public String getType() {
            return type;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public URL getUrl() {
            return url;
        }

        public Builder setUrl(URL url) {
            this.url = url;
            return this;
        }
    }
}
