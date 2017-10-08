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
 * Created by jett on 10/8/17.
 */
package com.clueride.domain.user.image;

import java.net.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Details for recording the location of an Image to be used any place where it can be
 * referenced by a URL.
 *
 * The Database stores just the String representation of the URL. The URL representation would added
 * during the build of the instance.
 */
@Immutable
public class Image {
    private Integer id;
    private URL url;

    private Image(Builder builder) {
        this.id = builder.getId();
        this.url = builder.getUrl();
    }

    public Integer getId() {
        return id;
    }

    public URL getUrl() {
        return url;
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

    @Entity(name="image")
    public static final class Builder implements com.clueride.domain.common.Builder<Image> {
        @Id
        @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="image_pk_sequence")
        @SequenceGenerator(name="image_pk_sequence",sequenceName="image_id_seq", allocationSize=1)
        private Integer id;

        @Column(name="url")
        private String urlString;

        @Transient
        private URL url;

        // TODO: CA-318 - Add builder() to the interface (as well as .from())
        public static Builder builder() {
            return new Builder();
        }

        public static Builder from(Image image) {
            return new Builder()
                    .withId(image.getId())
                    .withUrl(image.getUrl())
                    .withUrlString(image.getUrl().toString());
        }

        @Override
        public Image build() {
            return new Image(this);
        }

        public Integer getId() {
            return id;
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public String getUrlString() {
            return urlString;
        }

        public Builder withUrlString(String urlString) {
            this.urlString = urlString;
            return this;
        }

        public URL getUrl() {
            return url;
        }

        public Builder withUrl(URL url) {
            this.url = url;
            return this;
        }
    }

}
