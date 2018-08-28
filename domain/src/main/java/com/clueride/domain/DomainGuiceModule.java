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

import com.google.inject.AbstractModule;

import com.clueride.domain.account.member.MemberService;
import com.clueride.domain.account.member.MemberServiceImpl;
import com.clueride.domain.account.member.MemberStore;
import com.clueride.domain.account.member.MemberStoreJpa;
import com.clueride.domain.account.principal.PrincipalService;
import com.clueride.domain.account.principal.PrincipalServiceImpl;
import com.clueride.domain.account.principal.SessionPrincipal;
import com.clueride.domain.account.principal.SessionPrincipalImpl;
import com.clueride.domain.badge.BadgeService;
import com.clueride.domain.badge.BadgeServiceImpl;
import com.clueride.domain.badge.BadgeStore;
import com.clueride.domain.badge.BadgeStoreJpa;
import com.clueride.domain.badge.BadgeTypeService;
import com.clueride.domain.badge.BadgeTypeServiceMappedImpl;
import com.clueride.domain.badge.event.BadgeEventService;
import com.clueride.domain.badge.event.BadgeEventServiceImpl;
import com.clueride.domain.badge.event.BadgeEventStore;
import com.clueride.domain.badge.event.BadgeEventStoreJpa;
import com.clueride.domain.user.image.ImageService;
import com.clueride.domain.user.image.ImageServiceImpl;
import com.clueride.domain.user.image.ImageStore;
import com.clueride.domain.user.image.ImageStoreJpa;
import com.clueride.domain.user.latlon.LatLonService;
import com.clueride.domain.user.latlon.LatLonServiceImpl;
import com.clueride.domain.user.latlon.LatLonStore;
import com.clueride.domain.user.latlon.LatLonStoreJpa;
import com.clueride.domain.user.layer.MapLayerService;
import com.clueride.domain.user.layer.MapLayerServiceImpl;
import com.clueride.domain.user.layer.MapLayerStore;
import com.clueride.domain.user.layer.MapLayerStoreJpa;
import com.clueride.domain.user.loctype.LocationTypeService;
import com.clueride.domain.user.loctype.LocationTypeServiceImpl;
import com.clueride.domain.user.loctype.LocationTypeStore;
import com.clueride.domain.user.loctype.LocationTypeStoreJpa;
import com.clueride.domain.user.place.ScoredLocationService;
import com.clueride.domain.user.place.ScoredLocationServiceImpl;
import com.clueride.domain.user.puzzle.PuzzleService;
import com.clueride.domain.user.puzzle.PuzzleServiceImpl;
import com.clueride.domain.user.puzzle.PuzzleStore;
import com.clueride.domain.user.puzzle.PuzzleStoreJpa;
import com.clueride.infrastructure.tether.TetherService;
import com.clueride.infrastructure.tether.TetherServiceImpl;

/**
 * Guice Bindings for the Domain module.
 */
public class DomainGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new DomainGuiceProviderModule());

        /* Bindings for Application use. */
        bind(BadgeService.class).to(BadgeServiceImpl.class);
        bind(BadgeEventService.class).to(BadgeEventServiceImpl.class);
        bind(BadgeEventStore.class).to(BadgeEventStoreJpa.class);
        bind(BadgeStore.class).to(BadgeStoreJpa.class);
        bind(BadgeTypeService.class).to(BadgeTypeServiceMappedImpl.class);
        bind(ImageService.class).to(ImageServiceImpl.class);
        bind(ImageStore.class).to(ImageStoreJpa.class);
        bind(LatLonStore.class).to(LatLonStoreJpa.class);
        bind(LatLonService.class).to(LatLonServiceImpl.class);
        bind(LocationTypeService.class).to(LocationTypeServiceImpl.class);
        bind(LocationTypeStore.class).to(LocationTypeStoreJpa.class);
        bind(MapLayerService.class).to(MapLayerServiceImpl.class);
        bind(MapLayerStore.class).to(MapLayerStoreJpa.class);
        bind(MemberService.class).to(MemberServiceImpl.class);
        bind(MemberStore.class).to(MemberStoreJpa.class);
        bind(PrincipalService.class).to(PrincipalServiceImpl.class);
        bind(PuzzleService.class).to(PuzzleServiceImpl.class);
        bind(PuzzleStore.class).to(PuzzleStoreJpa.class);
        bind(ScoredLocationService.class).to(ScoredLocationServiceImpl.class);
        bind(SessionPrincipal.class).to(SessionPrincipalImpl.class);
        bind(TetherService.class).to(TetherServiceImpl.class);
    }

}
