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

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.mockito.Mock;

import com.clueride.aop.AopDummyService;
import com.clueride.aop.AopModuleTest;
import com.clueride.domain.account.member.MemberService;
import com.clueride.domain.account.member.MemberStore;
import com.clueride.domain.account.principal.EmailPrincipal;
import com.clueride.domain.account.principal.PrincipalService;
import com.clueride.domain.account.principal.SessionPrincipal;
import com.clueride.domain.badge.Badge;
import com.clueride.domain.badge.BadgeType;
import com.clueride.domain.badge.event.BadgeEventService;
import com.clueride.domain.badge.event.BadgeEventStore;
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
    private ImageStore imageStore;

    @Mock
    private MemberService memberService;

    @Mock
    private MemberStore memberStore;

    @Mock
    private PrincipalService principleService;

    @Mock
    private SessionPrincipal sessionPrincipal;

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
        bind(MemberService.class).toInstance(memberService);
        bind(MemberStore.class).toInstance(memberStore);
        bind(PrincipalService.class).toInstance(principleService);
        bind(SessionPrincipal.class).toInstance(sessionPrincipal);

    }

    @Provides
    private Principal getEmailPrincipal() {
        return new EmailPrincipal("test.different@clueride.com");
    }

    @Provides
    private Badge.Builder getBadgeBuilder() throws MalformedURLException {
        return Badge.Builder.builder()
                .withId(10)
                .withBadgeType(BadgeType.SEEKER)
                .withImageUrlString("https://clueride.com/favicon")
                .withCriteriaUrlString("https://clueride.com/hmm");
    }
}
