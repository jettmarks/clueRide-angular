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

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jettmarks.gmaps.encoder.Trackpoint;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import org.mockito.Mock;

import com.clueride.config.ConfigService;
import com.clueride.config.ConfigServiceImpl;
import com.clueride.dao.ClueStore;
import com.clueride.dao.CourseStore;
import com.clueride.dao.ImageStore;
import com.clueride.dao.NodeStore;
import com.clueride.dao.OutingStore;
import com.clueride.dao.PathStore;
import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.DomainGuiceProviderModule;
import com.clueride.domain.EdgeImpl;
import com.clueride.domain.GameCourse;
import com.clueride.domain.GeoNode;
import com.clueride.domain.Outing;
import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberService;
import com.clueride.domain.account.principal.EmailPrincipal;
import com.clueride.domain.account.principal.PrincipalService;
import com.clueride.domain.dev.NetworkProposal;
import com.clueride.domain.dev.NewNodeProposal;
import com.clueride.domain.dev.TrackImpl;
import com.clueride.domain.factory.PointFactory;
import com.clueride.domain.user.image.ImageService;
import com.clueride.domain.user.latlon.LatLonService;
import com.clueride.domain.user.latlon.LatLonStore;
import com.clueride.domain.user.location.LocationStore;
import com.clueride.domain.user.loctype.LocationTypeService;
import com.clueride.domain.user.place.ScoredLocationService;
import com.clueride.feature.Edge;
import com.clueride.geo.score.EasyTrack;
import com.clueride.gpx.TrackUtil;
import com.clueride.infrastructure.AuthService;
import com.clueride.infrastructure.AuthServiceImpl;
import com.clueride.infrastructure.Jpa;
import com.clueride.infrastructure.Json;
import com.clueride.service.AuthenticationService;
import com.clueride.service.InvitationService;
import com.clueride.service.LocationService;
import com.clueride.service.NodeService;
import com.clueride.service.OutingService;
import com.clueride.service.OutingServiceImpl;
import com.clueride.service.RecommendationService;
import com.clueride.service.TeamService;
import com.clueride.token.CustomClaim;
import com.clueride.token.JtiService;
import com.clueride.token.JtiServiceImpl;
import com.clueride.token.TokenService;
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
    private ImageService imageService;

    @Mock
    private ImageStore imageStore;

    @Mock
    private InvitationService invitationService;

    @Mock
    private LatLonService latLonService;

    @Mock
    private LatLonStore latLonStore;

    @Mock
    private LocationService locationService;

    @Mock
    private LocationStore locationStore;

    @Mock
    private LocationTypeService locationTypeService;

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
    private ScoredLocationService scoredLocationService;

    @Mock
    private TeamService teamService;

    @Mock
    private TokenService tokenService;

    @Override
    protected void configure() {
        initMocks(this);

        install(new DomainGuiceProviderModule());

        bind(AuthenticationService.class).toInstance(authenticationService);
        bind(AuthService.class).to(AuthServiceImpl.class);
        bind(ClueStore.class).toInstance(clueStore);
        bind(ConfigService.class).to(ConfigServiceImpl.class);
        bind(CourseStore.class).toInstance(courseStore);
        bind(ImageStore.class).toInstance(imageStore);
        bind(ImageService.class).toInstance(imageService);
        bind(InvitationService.class).toInstance(invitationService);
        bind(JtiService.class).to(JtiServiceImpl.class);
        bind(LatLonService.class).toInstance(latLonService);
        bind(LatLonStore.class).toInstance(latLonStore);
        bind(LocationStore.class).annotatedWith(Json.class).toInstance(locationStore);
        bind(LocationStore.class).annotatedWith(Jpa.class).toInstance(locationStore);
        bind(LocationStore.class).toInstance(locationStore);
        bind(LocationTypeService.class).toInstance(locationTypeService);
        bind(LocationService.class).toInstance(locationService);
        bind(MemberService.class).toInstance(memberService);
        bind(NodeService.class).toInstance(nodeService);
        bind(NodeStore.class).toInstance(nodeStore);
        bind(OutingService.class).to(OutingServiceImpl.class);
        bind(OutingStore.class).toInstance(outingStore);
        bind(PathStore.class).toInstance(pathStore);
        bind(PrincipalService.class).toInstance(principalService);
        bind(RecommendationService.class).toInstance(recommendationService);
        bind(ScoredLocationService.class).toInstance(scoredLocationService);
        bind(TeamService.class).toInstance(teamService);
        bind(TokenService.class).toInstance(tokenService);
    }

    @Provides
    private Point providePoint() {
        return PointFactory.getJtsInstance(33.771, -84.371, 0.0);
    }

    @Provides
    private GeoNode provideGeoNode(Point point) {
        return new DefaultGeoNode(point);
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

    @Provides NetworkProposal getNewNodeNetworkProposal(GeoNode geoNode) {
        return new NewNodeProposal(geoNode);
    }
}
