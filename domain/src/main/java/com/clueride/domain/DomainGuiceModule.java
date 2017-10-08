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

import com.clueride.domain.account.member.MemberStore;
import com.clueride.domain.account.member.MemberStoreJpa;
import com.clueride.domain.user.image.ImageService;
import com.clueride.domain.user.image.ImageServiceImpl;
import com.clueride.domain.user.image.ImageStore;
import com.clueride.domain.user.image.ImageStoreJpa;
import com.clueride.domain.user.latlon.LatLonService;
import com.clueride.domain.user.latlon.LatLonServiceImpl;
import com.clueride.domain.user.latlon.LatLonStore;
import com.clueride.domain.user.latlon.LatLonStoreJpa;
import com.clueride.domain.user.loctype.LocationTypeService;
import com.clueride.domain.user.loctype.LocationTypeServiceImpl;
import com.clueride.domain.user.loctype.LocationTypeStore;
import com.clueride.domain.user.loctype.LocationTypeStoreJpa;
import com.clueride.domain.user.place.ScoredLocationService;
import com.clueride.domain.user.place.ScoredLocationServiceImpl;

/**
 * Guice Bindings for the Domain module.
 */
public class DomainGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new DomainGuiceProviderModule());

        /* Bindings for Application use. */
        bind(ImageService.class).to(ImageServiceImpl.class);
        bind(ImageStore.class).to(ImageStoreJpa.class);
        bind(LatLonStore.class).to(LatLonStoreJpa.class);
        bind(LatLonService.class).to(LatLonServiceImpl.class);
        bind(LocationTypeService.class).to(LocationTypeServiceImpl.class);
        bind(LocationTypeStore.class).to(LocationTypeStoreJpa.class);
        bind(MemberStore.class).to(MemberStoreJpa.class);
        bind(ScoredLocationService.class).to(ScoredLocationServiceImpl.class);
    }

}
