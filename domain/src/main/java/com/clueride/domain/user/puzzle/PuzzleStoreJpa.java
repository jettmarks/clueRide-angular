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
 * Created by jett on 10/19/17.
 */
package com.clueride.domain.user.puzzle;

import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.clueride.domain.user.location.Location;

/**
 * Implementation of the Puzzle Store.
 */
public class PuzzleStoreJpa implements PuzzleStore {

    private final EntityManager entityManager;
    @Inject
    public PuzzleStoreJpa(
            @Nonnull EntityManager entityManager
    ) {
        this.entityManager = entityManager;
    }

    @Override
    public Integer addNew(Puzzle.Builder puzzleBuilder) {
        entityManager.getTransaction().begin();
        entityManager.persist(puzzleBuilder);
        entityManager.getTransaction().commit();
        return puzzleBuilder.getId();
    }

    @Override
    public Puzzle.Builder getPuzzleById(Integer id) {
        Puzzle.Builder puzzleBuilder;
        entityManager.getTransaction().begin();
        puzzleBuilder = entityManager.find(Puzzle.Builder.class, id);
        entityManager.getTransaction().commit();
        return puzzleBuilder;
    }

    @Override
    public List<Puzzle.Builder> getPuzzlesForLocation(Location.Builder locationBuilder) {
        List<Puzzle.Builder> puzzleBuilders;
        entityManager.getTransaction().begin();
        puzzleBuilders = entityManager.createQuery(
                        "SELECT p FROM PuzzleBuilder p where p.locationBuilder = :locationBuilder"
                ).setParameter("locationBuilder", locationBuilder)
                .getResultList();
        entityManager.getTransaction().commit();
        return puzzleBuilders;
    }

}
