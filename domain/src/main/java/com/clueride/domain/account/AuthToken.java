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
 * Created by jett on 5/29/16.
 */
package com.clueride.domain.account;

/**
 * Holds information about a User's Authentication status against a particular Outing.
 *
 * Logic is within the AuthenticationService.
 */
public class AuthToken {
    private final String id;
    private final String invitationToken;
    private final String memberToken;
    private final String jSessionId;

    public String getId() {
        return id;
    }

    public String getInvitationToken() {
        return invitationToken;
    }

    public String getMemberToken() {
        return memberToken;
    }

    public String getjSessionId() {
        return jSessionId;
    }

    public AuthToken(Builder builder) {
        this.id = builder.getId();
        this.invitationToken = builder.getInvitationToken();
        this.memberToken = builder.getMemberToken();
        this.jSessionId = builder.getjSessionId();
    }

    public static final class Builder {
        private String id;
        private String invitationToken;
        private String memberToken;
        private String jSessionId;

        public String getId() {
            return id;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public String getInvitationToken() {
            return invitationToken;
        }

        public Builder withInvitationToken(String invitationToken) {
            this.invitationToken = invitationToken;
            return this;
        }

        public String getMemberToken() {
            return memberToken;
        }

        public Builder withMemberToken(String memberToken) {
            this.memberToken = memberToken;
            return this;
        }
        public String getjSessionId() {
            return jSessionId;
        }
        public Builder withjSessionId(String jSessionId) {
            this.jSessionId = jSessionId;
            return this;
        }
    }
}
