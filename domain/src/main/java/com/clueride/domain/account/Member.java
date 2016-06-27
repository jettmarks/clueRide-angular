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
package com.clueride.domain.account;

import java.util.List;

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

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public static final class Builder implements com.clueride.domain.common.Builder {
        private Integer id;
        private String displayName;
        private String firstName;
        private String lastName;
        private String emailAddress;
//        private InternetAddress emailAddress;
        private String phone;
//        private Phonenumber.PhoneNumber phone;
        private List<Badge> badges;

        private Builder() {
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

        public Integer getId() {
            return id;
        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Builder setDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public String getFirstName() {
            return firstName;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public String getLastName() {
            return lastName;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public Builder setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        public String getPhone() {
            return phone;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public List<Badge> getBadges() {
            return badges;
        }

        public Builder setBadges(List<Badge> badges) {
            this.badges = badges;
            return this;
        }
    }
}
