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
import java.util.Collections;

import javax.persistence.EntityManager;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import com.clueride.domain.user.image.Image;
import com.clueride.domain.user.location.Location;
import com.clueride.domain.user.loctype.LocationType;
import com.clueride.infrastructure.JpaUtil;
import static java.util.Arrays.asList;

/**
 * Guice Providers that may be pulled into Tests of other modules.
 */
public class DomainGuiceProviderModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    private EntityManager getEntityManager() {
        return JpaUtil.getEntityManagerFactory().createEntityManager();
    }

    @Provides
    private Location provideLocation(
            LocationType locationType
    ) {
        return getLocationBuilder(locationType).build();
    }

    @Provides
    private Location.Builder getLocationBuilder(
            LocationType locationType
    ) {
        Location.Builder locationBuilder = Location.Builder.builder()
                .withId(1)
                .withNodeId(100)
                .withName("Test Location")
                .withClueIds(asList(1, 2, 3))
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

}
