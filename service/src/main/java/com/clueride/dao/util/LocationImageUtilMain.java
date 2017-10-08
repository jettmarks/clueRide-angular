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
 * Created by jett on 10/8/17.
 */
package com.clueride.dao.util;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

import com.clueride.domain.DomainGuiceModule;
import com.clueride.domain.user.image.Image;
import com.clueride.domain.user.image.ImageStore;
import com.clueride.domain.user.location.Location;
import com.clueride.domain.user.location.LocationStore;
import com.clueride.infrastructure.Jpa;
import com.clueride.infrastructure.Json;
import com.clueride.infrastructure.ServiceGuiceModule;

/**
 * Helps move Images into database.
 */
public class LocationImageUtilMain {
    private static LocationStore locationStoreJson;
    private static LocationStore locationStoreJpa;
    private static ImageStore imageStoreJpa;
    private static Collection<Location> locations;

    public static void main(String[] args) {
        instantiateStores();
        locations = locationStoreJson.getLocations();
        System.out.println("Found  " + locations.size() + " locations");
        for (Location location : locations) {
            Location.Builder locationBuilder = Location.Builder.from(location);
            URL firstUrl = location.getImageUrls().get(0);
            int newImageId = persistImageUrl(firstUrl.toString());
            locationBuilder.withFeaturedImageId(newImageId);
        }
    }

    private static int persistImageUrl(String urlString) {
        Image.Builder imageBuilder = Image.Builder.builder().withUrlString(urlString);
        return imageStoreJpa.addNew(imageBuilder);
    }

    /**
     * Used to check the uniqueness of the inbound data.
     * @param locations
     * @return
     */
    private static Map<String,Integer> buildImageMap(Collection<Location> locations) {
        Map<String,Integer> urlStringToLocationId = new HashMap<>();
        int totalCount = 0;
        for (Location location : locations) {
            int locationId = location.getId();
            int imageCountForThisLocation = location.getImageUrls().size();
            totalCount += imageCountForThisLocation;
            System.out.println("Location ID " + locationId + " has " + imageCountForThisLocation + " images");
            for (URL imageUrl : location.getImageUrls()) {
              urlStringToLocationId.put(imageUrl.toString(), locationId);
              System.out.println(imageUrl.toString() + ": " + locationId);
            }
        }
        System.out.println("Images Total: " + totalCount);
        System.out.println("Unique Images: " + urlStringToLocationId.values().size());
        return urlStringToLocationId;
    }

    private static void instantiateStores() {
        Injector injector = Guice.createInjector(
                new DomainGuiceModule(),
                new ServiceGuiceModule()
        );

        locationStoreJson = injector.getInstance(
                Key.get(
                        LocationStore.class,
                        Json.class
                )
        );

        locationStoreJpa = injector.getInstance(
                Key.get(
                        LocationStore.class,
                        Jpa.class
                )
        );

        imageStoreJpa = injector.getInstance(
                ImageStore.class
        );

        if (
                locationStoreJpa == null ||
                locationStoreJson == null ||
                imageStoreJpa == null
        ) {
            throw new RuntimeException("Unable to instantiate services");
        }
    }
}
