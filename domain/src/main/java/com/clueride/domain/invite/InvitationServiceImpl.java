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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import org.apache.log4j.Logger;

import com.clueride.domain.Team;
import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberStore;
import com.clueride.domain.account.principal.BadgeOsPrincipal;
import com.clueride.domain.account.principal.SessionPrincipal;
import com.clueride.domain.course.Course;
import com.clueride.domain.course.CourseStore;
import com.clueride.domain.course.CourseType;
import com.clueride.domain.course.CourseTypeStore;
import com.clueride.domain.outing.Outing;
import com.clueride.domain.outing.OutingStore;
import com.clueride.domain.team.TeamService;
import static java.util.Objects.requireNonNull;

/**
 * Default implementation of InvitationService.
 */
public class InvitationServiceImpl implements InvitationService {
    private static final Logger LOGGER = Logger.getLogger(InvitationServiceImpl.class);

    private final InvitationStore invitationStore;
    private final MemberStore memberStore;
    private final OutingStore outingStore;
    private final CourseStore courseStore;
    private final CourseTypeStore courseTypeStore;
    private final TeamService teamService;
    private final SessionPrincipal sessionPrincipal;

    /**
     * Injectable constructor.
     * @param invitationStore - Persistence layer for Invitations.
     * TODO: Switch remaining over to using services instead of stores.
     * @param memberStore - Persistence layer for Members.
     * @param outingStore - Persistence layer for Outings.
     * @param courseStore - Persistence layer for Courses.
     * @param courseTypeStore - Persistence layer for Course Types.
     * @param teamService - Provides Team-related services; for us, Team retrieval.
     * @param sessionPrincipal - Provides the Session's Principal.
     */
    @Inject
    public InvitationServiceImpl(
            InvitationStore invitationStore,
            MemberStore memberStore,
            OutingStore outingStore,
            CourseStore courseStore,
            CourseTypeStore courseTypeStore,
            TeamService teamService,
            SessionPrincipal sessionPrincipal
    ) {
        this.invitationStore = invitationStore;
        this.memberStore = memberStore;
        this.outingStore = outingStore;
        this.courseStore = courseStore;
        this.courseTypeStore = courseTypeStore;
        this.teamService = teamService;
        this.sessionPrincipal = sessionPrincipal;
    }

    @Override
    public InvitationFull getInvitationByToken(String token) {
        Invitation.Builder invitationBuilder = invitationStore.getInvitationByToken(token);
        Outing outing = outingStore.getOutingById(invitationBuilder.getOutingId());
        Course course = courseStore.getCourseById(outing.getCourseId());
        Team team = teamService.getTeam(outing.getTeamId());
        Member member = memberStore.getMemberById(invitationBuilder.getMemberId());

        return InvitationFull.Builder.builder()
                .setToken(token)
                .setId(invitationBuilder.getId())
                .setOuting(outing)
                .setCourse(course)
                .setCourseType(courseTypeStore.getCourseTypeById(course.getCourseTypeId()))
                .setTeam(team)
                .setMember(member)
                .build();
    }

    @Override
    public List<Invitation> getInvitationsForOuting(Integer outingId) {
        List<Invitation.Builder> builders = invitationStore.getInvitationsByOuting(outingId);
        return getInvitesFromBuilders(builders);
    }

    @Override
    public Invitation createNew(Integer outingId, Integer memberId) throws IOException {
        LOGGER.info("Creating new instance of Invitation");

        Member member = memberStore.getMemberById(memberId);
        validateMember(member);

        Invitation.Builder invitation = Invitation.Builder.builder()
                .withMemberId(member.getId())
                .withOutingId(outingId)
                .evaluateToken();        // TODO: consider what we'll do with the Token

        invitationStore.addNew(invitation);
        return invitation.build();
    }

    @Override
    public List<Invitation> send(Integer outingId) {
        List<Invitation.Builder> builders = invitationStore.getInvitationsByOuting(outingId);
        List<Invitation> invites = new ArrayList<>();
        for (Invitation.Builder builder : builders) {
            Invitation invitation = builder.build();
            sendEmail(createEmail(invitation));
            invites.add(invitation);
        }
        return invites;
    }

    @Override
    public List<Invitation> getInvitationsForSession() {
        BadgeOsPrincipal badgeOsPrincipal = (BadgeOsPrincipal) sessionPrincipal.getSessionPrincipal();
        InternetAddress emailAddress = badgeOsPrincipal.getEmailAddress();
        Integer memberId = memberStore.getMemberByEmail(emailAddress).getId();
        List<Invitation.Builder> builders = invitationStore.getUpcomingInvitationsByMemberId(memberId);
        return getInvitesFromBuilders(builders);
    }

    @Override
    public Invitation accept(Integer inviteId) {
        return updateInvitationState(inviteId, InvitationState.ACTIVE);
    }

    @Override
    public Invitation decline(Integer inviteId) {
        return updateInvitationState(inviteId, InvitationState.DECLINED);
    }

    @Override
    public Invitation expire(Integer inviteId) {
        return updateInvitationState(inviteId, InvitationState.EXPIRED);
    }

    private Invitation updateInvitationState(Integer inviteId, InvitationState inviteState) {
        requireNonNull(inviteId, "Expecting non-null Invitation ID");
        Invitation.Builder builder = invitationStore.getInvitationById(inviteId);
        builder.withState(inviteState);
        invitationStore.save(builder);
        return builder.build();
    }

    private List<Invitation> getInvitesFromBuilders(List<Invitation.Builder> builders) {
        List<Invitation> invites = new ArrayList<>();
        for (Invitation.Builder builder : builders) {
            invites.add(builder.build());
        }
        return invites;
    }

    private void sendEmail(Message email) {
        // TODO: Actually send the email
    }

    private Message createEmail(Invitation invitation) {
        Session session = null;
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            session = (Session) envCtx.lookup("mail/Session");
        } catch (NamingException e) {
            e.printStackTrace();
        }
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("clueride@gmail.com"));
            message.setSubject("Clue Ride Invitation - Calling All Seekers");
            // TODO: During test, we want to ensure all emails come to my account
            message.setRecipients(
                    Message.RecipientType.TO,
                    new InternetAddress[]{new InternetAddress("jettmarks@bellsouth.net")}
            );
            message.setContent(createEmailContent(invitation), "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    private String createEmailContent(Invitation invitation) {
        StringBuilder buffer = new StringBuilder();

        Member member = memberStore.getMemberById(invitation.getMemberId());
        Outing outing = outingStore.getOutingById(invitation.getOutingId());
        Course course = courseStore.getCourseById(outing.getCourseId());
        CourseType courseType = courseTypeStore.getCourseTypeById(course.getCourseTypeId());

        buffer.append("Dear ").append(member.getDisplayName()).append(",<p>")
                .append("You've been invited to join members of the ")
                .append(outing.getTeamId()).append(" team as they explore the ")
                .append("<i>")
                .append(course.getName())
                .append("</i> course.<p>")
                .append("Details of the course can be found by following <a href=\"")
                .append(courseType.getUrl())
                .append("\">this link</a><p>")
                .append("Follow this link to acknowledge this invitation: ")
                /*
                 * This URL is passing the invitation token twice:
                 * once before the hash for the server to pick up,
                 * and once after the hash for the angular $routeParams to pick up.
                 */
                .append("<a href=\"http://localhost:8080/?inviteToken=")
                .append(invitation.getToken())
                .append("#/invitation?inviteToken=")
                .append(invitation.getToken())
                .append("\">Yes, I plan to Attend</a>");
        return buffer.toString();
    }

    private void validateMember(Member member) {
        if (Strings.isNullOrEmpty(member.getDisplayName())) {
            throw new IllegalAccessError("Expected Member Name to be non-empty");
        }

        if (Strings.isNullOrEmpty(member.getEmailAddress())) {
            throw new IllegalArgumentException("Expected Email Address to be non-empty");
        }
    }
}
