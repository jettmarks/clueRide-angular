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
 * Created by jett on 8/13/17.
 */
package com.clueride.domain;

import java.net.MalformedURLException;
import java.security.Principal;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.mockito.Mock;

import com.clueride.aop.AopDummyService;
import com.clueride.aop.AopModuleTest;
import com.clueride.domain.account.member.MemberService;
import com.clueride.domain.account.member.MemberStore;
import com.clueride.domain.account.principal.BadgeOsPrincipal;
import com.clueride.domain.account.principal.BadgeOsPrincipalService;
import com.clueride.domain.account.principal.EmailPrincipal;
import com.clueride.domain.account.principal.PrincipalService;
import com.clueride.domain.account.wpuser.WpUserStore;
import com.clueride.domain.badge.Badge;
import com.clueride.domain.badge.BadgeStore;
import com.clueride.domain.badge.BadgeType;
import com.clueride.domain.badge.BadgeTypeService;
import com.clueride.domain.badge.event.BadgeEventService;
import com.clueride.domain.badge.event.BadgeEventStore;
import com.clueride.domain.course.CourseStore;
import com.clueride.domain.course.CourseTypeStore;
import com.clueride.domain.invite.InvitationService;
import com.clueride.domain.invite.InvitationServiceImpl;
import com.clueride.domain.invite.InvitationStore;
import com.clueride.domain.outing.OutingStore;
import com.clueride.domain.session.SessionPrincipal;
import com.clueride.domain.ssevent.SSEventService;
import com.clueride.domain.team.TeamService;
import com.clueride.domain.user.image.ImageStore;
import com.clueride.domain.user.image.ImageStoreJpa;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Guice Bindings for the Testing of the Domain Module.
 */
public class DomainGuiceModuleTest extends AbstractModule {
    private boolean runWithDB = ("true".equals(System.getProperty("db.available")));

    @Mock
    private AopDummyService dummyService;

    @Mock
    private BadgeEventService badgeEventService;

    @Mock
    private BadgeEventStore badgeEventStore;

    @Mock
    private BadgeOsPrincipalService badgeOsPrincipalService;

    @Mock
    private BadgeStore badgeStore;

    @Mock
    private BadgeTypeService badgeTypeService;

    @Mock
    private CourseStore courseStore;

    @Mock
    private CourseTypeStore courseTypeStore;

    @Mock
    private ImageStore imageStore;

    @Mock
    private InvitationStore invitationStore;

    @Mock
    private MemberService memberService;

    @Mock
    private MemberStore memberStore;

    @Mock
    private OutingStore outingStore;

    @Mock
    private PrincipalService principleService;

    @Mock
    private SessionPrincipal sessionPrincipal;

    @Mock
    private SSEventService ssEventService;

    @Mock
    private TeamService teamService;

    @Mock
    private WpUserStore wpUserStore;

    @Override
    protected void configure() {
        initMocks(this);

        install(new DomainGuiceProviderModule());
        install(new AopModuleTest());

        if (runWithDB) {
            bind(ImageStore.class).to(ImageStoreJpa.class);
        } else {
            bind(ImageStore.class).toInstance(imageStore);
        }
        bind(AopDummyService.class).toInstance(dummyService);
        bind(BadgeEventService.class).toInstance(badgeEventService);
        bind(BadgeEventStore.class).toInstance(badgeEventStore);
        bind(BadgeOsPrincipalService.class).toInstance(badgeOsPrincipalService);
        bind(BadgeStore.class).toInstance(badgeStore);
        bind(BadgeTypeService.class).toInstance(badgeTypeService);
        bind(CourseStore.class).toInstance(courseStore);
        bind(CourseTypeStore.class).toInstance(courseTypeStore);
        bind(InvitationStore.class).toInstance(invitationStore);
        bind(MemberService.class).toInstance(memberService);
        bind(MemberStore.class).toInstance(memberStore);
        bind(OutingStore.class).toInstance(outingStore);
        bind(PrincipalService.class).toInstance(principleService);
        bind(SessionPrincipal.class).toInstance(sessionPrincipal);
        bind(SSEventService.class).toInstance(ssEventService);
        bind(TeamService.class).toInstance(teamService);
        bind(WpUserStore.class).toInstance(wpUserStore);

    }

    @Provides
    private Principal getEmailPrincipal() {
        return new EmailPrincipal("test.different@clueride.com");
    }

    @Provides
    private Badge.Builder getBadgeBuilder() throws MalformedURLException {
        return Badge.Builder.builder()
                .withId(10)
                .withUserId(2)
                .withBadgeType(BadgeType.SEEKER)
                .withBadgeName("seekers")
                .withBadgeLevel("aware")
                .withImageUrlString("https://clueride.com/favicon")
                .withBaseUrlString("https://clueride.com/?post_id=3996");
    }

    @Provides
    private BadgeOsPrincipal getBadgeOsPrincipal() {
        InternetAddress emailAddress = null;
        try {
            emailAddress = new InternetAddress("test.different@clueride.com");
        } catch (AddressException e) {
            e.printStackTrace();
        }
        return BadgeOsPrincipal.Builder.builder()
                .withBadgeOsUserId(2)
                .withEmailAddress(emailAddress)
                .withName("Test Account")
                .build();
    }

    @Provides
    private InvitationService getInvitationService(
            InvitationStore invitationStore,
            MemberStore memberStore,
            OutingStore outingStore,
            CourseStore courseStore,
            CourseTypeStore courseTypeStore,
            TeamService teamService,
            SessionPrincipal sessionPrincipal
    ) {
        return new InvitationServiceImpl(
                invitationStore,
                memberStore,
                outingStore,
                courseStore,
                courseTypeStore,
                teamService,
                sessionPrincipal
        );
    }


}
