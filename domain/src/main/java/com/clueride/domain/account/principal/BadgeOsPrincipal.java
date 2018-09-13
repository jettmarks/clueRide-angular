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
 * Created by jett on 8/28/18.
 */
package com.clueride.domain.account.principal;

import java.security.Principal;

import javax.annotation.concurrent.Immutable;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static java.util.Objects.requireNonNull;

/**
 * Identifies a given user in the BadgeOS system including email address, Name and user ID within WordPress.
 */
@Immutable
public class BadgeOsPrincipal implements Principal {
    private Integer badgeOsUserId;
    private String name;
    private InternetAddress emailAddress;

    private BadgeOsPrincipal(Builder builder) {
        this.name = requireNonNull(builder.getName());
        this.badgeOsUserId = requireNonNull(builder.getBadgeOsUserId());
        this.emailAddress = requireNonNull(builder.getEmailAddress());
    }

    public Integer getBadgeOsUserId() {
        return badgeOsUserId;
    }

    @Override
    public String getName() {
        return name;
    }

    public InternetAddress getEmailAddress() {
        return emailAddress;
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

    @Entity(name="badgeos_principal")
    @Table(name="wp_users")
    public static class Builder implements com.clueride.domain.common.Builder {
        /**
         * Builder for BadgeOsPrincipal.
         */
        @Id
        private Integer id;

        @Column(name="display_name")
        private String name;

        @Transient
        private InternetAddress emailAddress;

        @Column(name="user_email")
        private String emailAddressString;

        @Override
        public BadgeOsPrincipal build() {
            return new BadgeOsPrincipal(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public String getName() {
            return name;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public InternetAddress getEmailAddress() {
            emailStringToInternetAddress(emailAddressString);
            return this.emailAddress;
        }

        public Builder withEmailAddress(InternetAddress emailAddress) {
            this.emailAddressString = emailAddress.toString();
            this.emailAddress = emailAddress;
            return this;
        }

        public Builder withEmailAddressString(String emailAddressString) {
            this.emailAddressString = emailAddressString;
            emailStringToInternetAddress(emailAddressString);
            return this;
        }

        private void emailStringToInternetAddress(String emailAddressString) {
            try {
                this.emailAddress = new InternetAddress(emailAddressString);
            } catch (AddressException e) {
                this.emailAddress = null;
                e.printStackTrace();
            }
        }

        public Integer getBadgeOsUserId() {
            return id;
        }

        public Builder withId(Integer badgeOsUserId) {
            return withBadgeOsUserId(badgeOsUserId);
        }

        public Builder withBadgeOsUserId(Integer badgeOsUserId) {
            this.id = badgeOsUserId;
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
