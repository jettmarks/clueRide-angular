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
package com.clueride.dao.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import org.apache.log4j.Logger;

import com.clueride.config.GeoToolsGuiceModule;
import com.clueride.dao.ClueStore;
import com.clueride.domain.DomainGuiceModule;
import com.clueride.domain.user.Clue;
import com.clueride.domain.user.answer.Answer;
import com.clueride.domain.user.location.Location;
import com.clueride.domain.user.location.LocationStore;
import com.clueride.domain.user.puzzle.Puzzle;
import com.clueride.domain.user.puzzle.PuzzleStore;
import com.clueride.infrastructure.Jpa;
import com.clueride.infrastructure.Json;
import com.clueride.infrastructure.ServiceGuiceModule;
import static org.apache.log4j.Level.WARN;

/**
 * Moves records from one (JSON) Store to another (JPA).
 */
public class PuzzleUtilMain {
    private static ClueStore clueStore;
    private static EntityManager entityManager;
    private static Map<Integer,Location.Builder> locationPerClue;
    private static LocationStore locationStoreJpa;
    private static LocationStore locationStoreJson;
    private static PuzzleStore puzzleStore;

    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(WARN);
        instantiateStores();
        try {
            locationPerClue = mapClueToLocation();
            List<Clue> clues = clueStore.get();
            System.out.println("Found " + clues.size() + " clues to be turned into Puzzles");
            for (Clue clue : clues) {
                Puzzle.Builder puzzleBuilder = getPuzzleFromClue(clue);
                if (puzzleBuilder != null) {
                    Integer locationId = puzzleBuilder.getLocationBuilder().getId();
                    Location.Builder locationBuilder = locationStoreJpa.getLocationBuilderById(locationId);
                    puzzleBuilder.withId(null);     // Clearing this avoids exception against "detached" instance
                    locationBuilder.addPuzzleBuilder(puzzleBuilder);
                    puzzleBuilder.withLocationBuilder(locationBuilder);
                    puzzleStore.addNew(puzzleBuilder);
                }
            }
        } finally {
            entityManager.close();
        }
        System.exit(0);
    }

    private static Puzzle.Builder getPuzzleFromClue(Clue clue) {
        Integer clueId = clue.getId();
        System.out.printf("%d: %s", clueId, clue.getName());

        Location.Builder locationBuilder = locationPerClue.get(clueId);
        if (locationBuilder == null) {
            System.out.printf(" -- not associated with a Location\n");
            return null;
        }

        System.out.printf(" has location\n");
        List<Answer> answers = clue.getAnswers();
        Puzzle.Builder puzzleBuilder = Puzzle.Builder.builder()
                .withId(clue.getId())
                .withLocationBuilder(locationBuilder)
                .withName(clue.getName())
                .withQuestion(clue.getQuestion())
                .withCorrectAnswer(clue.getCorrectAnswer())
                .withPoints(clue.getPoints())
                ;
        for (Answer answer : answers) {
            answer.withPuzzleBuilder(puzzleBuilder);
        }
        puzzleBuilder.withAnswers(answers);

        System.out.println(puzzleBuilder);
        return puzzleBuilder;
    }

    private static Map<Integer,Location.Builder> mapClueToLocation() {
        Map<Integer,Location.Builder> locationPerClue = new HashMap<>();
        for (Location.Builder locationBuilder : locationStoreJson.getLocationBuilders())
        {
            List<Integer> clueIds = locationBuilder.getClueIds();
            for (Integer clueId : clueIds) {
                locationPerClue.put(clueId, locationBuilder);
            }
        }
        return locationPerClue;
    }

    private static void instantiateStores() {
        Injector injector = Guice.createInjector(
                new DomainGuiceModule(),
                new ServiceGuiceModule(),
                new GeoToolsGuiceModule()
        );

        entityManager = injector.getInstance(EntityManager.class);
        puzzleStore = injector.getInstance(
                PuzzleStore.class
        );
        locationStoreJpa = injector.getInstance(
                Key.get(
                        LocationStore.class,
                        Jpa.class
                )
        );
        locationStoreJson = injector.getInstance(
                Key.get(
                        LocationStore.class,
                        Json.class
                )
        );
        clueStore = injector.getInstance(ClueStore.class);
    }

}
