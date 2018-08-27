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
 * Created by jett on 8/12/17.
 */
package com.clueride.infrastructure;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Allows obtaining the configured factory for the Hibernate Entity Managers.
 */
public class JpaUtil {
    private static final String CLUERIDE_PERSISTENCE_UNIT_NAME = "ClueRidePersistenceUnit";
    private static final String WORDPRESS_PERSISTENCE_UNIT_NAME = "WordPressPersistenceUnit";
    private static EntityManagerFactory clueRideFactory;
    private static EntityManagerFactory wordPressFactory;

    public static EntityManagerFactory getClueRideEntityManagerFactory() {
        if (clueRideFactory == null) {
            clueRideFactory = Persistence.createEntityManagerFactory(CLUERIDE_PERSISTENCE_UNIT_NAME);
        }
        return clueRideFactory;
    }

    public static EntityManagerFactory getWordPressEntityManagerFactory() {
        if (wordPressFactory == null) {
            wordPressFactory = Persistence.createEntityManagerFactory(WORDPRESS_PERSISTENCE_UNIT_NAME);
        }
        return wordPressFactory;
    }

    public static void shutdown() {
        if (clueRideFactory != null) {
            clueRideFactory.close();
        }
        if (wordPressFactory != null) {
            wordPressFactory.close();
        }
    }

}
