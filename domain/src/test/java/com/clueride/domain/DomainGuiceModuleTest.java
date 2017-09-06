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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import com.clueride.domain.user.Location;
import com.clueride.domain.user.LocationType;
import com.clueride.infrastructure.JpaUtil;

/**
 * Guice Bindings for the Testing of the Domain Module.
 */
public class DomainGuiceModuleTest extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    private EntityManager getEntityManager() {
        return JpaUtil.getEntityManagerFactory().createEntityManager();
    }

    @Provides
    private Location.Builder getLocationBuilder() {
        String expectedName = "Test Location";
        String expectedDescription = "Here's a nice spot to spread out the blanket or toss the frisbee.";
        LocationType expectedLocationType = LocationType.PICNIC;
        Integer expectedNodeId = 123;

        List<Integer> expectedClues = new ArrayList<>();
        expectedClues.add(1);
        expectedClues.add(2);
        expectedClues.add(3);
        expectedClues.add(4);
        expectedClues.add(5);
        expectedClues.add(6);
        expectedClues.add(7);

        List<URL> expectedImageUrls = new ArrayList<>();
        try {
            expectedImageUrls.add(new URL("https://clueride.com/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return Location.Builder.builder()
                .withName(expectedName)
                .withDescription(expectedDescription)
                .withLocationType(expectedLocationType)
                .withNodeId(expectedNodeId)
                .withClueIds(expectedClues)
                .withImageUrls(expectedImageUrls);
    }
}
