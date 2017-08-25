/**
 * Copyright 2015 Jett Marks
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Created 11/17/15.
 */
package com.clueride.domain.account.member;

import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.clueride.domain.user.Badge;
import com.clueride.service.IdProvider;
import com.clueride.service.MemoryBasedMemberIdProvider;

public class Member {
    private Integer id;
    private String displayName;
    private String firstName;
    private String lastName;
    // TODO: CA-272 bring in support for Email Addresses
//    private InternetAddress emailAddress;
    private String emailAddress;
    // TODO: CA-272 Bring in support for Phone Numbers
//    private Phonenumber.PhoneNumber phone;
    private String phoneNumber;
    private List<Badge> badges;

    /** Supporting Jackson. */
    public Member() {}

    public Member(String name) {
        this.displayName = name;
    }

    public Member(Builder builder) {
        this.id = builder.getId();
        this.displayName = builder.getDisplayName();
        this.firstName = builder.getFirstName();
        this.lastName = builder.getLastName();
        this.emailAddress = builder.getEmailAddress();
        this.phoneNumber = builder.getPhone();
        this.badges = builder.getBadges();
    }

    public Integer getId() {
        return id;
    }

    public void withId(Integer id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void withDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void withEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, Collections.singletonList("id"));
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Entity(name="member")
    public static class Builder implements com.clueride.domain.common.Builder {
        @Id
        private Integer id;

        @Column(name="display_name") private String displayName;
        @Column(name="first_name") private String firstName;
        @Column(name="last_name") private String lastName;
        @Column(name="primary_email") private String emailAddress;
        //        private InternetAddress emailAddress;
        @Column private String phone;
        //        private Phonenumber.PhoneNumber phone;
        @Transient
        private List<Badge> badges = Collections.emptyList();

        public Builder() {
            IdProvider idProvider = new MemoryBasedMemberIdProvider();
            id = idProvider.getId();
        }

        @Override
        public Member build() {
            return new Member(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static Builder from(Member member) {
            return builder()
                    .withPhone(member.getPhoneNumber())
                    .withFirstName(member.firstName)
                    .withLastName(member.lastName)
                    .withDisplayName(member.getDisplayName())
                    .withEmailAddress(member.emailAddress)
                    .withBadges(member.getBadges());
        }

        public Integer getId() {
            return id;
        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public Builder withDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public Builder withEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Builder withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public List<Badge> getBadges() {
            return badges;
        }

        public void setBadges(List<Badge> badges) {
            this.badges = badges;
        }

        public Builder withBadges(List<Badge> badges) {
            this.badges = badges;
            return this;
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

    }
}
