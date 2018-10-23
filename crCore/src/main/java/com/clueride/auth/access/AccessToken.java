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
 * Created by jett on 10/13/18.
 */
package com.clueride.auth.access;

/**
 * Holds the details about an Access Token.
 *
 * This starts with a String representation that can be parsed into the specific
 * attributes of what that token represents.
 */
public class AccessToken {
    private final String token;

    private AccessToken(Builder builder) {
        this.token = builder.getToken();
    }

    public String getToken() {
        return token;
    }

    public static class Builder {
        public AccessToken build() {
            return new AccessToken(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public String getToken() {
            return token;
        }

        public Builder withToken(String token) {
            this.token = token;
            return this;
        }

        private String token;
    }
}
