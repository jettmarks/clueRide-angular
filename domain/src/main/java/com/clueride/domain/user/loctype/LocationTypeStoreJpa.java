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
 * Created by jett on 10/4/17.
 */
package com.clueride.domain.user.loctype;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.google.inject.Inject;

import com.clueride.infrastructure.db.ClueRide;

/**
 * JPA implementation of the LocationTypeStore (DAO).
 */
public class LocationTypeStoreJpa implements LocationTypeStore {
    private final EntityManager entityManager;

    @Inject
    public LocationTypeStoreJpa(
            @ClueRide EntityManager entityManager
    ) {
        this.entityManager = entityManager;
    }

    @Override
    public List<LocationType> getLocationTypes() {
        List<LocationType> locationTypes = new ArrayList<>();
        entityManager.getTransaction().begin();
        List<LocationType.Builder> locTypeBuilders =
                entityManager.createQuery(
                        "SELECT lt from location_type lt"
                ).getResultList();

        for (LocationType.Builder locTypeBuilder : locTypeBuilders) {
            locationTypes.add(locTypeBuilder.build());
        }
        entityManager.getTransaction().commit();
        return locationTypes;
    }

    @Override
    public LocationType getLocationTypeByName(String locationTypeName) {
        entityManager.getTransaction().begin();

        List<LocationType.Builder> builders = entityManager.createQuery(
                "SELECT lt FROM location_type lt WHERE lt.name = :locTypeName"
        ).setParameter("locTypeName", locationTypeName)
                .getResultList();
        entityManager.getTransaction().commit();

        if (builders.size() == 1) {
            return builders.get(0).build();
        } else {
            throw new RuntimeException("Multiple records found for location type name: " + locationTypeName );
        }
    }

    @Override
    public LocationType getLocationTypeById(Integer locationTypeId) {
        entityManager.getTransaction().begin();
        LocationType locationType = entityManager.find(
                LocationType.Builder.class,
                locationTypeId
        ).build();
        entityManager.getTransaction().commit();
        return locationType;
    }

}
