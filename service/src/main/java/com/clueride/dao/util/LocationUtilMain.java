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
 * Created by jett on 9/10/17.
 */
package com.clueride.dao.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

import com.clueride.domain.user.location.Location;
import com.clueride.domain.user.location.LocationStore;
import com.clueride.infrastructure.Jpa;
import com.clueride.infrastructure.Json;
import com.clueride.infrastructure.ServiceGuiceModule;
import static java.lang.System.exit;

/**
 * Moves records from one (JSON) Store to another (JPA).
 */
public class LocationUtilMain {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ServiceGuiceModule());

        LocationStore locationStoreJson = injector.getInstance(
                Key.get(
                        LocationStore.class,
                        Json.class
                )
        );

        LocationStore locationStoreJpa = injector.getInstance(
                Key.get(
                        LocationStore.class,
                        Jpa.class
                )
        );

        List<Location> locations = new ArrayList<>();
        locations.addAll(locationStoreJson.getLocations());

        for (Location location : locations) {
            try {
                locationStoreJpa.addNew(location);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        exit(0);
    }
}
