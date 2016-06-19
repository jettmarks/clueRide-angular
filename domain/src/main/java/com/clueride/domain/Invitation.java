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
package com.clueride.domain;

import com.clueride.domain.account.Member;
import com.clueride.service.IdProvider;
import com.clueride.service.MemoryBasedInvitationIdProvider;

/**
 * Maps a given Member to a given Outing along with a unique token that serves as the first step
 * toward authenticating that member.
 */
public class Invitation {
    private String token;
    private Member member;
    private Outing outing;
    private Integer id = null;
    private InvitationState state;

    /**
     * Builds immutable instance of Invitation from Builder instance.
     *
     * @param builder - Builder with all the data needed to construct Invitation instance.
     */
    public Invitation(Builder builder) {
        this.token = builder.getToken();
        this.id = builder.getId();
        this.outing = builder.getOuting();
        this.member = builder.getMember();
        this.state = (builder.getState() != null) ? builder.getState() : InvitationState.INITIAL;
    }

    public String getToken() {
        return token;
    }

    public Member getMember() {
        return member;
    }

    public Outing getOuting() {
        return outing;
    }

    public Integer getId() {
        return id;
    }

    public void setState(InvitationState state) {
        // TODO: Do we really want this in the immutable?
        this.state = state;
    }

    public InvitationState getState() {
        return state;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static final class Builder {

        private String token;
        private Outing outing;
        private Member member;
        private Integer id;
        private InvitationState invitationState;
        /** No-arg constructor for our use only. */
        private Builder() {
            IdProvider idProvider = new MemoryBasedInvitationIdProvider();
            id = idProvider.getId();
        }

        public static Builder builder() {
            return new Builder();
        }

        public Invitation build() {
            return new Invitation(this);
        }

        public Outing getOuting() {
            return outing;
        }

        public Builder setBuiltOuting(Outing outing) {
            this.outing = outing;
            return this;
        }

        public String getToken() {
            return token;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        /* Assists Jackson in creating instances. */
        public Builder setOuting(Outing.Builder outingBuilder) {
            this.outing = outingBuilder.build();
            return this;
        }

        public Member getMember() {
            return member;
        }

        public Builder setMember(Member member) {
            this.member = member;
            return this;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public InvitationState getState() {
            return invitationState;
        }

        public Builder setState(InvitationState invitationState) {
            this.invitationState = invitationState;
            return this;
        }

        /**
         * Token is a 64-bit representation of the invitation where the upper 32 bits
         * come from the Outing instance and the lower 32 bits come from the entire
         * object.
         *
         * This value should be calculated once, persisted, and then used unchanged from then on since
         * the token will become "public", and is expected to match up through the life-cycle
         * of the invitation.
         */
        public void evaluateToken() {
            // Unable to handle left-shift until we put this into a long
            long longToken = outing.hashCode();
            longToken <<= 32;
            longToken += this.hashCode();
            token = Long.toHexString(longToken);
        }
    }
}
