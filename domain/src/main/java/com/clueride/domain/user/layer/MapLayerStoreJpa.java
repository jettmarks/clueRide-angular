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
 * Created by jett on 12/7/17.
 */
package com.clueride.domain.user.layer;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.clueride.infrastructure.db.ClueRide;

/**
 * JPA-based implementation of Map Layer persistence.
 */
public class MapLayerStoreJpa implements MapLayerStore {
    private final EntityManager entityManager;

    @Inject
    public MapLayerStoreJpa(
            @ClueRide EntityManager entityManager
    ) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Layer> getLayers() {
        entityManager.getTransaction().begin();
        List<Layer> layerList = entityManager.createQuery(
               "SELECT l FROM layer l"
        ).getResultList();
        entityManager.getTransaction().commit();
        return layerList;
    }
}
