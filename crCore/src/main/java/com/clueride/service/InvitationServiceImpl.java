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
package com.clueride.service;

import java.io.IOException;
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

import com.clueride.dao.CourseStore;
import com.clueride.dao.CourseTypeStore;
import com.clueride.dao.InvitationStore;
import com.clueride.domain.Course;
import com.clueride.domain.CourseType;
import com.clueride.domain.Invitation;
import com.clueride.domain.InvitationFull;
import com.clueride.domain.Outing;
import com.clueride.domain.Team;
import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberStore;

/**
 * Default implementation of InvitationService.
 */
public class InvitationServiceImpl implements InvitationService {
    private static final Logger LOGGER = Logger.getLogger(InvitationServiceImpl.class);

    private final InvitationStore invitationStore;
    private final MemberStore memberStore;
    private final CourseStore courseStore;
    private final CourseTypeStore courseTypeStore;
    private final TeamService teamService;

    /**
     * Injectable constructor.
     * @param invitationStore - Persistence layer for Invitations.
     * @param memberStore - Persistence layer for Members.
     * @param courseStore - Persistence layer for Courses.
     * @param courseTypeStore - Persistence layer for Course Types.
     * @param teamService - Provides Team-related services; for us, Team retrieval.
     */
    @Inject
    public InvitationServiceImpl(
            InvitationStore invitationStore,
            MemberStore memberStore,
            CourseStore courseStore,
            CourseTypeStore courseTypeStore,
            TeamService teamService
    ) {
        this.invitationStore = invitationStore;
        this.memberStore = memberStore;
        this.courseStore = courseStore;
        this.courseTypeStore = courseTypeStore;
        this.teamService = teamService;
    }

    @Override
    public InvitationFull getInvitationByToken(String token) {
        Invitation invitation = invitationStore.getInvitationByToken(token);
        Outing outing = invitation.getOuting();
        Course course = courseStore.getCourseById(outing.getCourseId());
        Team team = teamService.getTeam(outing.getTeamId());
        Member member = memberStore.getMemberById(invitation.getMemberId());

        return InvitationFull.Builder.builder()
                .setToken(token)
                .setId(invitation.getId())
                .setOuting(outing)
                .setCourse(course)
                .setCourseType(courseTypeStore.getCourseTypeById(course.getCourseTypeId()))
                .setTeam(team)
                .setMember(member)
                .build();
    }

    @Override
    public List<Invitation> getInvitationsForOuting(Integer outingId) {
        return invitationStore.getInvitationsByOuting(outingId);
    }

    @Override
    public Invitation createNew(Outing outing, Integer memberId) throws IOException {
        LOGGER.info("Creating new instance of Invitation");

        Member member = memberStore.getMemberById(memberId);
        validateMember(member);
        validateOuting(outing);

        Invitation invitation = Invitation.Builder.builder()
                .setMemberId(member.getId())
                .setBuiltOuting(outing)
                .evaluateToken()
                .build();

        invitationStore.addNew(invitation);
        return invitation;
    }

    @Override
    public List<Invitation> send(Integer outingId) {
        List<Invitation> invitations = invitationStore.getInvitationsByOuting(outingId);
        for (Invitation invitation : invitations) {
            sendEmail(createEmail(invitation));
        }
        return invitations;
    }

    private void sendEmail(Message email) {
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
        StringBuffer buffer = new StringBuffer();

        Member member = memberStore.getMemberById(invitation.getMemberId());
        Course course = courseStore.getCourseById(invitation.getOuting().getCourseId());
        CourseType courseType = courseTypeStore.getCourseTypeById(course.getCourseTypeId());

        buffer.append("Dear ").append(member.getDisplayName()).append(",<p>")
                .append("You've been invited to join members of the ")
                .append(invitation.getOuting().getTeamId()).append(" team as they explore the ")
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

    private void validateOuting(Outing outing) {
        // Placeholder
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
