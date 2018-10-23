/*
 * Copyright 2015 Jett Marks
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
 * Created by jett on 12/10/15.
 */
package com.clueride;

import java.security.Principal;

import com.google.inject.AbstractModule;

import com.clueride.auth.Auth0Connection;
import com.clueride.auth.Auth0ConnectionImpl;
import com.clueride.auth.access.AccessTokenService;
import com.clueride.auth.access.AccessTokenServiceImpl;
import com.clueride.auth.identity.IdentityStore;
import com.clueride.auth.identity.IdentityStoreAuth0;
import com.clueride.config.ConfigService;
import com.clueride.config.ConfigServiceImpl;
import com.clueride.dao.FileImageStore;
import com.clueride.dao.ImageStore;
import com.clueride.dao.JsonLocationStore;
import com.clueride.domain.account.member.MemberService;
import com.clueride.domain.account.member.MemberServiceImpl;
import com.clueride.domain.account.principal.EmailPrincipal;
import com.clueride.domain.account.principal.PrincipalService;
import com.clueride.domain.account.principal.PrincipalServiceImpl;
import com.clueride.domain.team.TeamService;
import com.clueride.domain.team.TeamServiceImpl;
import com.clueride.domain.user.location.LocationStore;
import com.clueride.domain.user.location.LocationStoreJpa;
import com.clueride.infrastructure.AuthService;
import com.clueride.infrastructure.AuthServiceImpl;
import com.clueride.infrastructure.Jpa;
import com.clueride.infrastructure.Json;
import com.clueride.service.*;
import com.clueride.token.JtiService;
import com.clueride.token.JtiServiceImpl;
import com.clueride.token.TokenService;
import com.clueride.token.TokenServiceJwt;

/**
 * Bindings for Guice in the Clueride Module.
 */
public class CluerideGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AccessTokenService.class).to(AccessTokenServiceImpl.class);
        bind(Auth0Connection.class).to(Auth0ConnectionImpl.class);
        bind(AuthService.class).to(AuthServiceImpl.class);
        bind(AuthenticationService.class).to(AuthenticationServiceImpl.class);
        bind(RecommendationService.class).to(DefaultRecommendationService.class);
        bind(ConfigService.class).to(ConfigServiceImpl.class);
        bind(ControlService.class).to(DefaultControlService.class);
        bind(CourseService.class).to(CourseServiceImpl.class);
        bind(CourseTypeService.class).to(CourseTypeServiceImpl.class);
        bind(ClueService.class).to(ClueServiceImpl.class);
        bind(DiagnosticService.class).to(DiagnosticServiceImpl.class);
        bind(IdentityStore.class).to(IdentityStoreAuth0.class);
        bind(ImageStore.class).to(FileImageStore.class);
        bind(JtiService.class).to(JtiServiceImpl.class);
        bind(LocationService.class).to(DefaultLocationService.class);
        bind(LocationStore.class).annotatedWith(Json.class).to(JsonLocationStore.class);
        bind(LocationStore.class).annotatedWith(Jpa.class).to(LocationStoreJpa.class);
        bind(MemberService.class).to(MemberServiceImpl.class);
        bind(Network.class).to(DefaultNetwork.class);
        bind(NetworkEval.class).to(NetworkEvalImpl.class);
        bind(NodeService.class).to(DefaultNodeService.class);
        bind(OutingService.class).to(OutingServiceImpl.class);
        bind(PathService.class).to(PathServiceImpl.class);
        bind(Principal.class).to(EmailPrincipal.class);
        bind(PrincipalService.class).to(PrincipalServiceImpl.class);
        bind(TeamService.class).to(TeamServiceImpl.class);
        bind(TokenService.class).to(TokenServiceJwt.class);
    }
}
