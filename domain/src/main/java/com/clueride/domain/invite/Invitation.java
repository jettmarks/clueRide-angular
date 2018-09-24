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
package com.clueride.domain.invite;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Maps a given Member to a given Outing along with a unique token that serves as the first step
 * toward authenticating that member.
 */
@Immutable
public class Invitation {
    private String token;
    private Integer memberId;
    private Integer outingId;
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
        this.outingId = builder.getOutingId();
        this.memberId = builder.getMemberId();
        this.state = (builder.getState() != null) ? builder.getState() : InvitationState.INITIAL;
    }

    public String getToken() {
        return token;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public Integer getOutingId() {
        return outingId;
    }

    public Integer getId() {
        return id;
    }

    public void setState(InvitationState state) {
        this.state = state;
    }

    public InvitationState getState() {
        return state;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Entity(name="invite")
    public static final class Builder implements com.clueride.domain.common.Builder<Invitation> {
        @Id
        @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="invite_pk_sequence")
        @SequenceGenerator(name="invite_pk_sequence",sequenceName="invite_id_seq", allocationSize=1)
        private Integer id;

        @Column(name="member_id")
        private Integer memberId;
        @Column(name="outing_id")       // TODO: Outing is not yet persisted in the JPA store.
        private Integer outingId;
        @Column(name="team_id")         // TODO: Team is not yet persisted in the JPA store.
        private Integer teamId;

        @Column(name="state")
        @Enumerated(EnumType.STRING)
        private InvitationState invitationState;

        @Transient
        private String token;

        public static Builder builder() {
            return new Builder();
        }

        public static Builder from(Invitation instance) {
            return builder()
                    .withId(instance.id)
                    .withOutingId(instance.getOutingId())
                    .withMemberId(instance.memberId)
                    .withState(instance.state)
                    .withToken(instance.token);
        }

        public Invitation build() {
            return new Invitation(this);
        }

        public Integer getOutingId() {
            return outingId;
        }

        public Integer getMemberId() {
            return memberId;
        }

        public Builder withMemberId(Integer memberId) {
            this.memberId = memberId;
            return this;
        }

        public String getToken() {
            return token;
        }

        public Builder withToken(String token) {
            this.token = token;
            return this;
        }

        /* Assists Jackson in creating instances. */
        public Builder withOutingId(Integer outingId) {
            this.outingId = outingId;
            return this;
        }

        public Integer getId() {
            return id;
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Integer getTeamId() {
            return teamId;
        }

        public Builder withTeamId(Integer teamId) {
            this.teamId = teamId;
            return this;
        }

        public InvitationState getState() {
            return invitationState;
        }

        public Builder withState(InvitationState invitationState) {
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
         * @deprecated - Unsure if this will be used.
         */
        public Builder evaluateToken() {
            // Unable to handle left-shift until we put this into a long
//            long longToken = outing.hashCode();
            long longToken = outingId;
            longToken <<= 32;
            longToken += this.hashCode();
            token = Long.toHexString(longToken);
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
