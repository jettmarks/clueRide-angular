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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import com.clueride.domain.account.member.MemberStore;
import com.clueride.domain.account.member.MemberStoreJpa;
import com.clueride.domain.user.latlon.LatLonService;
import com.clueride.domain.user.latlon.LatLonServiceImpl;
import com.clueride.domain.user.latlon.LatLonStore;
import com.clueride.domain.user.latlon.LatLonStoreJpa;
import com.clueride.domain.user.location.Location;
import com.clueride.domain.user.location.LocationType;
import com.clueride.infrastructure.JpaUtil;

/**
 * Guice Bindings for the Domain module.
 */
public class DomainGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(LatLonStore.class).to(LatLonStoreJpa.class);
        bind(LatLonService.class).to(LatLonServiceImpl.class);
        bind(MemberStore.class).to(MemberStoreJpa.class);
    }

    @Provides
    private EntityManager getEntityManager() {
        return JpaUtil.getEntityManagerFactory().createEntityManager();
    }

    // TODO: CA-307: Duplicated provider
    @Provides
    private Location getLocation() throws Exception {
        List<Integer> clues = new ArrayList<>();
        List<URL> imageUrls = new ArrayList<>();
        String name = "Test Location";
        String description = "Here's a nice spot to spread out the blanket or toss the frisbee.";
        LocationType locationType = LocationType.PICNIC;
        Integer nodeId = 123;

        clues.add(1);
        clues.add(2);
        clues.add(3);
        clues.add(4);
        clues.add(5);
        clues.add(6);
        clues.add(7);
        imageUrls.add(new URL("https://clueride.com/"));
        Location.Builder builder = Location.Builder.builder()
                .withName(name)
                .withDescription(description)
                .withLocationType(locationType)
                .withNodeId(nodeId)
                .withClueIds(clues)
                .withImageUrls(imageUrls);
        return builder.build();
    }
}
