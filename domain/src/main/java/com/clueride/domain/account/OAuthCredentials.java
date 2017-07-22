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
 * Created by jett on 7/13/17.
 */
package com.clueride.domain.account;

import java.net.URL;

import javax.mail.internet.InternetAddress;

/**
 * Placeholder for the object that is provided by an OAuth session that we use
 * to find our internal user account.
 */
public class OAuthCredentials {
    private InternetAddress email;
    private URL profilePicture;

    private OAuthCredentials(Builder builder) {
        this.email = builder.getEmail();
        this.profilePicture = builder.getProfilePicture();
    }

    public InternetAddress getEmail() {
        return email;
    }

    public URL getProfilePicture() {
        return profilePicture;
    }

    public static final class Builder {
        private InternetAddress email;
        private URL profilePicture;

        public Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public InternetAddress getEmail() {
            return email;
        }
        public Builder setEmail(InternetAddress email) {
            this.email = email;
            return this;
        }

        public URL getProfilePicture() {
            return profilePicture;
        }

        public Builder setProfilePicture(URL profilePicture) {
            this.profilePicture = profilePicture;
            return this;
        }

        public OAuthCredentials build() {
            return new OAuthCredentials(this);
        }
    }

}
