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
 * Created by jett on 10/7/17.
 */
package com.clueride.domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.principal.EmailPrincipal;
import com.clueride.domain.badge.event.BadgeEvent;
import com.clueride.domain.user.Badge;
import com.clueride.domain.user.answer.Answer;
import com.clueride.domain.user.answer.AnswerKey;
import com.clueride.domain.user.image.Image;
import com.clueride.domain.user.image.ImageServiceImpl;
import com.clueride.domain.user.location.Location;
import com.clueride.domain.user.loctype.LocationType;
import com.clueride.domain.user.puzzle.Puzzle;
import com.clueride.infrastructure.ClientSourced;
import com.clueride.infrastructure.DbSourced;
import com.clueride.infrastructure.JpaUtil;
import com.clueride.infrastructure.ServiceSourced;
import com.clueride.infrastructure.db.ClueRide;
import com.clueride.infrastructure.db.WordPress;

/**
 * Guice Providers that may be pulled into Tests of other modules.
 */
public class DomainGuiceProviderModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    @ClueRide
    private EntityManager getClueRideEntityManager() {
        return JpaUtil.getClueRideEntityManagerFactory().createEntityManager();
    }

    @Provides
    @WordPress
    private EntityManager getWordPressEntityManager() {
        return JpaUtil.getWordPressEntityManagerFactory().createEntityManager();
    }

    @Provides
    private Puzzle.Builder getPuzzleBuilder() {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer(AnswerKey.A, "Answer A"));
        answers.add(new Answer(AnswerKey.B, "42"));
        answers.add(new Answer(AnswerKey.C, "Answer C"));
        answers.add(new Answer(AnswerKey.D, "Answer D"));

        return Puzzle.Builder.builder()
                .withId(1)
                .withName("Test Puzzle")
                .withQuestion("What is the Meaning of Life")
                .withAnswers(answers)
                .withCorrectAnswer(AnswerKey.B);
    }

    @Provides
    private Location provideLocation(
            LocationType locationType,
            Puzzle.Builder puzzleBuilder
    ) {
        return getLocationBuilder(locationType, puzzleBuilder).build();
    }

    @Provides
    private Location.Builder getLocationBuilder(
            LocationType locationType,
            Puzzle.Builder puzzleBuilder
    ) {
        Location.Builder locationBuilder = Location.Builder.builder()
                .withId(1)
                .withNodeId(100)
                .withName("Test Location")
                .withPuzzleBuilders(Collections.singletonList(puzzleBuilder))
                .withDescription("Beautiful Test")
                .withLocationType(locationType)
                .withTagScores(Collections.EMPTY_MAP);

        try {
            locationBuilder.withFeaturedImage(new URL("https://img.clueride.com/test.png"));
            locationBuilder.withImageUrls(Collections.singletonList(new URL("https://img.clueride.com/test.png")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return locationBuilder;
    }

    @Provides
    private LocationType provideLocationType() {
        return LocationType.Builder.builder()
                .withId(10)
                .withName("Picnic")
                .withDescription("Parks, Cemeteries, Squares")
                .withIcon("basket")
                .build();
    }

    /**
     * Image Builder appears similar to what comes out of the Store/DAO.
     * @return partially constructed Image.Builder instance.
     */
    @Provides
    private Image.Builder provideImageBuilder() {
        String urlString = "https://images.clueride.com/img/4/1.jpg";
        return Image.Builder.builder()
                .withId(10)
                .withUrlString(urlString);
    }

    /**
     * Provides fully-constructed instance based on the provided imageBuilder.
     * @param imageBuilder contains data similar to what comes out of DataStore.
     * @return fully-constructed immutable instance.
     */
    @Provides
    private Image provideImage(
           Image.Builder imageBuilder
    ) {
        try {
            return imageBuilder
                    .withUrl(new URL(imageBuilder.getUrlString()))
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Provides
    private EmailPrincipal getEmailPrincipal() {
        return new EmailPrincipal("guest.dummy@clueride.com");
    }

    @Provides
    private Member getMember() {
        return Member.Builder.builder()
                .withId(-1)
                .withFirstName("ClueRide")
                .withLastName("Guest")
                .withEmailAddress("guest.dummy@clueride.com")
                .withBadges(Collections.singletonList(Badge.LOCATION_EDITOR))
                .withDisplayName("ClueRide Guest")
                .withPhone("123-456-7890")
                .build();
    }

    @Provides
    @DbSourced
    private BadgeEvent.Builder getDbSourcedBadgeEvent(
            Member member
    ) {
        return BadgeEvent.Builder.builder()
                .withTimestamp(new Date())
                .withId(123)
                .withMemberId(member.getId())
                .withClassName("com.clueride.domain.user.ImageServiceImpl")
                .withMethodName("addNewToLocation")
                .withReturnValue("1")
                ;
    }

    @Provides
    @ServiceSourced
    private BadgeEvent.Builder getBadgeEventBuilder (
            EmailPrincipal principal
    ) {
        return BadgeEvent.Builder.builder()
                .withId(123)
                .withPrincipal(principal)
                .withMemberId(123)
                .withReturnValue(123)
                .withTimestamp(new Date())
                .withMethodName("addNewToLocation")
                .withMethodClass(ImageServiceImpl.class)
                .withClassName("com.clueride.domain.user.ImageServiceImpl")
                .withReturnValue(1)
                ;
    }

    @Provides
    @ClientSourced
    private BadgeEvent.Builder getBadgeEventBuilderClientSourced(
            EmailPrincipal principal
    ) {
        return BadgeEvent.Builder.builder()
                .withPrincipal(principal)
                .withReturnValue(123)
                .withTimestamp(new Date())
                .withMethodName("addNewToLocation")
                .withMethodClass(ImageServiceImpl.class)
                .withReturnValue(1)
                ;
    }

    @Provides
    private BadgeEvent getBadgeEvent(
            @ServiceSourced BadgeEvent.Builder badgeEventBuilder
    ) {
        return badgeEventBuilder.build();
    }

    @Provides
    @DbSourced
    private com.clueride.domain.badge.Badge.Builder getDbBadgeBuilder() {
           com.clueride.domain.badge.Badge.Builder builder
           = com.clueride.domain.badge.Badge.Builder.builder();

        try {
            builder
                    .withId(3376)
                    .withImageUrlString("http://clueride.com/favicon.ico")
                    .withCriteriaUrlString("http://clueride.com/?post_id=3376");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return builder;
    }
}
