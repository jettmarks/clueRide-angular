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
 * Created by jett on 3/11/16.
 */
package com.clueride.service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.clueride.domain.InvitationFull;
import com.clueride.domain.account.OAuthCredentials;
import com.clueride.domain.account.member.Member;
import com.clueride.domain.user.Badge;
import com.clueride.member.MemberService;
import com.clueride.principal.EmailPrincipal;
import com.clueride.rest.dto.CRCredentials;
import com.clueride.token.TokenService;

/**
 * Implementation of Authentication services.
 */
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger LOGGER = Logger.getLogger(AuthenticationServiceImpl.class);
    private final TeamService teamService;
    private final MemberService memberService;
    private final TokenService tokenService;

    @Inject
    public AuthenticationServiceImpl(
            TeamService teamService,
            MemberService memberService,
            TokenService tokenService
    ) {
        this.teamService = teamService;
        this.memberService = memberService;
        this.tokenService = tokenService;
    }

    // TODO: Eventually, this will also hold the selected Team/Outing

    @Override
    public List<Badge> loginReturningBadges(CRCredentials crCredentials) {
        String displayName = crCredentials.name;
        LOGGER.info("Retrieving badges for user " + displayName);

        List<Badge> result = Collections.emptyList();

        // TODO: Add the check of the password when coming in from this direction
//        if ("Jett".equals(displayName) && "adfhg".equals(crCredentials.password)) {

        Member member = null;
        List<Member> members = memberService.getMemberByDisplayName(crCredentials.name);
        if (members.size() == 1) {
            member = members.get(0);
        } else {
            LOGGER.error("More than one Member with the display Name: " + displayName);
        }
        if (member != null) {
            result = member.getBadges();
        }

        // TODO: Hardcoded the team here; probably should go with establishing the outing
        teamService.addMember(2, member);
        return result;
    }

    @Override
    public List<Badge> loginReturningBadges(InvitationFull invitation) {
        return invitation.getMember().getBadges();
    }

    @Override
    public List<Badge> loginReturningBadges(OAuthCredentials oaCredentials) {
        // Here's where we connect the OAuthCredentials with one of our users (or not).

        // Meanwhile, let us in
        return Collections.singletonList(Badge.LOCATION_EDITOR);
    }

    @Override
    public String establishSession(List<Badge> badges, HttpServletRequest request) {
        request.getSession(true);
        HttpSession session = request.getSession();
        LOGGER.debug("Session created");
        session.setMaxInactiveInterval(12 * 60 * 60);
        session.setAttribute("badges", badges);
        // TODO: This is a temporary hardcoding of the Outing ID for anyone logging in
        session.setAttribute("outingId", 2);
        return tokenService.generateSignedToken();
    }

    @Override
    public Principal getPrincipal(CRCredentials crCredentials) {
        Member member = memberService.getMemberByEmail(crCredentials.name);
        return new EmailPrincipal(member.getEmailAddress());
    }

}
