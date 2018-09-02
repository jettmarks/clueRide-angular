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
 * Created by jett on 9/1/18.
 */
package com.clueride.domain.account.wpuser;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;

import javax.annotation.concurrent.Immutable;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.base.Strings;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static java.util.Objects.requireNonNull;

/**
 * POJO for WordPerfect Users to/from the BadgeOS system.
 *
 * There is a BadgeOSPrincipal which also hits the same table, but that is a limited
 * set of data -- which can be built from the validated data within this class.
 */
@Immutable
public class WpUser {
    private final Integer id;
    private final String accountName;
    private final String displayName;
    private final String firstName;
    private final String lastName;
    private final InternetAddress email;
    private final URL website;
    private final Timestamp activatedTimestamp;

    public WpUser(Builder builder) {
        this.id = requireNonNull(builder.getId());
        this.accountName = requireNonNull(builder.getAccountName());
        this.displayName = requireNonNull(builder.getDisplayName());
        this.firstName = builder.getFirstName();
        this.lastName = builder.getLastName();
        this.email = requireNonNull(builder.getEmail());
        this.website = builder.getWebsite();
        this.activatedTimestamp = requireNonNull(builder.getActivatedTimestamp());
    }

    public Integer getId() {
        return id;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public InternetAddress getEmail() {
        return email;
    }

    public URL getWebsite() {
        return website;
    }

    public Timestamp getActivatedTimestamp() {
        return activatedTimestamp;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Entity(name="wp_users")
    @Table(name="wp_users")
    public static class Builder {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name="user_login")
        private String accountName;

        @Column(name="user_nicename")
        private String displayName;

        @Column(name="user_email")
        private String emailString;

        @Transient private InternetAddress email;

        @Column(name="user_url")
        private String websiteString;

        @Transient private URL website;

        @Column(name="user_registered")
        private Timestamp activatedTimestamp;

        @Transient private String firstName;
        @Transient private String lastName;

        public WpUser build() {
            return new WpUser(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static Builder from(WpUser wpUser) {
            return builder()
                    .withId(wpUser.getId())
                    .withAccountName(wpUser.getAccountName())
                    .withDisplayName(wpUser.getDisplayName())
                    .withEmail(wpUser.getEmail())
                    .withFirstName(wpUser.getFirstName())
                    .withLastName(wpUser.getLastName())
                    .withWebsite(wpUser.getWebsite())
                    .withActivatedTimestamp(wpUser.getActivatedTimestamp());
        }

        public Integer getId() {
            return id;
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public String getAccountName() {
            return accountName;
        }

        public Builder withAccountName(String accountName) {
            this.accountName = accountName;
            return this;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Builder withDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public String getFirstName() {
            return firstName;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public String getLastName() {
            return lastName;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public InternetAddress getEmail() {
            try {
                this.email = new InternetAddress(this.emailString);
            } catch (AddressException e) {
                e.printStackTrace();
            }
            return email;
        }

        public Builder withEmail(InternetAddress email) {
            this.email = email;
            this.emailString = email.getAddress();
            return this;
        }

        public Builder withEmailString(String emailString) {
            this.emailString = emailString;
            return this;
        }

        public URL getWebsite() {
            if (Strings.isNullOrEmpty(this.websiteString)) {
                return null;
            }

            try {
                this.website = new URL(this.websiteString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return website;
        }

        public Builder withWebsite(URL website) {
            this.website = website;
            this.websiteString = website.toString();
            return this;
        }

        public String getWebsiteString() {
            return websiteString;
        }

        public Builder withWebsiteString(String websiteString) {
            this.websiteString = websiteString;
            return this;
        }

        public Timestamp getActivatedTimestamp() {
            return activatedTimestamp;
        }

        public Builder withActivatedTimestamp(Timestamp activatedTimestamp) {
            this.activatedTimestamp = activatedTimestamp;
            return this;
        }

    }

}
