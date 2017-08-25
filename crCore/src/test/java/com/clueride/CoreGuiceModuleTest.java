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
 * Created by jett on 7/25/17.
 */
package com.clueride;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jettmarks.gmaps.encoder.Trackpoint;
import com.vividsolutions.jts.geom.LineString;
import org.mockito.Mock;

import com.clueride.config.ConfigService;
import com.clueride.config.ConfigServiceImpl;
import com.clueride.dao.ClueStore;
import com.clueride.dao.CourseStore;
import com.clueride.dao.ImageStore;
import com.clueride.dao.NodeStore;
import com.clueride.dao.OutingStore;
import com.clueride.dao.PathStore;
import com.clueride.domain.EdgeImpl;
import com.clueride.domain.GameCourse;
import com.clueride.domain.Outing;
import com.clueride.domain.account.member.Member;
import com.clueride.domain.dev.TrackImpl;
import com.clueride.domain.user.Badge;
import com.clueride.domain.user.Location;
import com.clueride.domain.user.LocationType;
import com.clueride.feature.Edge;
import com.clueride.geo.score.EasyTrack;
import com.clueride.gpx.TrackUtil;
import com.clueride.infrastructure.AuthService;
import com.clueride.infrastructure.AuthServiceImpl;
import com.clueride.member.MemberService;
import com.clueride.principal.EmailPrincipal;
import com.clueride.principal.PrincipalService;
import com.clueride.service.AuthenticationService;
import com.clueride.service.InvitationService;
import com.clueride.service.LocationService;
import com.clueride.service.NodeService;
import com.clueride.service.OutingService;
import com.clueride.service.OutingServiceImpl;
import com.clueride.service.RecommendationService;
import com.clueride.token.CustomClaim;
import com.clueride.token.JtiService;
import com.clueride.token.JtiServiceImpl;
import com.clueride.token.TokenService;
import static java.util.Arrays.asList;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Configures bindings for Core Guice module testing.
 */
public class CoreGuiceModuleTest extends AbstractModule {
    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private ClueStore clueStore;

    @Mock
    private CourseStore courseStore;

    @Mock
    private ImageStore imageStore;

    @Mock
    private InvitationService invitationService;

    @Mock
    private LocationService locationService;

    @Mock
    private MemberService memberService;

    @Mock
    private NodeStore nodeStore;

    @Mock
    private NodeService nodeService;

    @Mock
    private OutingStore outingStore;

    @Mock
    private PathStore pathStore;

    @Mock
    private PrincipalService principalService;

    @Mock
    private RecommendationService recommendationService;

    @Mock
    private TokenService tokenService;

    @Override
    protected void configure() {
        initMocks(this);
        bind(AuthenticationService.class).toInstance(authenticationService);
        bind(AuthService.class).to(AuthServiceImpl.class);
        bind(ClueStore.class).toInstance(clueStore);
        bind(ConfigService.class).to(ConfigServiceImpl.class);
        bind(CourseStore.class).toInstance(courseStore);
        bind(ImageStore.class).toInstance(imageStore);
        bind(InvitationService.class).toInstance(invitationService);
        bind(JtiService.class).to(JtiServiceImpl.class);
        bind(LocationService.class).toInstance(locationService);
        bind(MemberService.class).toInstance(memberService);
        bind(NodeService.class).toInstance(nodeService);
        bind(NodeStore.class).toInstance(nodeStore);
        bind(OutingStore.class).toInstance(outingStore);
        bind(PathStore.class).toInstance(pathStore);
        bind(PrincipalService.class).toInstance(principalService);
        bind(RecommendationService.class).toInstance(recommendationService);
        bind(TokenService.class).toInstance(tokenService);
        bind(OutingService.class).to(OutingServiceImpl.class);
    }

    @Provides
    private Location provideLocation() {
        try {
            return Location.Builder.builder()
                    .withId(1)
                    .withNodeId(100)
                    .withName("Test Location")
                    .withClueIds(asList(1, 2, 3))
                    .withDescription("Beautiful Test")
                    .withLocationType(LocationType.PICNIC)
                    .withImageUrls(Collections.singletonList(new URL("https://img.clueride.com/dummy.png")))
                    .withTagScores(Collections.EMPTY_MAP)
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Provides
    private Outing provideOuting() {
        return Outing.Builder.builder()
                .withId(101)
                .withCourseId(2)
                .withTeamId(101)
                .build();
    }

    @Provides
    private GameCourse provideGameCourse() {
        return GameCourse.Builder.getBuilder()
                .withName("Test Course")
                .withDescription("Trying out the Builder for a Game Course")
                .build();
    }

    @Provides
    private TrackImpl getTrackImpl() {
        return new TrackImpl(
                "Test Track",
                "dummy URL"
        );
    }

    @Provides
    private LineString getLineString() {
        return TrackUtil.getLineString(
                new EasyTrack(
                        new Trackpoint(-83.0, 33.0),
                        new Trackpoint(-83.0, 34.0)
                )
        );
    }

    @Provides
    private Edge provideEdge(
            TrackImpl trackFeature,
            LineString lineString
    ) {
        return new EdgeImpl(
                trackFeature,
                lineString
        );
    }

    @Provides
    private Principal getPrincipal(
            Member member
    ) {
        return new EmailPrincipal(member.getEmailAddress());
    }

    @Provides
    private JWTCreator.Builder getJwtBuilder(
            JtiService jtiService,
            Member member
    ) {
        Date now = new Date();
        Date inASecond = new Date(now.getTime() + 1000);

        Map<String, Object> headerClaims = new HashMap<>();
        headerClaims.put(CustomClaim.BADGES, member.getBadges());
        headerClaims.put(CustomClaim.EMAIL, member.getEmailAddress());

        return JWT.create()
                .withHeader(headerClaims)
                .withJWTId(jtiService.registerNewId())
                .withExpiresAt(inASecond);
    }

    @Provides
    private Member getMember() {
        return Member.Builder.builder()
                .withFirstName("ClueRide")
                .withLastName("Guest")
                .withEmailAddress("guest.dummy@clueride.com")
                .withBadges(Collections.singletonList(Badge.LOCATION_EDITOR))
                .withDisplayName("ClueRide Guest")
                .withPhone("123-456-7890")
                .build();
    }
}
