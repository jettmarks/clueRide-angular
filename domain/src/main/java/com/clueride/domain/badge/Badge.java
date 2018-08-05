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
 * Created by jett on 8/4/18.
 */
package com.clueride.domain.badge;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import static java.util.Objects.requireNonNull;

/**
 * Pojo for Badges.
 *
 * Each instance of awarding a badge type is a Badge, so the IDs are
 * unique instances of an awarded badge.
 */
@Immutable
public class Badge {
    private final Integer id;
    private final BadgeType badgeType;
    private final URL badgeImageUrl;
    private final URL badgeCriteriaUrl;

    public Badge(Builder builder) {
        this.id = requireNonNull(builder.getId());
        this.badgeType = requireNonNull(builder.getBadgeType());
        this.badgeImageUrl = requireNonNull(builder.getImageUrl());
        this.badgeCriteriaUrl = requireNonNull(builder.getCriteriaUrl());
    }

    public Integer getId() {
        return id;
    }

    public BadgeType getBadgeType() {
        return badgeType;
    }

    public URL getBadgeImageUrl() {
        return badgeImageUrl;
    }

    public URL getBadgeCriteriaUrl() {
        return badgeCriteriaUrl;
    }

    @Entity(name="badge")
    public static class Builder implements com.clueride.domain.common.Builder<Badge> {

        @Id
        private Integer id;

        @Column(name="badge_type") private String badgeTypeString;
        @Column(name="image_url") private String imageUrlString;
        @Column(name="criteria_url") private String criteriaUrlString;

        @Transient
        private BadgeType badgeType;

        @Transient
        private URL imageUrl;

        @Transient
        private URL criteriaUrl;

        public static Builder builder() {
            return new Builder();
        }

        public BadgeType getBadgeType() {
            return badgeType;
        }

        public Builder withBadgeType(BadgeType badgeType) {
            this.badgeType = badgeType;
            this.badgeTypeString = badgeType.toString();
            return this;
        }

        public URL getImageUrl() {
            return imageUrl;
        }

        public Builder withImageUrl(URL imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }
        public URL getCriteriaUrl() {
            return criteriaUrl;
        }

        public Builder withCriteriaUrl(URL criteriaUrl) {
            this.criteriaUrl = criteriaUrl;
            return this;
        }

        @Override
        public Badge build() {
            return new Badge(this);
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public String getBadgeTypeString() {
            return badgeTypeString;
        }

        public Builder withBadgeTypeString(String badgeTypeString) {
            this.badgeTypeString = badgeTypeString;
            this.badgeType = BadgeType.valueOf(badgeTypeString);
            return this;
        }

        public String getImageUrlString() {
            return imageUrlString;
        }

        public Builder withImageUrlString(String imageUrlString) throws MalformedURLException {
            this.imageUrlString = imageUrlString;
            this.imageUrl = new URL(imageUrlString);
            return this;
        }

        public String getCriteriaUrlString() {
            return criteriaUrlString;
        }

        public Builder withCriteriaUrlString(String criteriaUrlString) throws MalformedURLException {
            this.criteriaUrlString = criteriaUrlString;
            this.criteriaUrl = new URL(criteriaUrlString);
            return this;
        }
    }

}
