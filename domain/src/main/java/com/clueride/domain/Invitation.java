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

/**
 * Maps a given Member to a given Outing along with a unique token that serves as the first step
 * toward authenticating that member.
 */
public class Invitation {
    private String token;
    private Member member;
    private Outing outing;

    public InvitationState getState() {
        return state;
    }

    public void setState(InvitationState state) {
        this.state = state;
    }

    private InvitationState state;

    /**
     * Instantiates an Invitation based on the Outing and Member.
     * @param outing - Where we're riding and at what time.
     * @param member - The Guest being invited.
     */
    public Invitation(Outing outing, Member member) {
        this.outing = outing;
        this.member = member;
        this.state = InvitationState.INITIAL;
        evaluateToken();
    }

    /**
     * Token is a 64-bit representation of the invitation where the upper 32 bits
     * come from the Outing instance and the lower 32 bits come from the entire
     * object.
     */
    private void evaluateToken() {
        // Unable to handle left-shift until we put this into a long
        long longToken = outing.hashCode();
        longToken <<= 32;
        longToken += this.hashCode();
        token = Long.toHexString(longToken);
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
}
