/*
 * Copyright 2015 Jett Marks
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
 * Created by jett on 12/1/15.
 */
package com.clueride.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import com.clueride.domain.Course;
import com.clueride.domain.GameCourse;
import com.clueride.domain.GamePath;
import com.clueride.domain.Invitation;
import com.clueride.domain.user.Clue;
import com.clueride.domain.user.Location;
import com.clueride.domain.user.Path;
import com.clueride.rest.dto.ClueRideState;
import com.clueride.service.IdProvider;
import com.clueride.service.MemoryBasedClueIdProvider;
import com.clueride.service.MemoryBasedInvitationIdProvider;
import com.clueride.service.MemoryBasedLocationIdProvider;
import com.clueride.service.MemoryBasedPathIdProvider;

/**
 * Utility for reading and writing JSON files and strings without the GeoJson
 * features.
 */
public class PojoJsonUtil {
    private static final Logger LOGGER = Logger.getLogger(PojoJsonUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static IdProvider locationIdProvider = new MemoryBasedLocationIdProvider();
    private static IdProvider pathIdProvider = new MemoryBasedPathIdProvider();
    private static IdProvider clueIdProvider = new MemoryBasedClueIdProvider();
    private static IdProvider invitationIdProvider = new MemoryBasedInvitationIdProvider();

    /**
     * From the values in the location instance, create a File appropriate for storing
     * the location.
     * The file is created if it doesn't exist.
     * @param location to be persisted.
     * @return File which can be used to store the location.
     */
    public static File getFileForLocation(Location location) throws IOException {
        Integer locationId = location.getId();
        return getFile(locationId, JsonStoreType.LOCATION);
    }

    /**
     * Given an object ID to write to disk, determine the file name where that object
     * is expected to reside.
     * A little more genericized version.
     * @param id - Integer representing the object being persisted.
     * @param storeType - Provides persistence details particular to the Type of object.
     * @return File instance ready to read/write.
     * @throws IOException
     */
    public static File getFile(Integer id, JsonStoreType storeType) throws IOException {
        File candidateFile = getFileForId(id, storeType);
        if (!candidateFile.canWrite()) {
            // Possible that the directory doesn't exist either
            if (!candidateFile.getParentFile().canWrite()) {
                //noinspection ResultOfMethodCallIgnored
                candidateFile.getParentFile().mkdir();
            }
            //noinspection ResultOfMethodCallIgnored
            candidateFile.createNewFile();
        }
        return candidateFile;
    }

    private static File getFileForId(Integer id, JsonStoreType storeType) {
        String specificName = JsonPrefixMap.toString(storeType) + "-" + String.format("%05d", id);
        return new File(JsonStoreLocation.toString(storeType)
                + File.separator
                + specificName
                + File.separator
                + specificName
                + ".json");
    }

    public static File getFileForClueId(Integer id) {
        JsonStoreType storeType = JsonStoreType.CLUE;
        String specificName = JsonPrefixMap.toString(storeType) + "-" + String.format("%06d", id);
        return new File(JsonStoreLocation.toString(storeType)
                + File.separator
                + specificName
                + ".json");
    }

    public static List<Location> loadLocations() {
        List<Location> locations = new ArrayList<>();
        File locationDirectory = new File(JsonStoreLocation.toString(JsonStoreType.LOCATION));
        for (File child : locationDirectory.listFiles(new FilenameFilter(){
            @Override
            public boolean accept(File file, String s) {
                LOGGER.debug("Checking for Location at "+s);
                return s.contains("loc-");
            }
        })) {
            LOGGER.debug("Checking for JSON Location in the directory " + child.getName());
            for (File locationFile : child.listFiles(new FilenameFilter(){
                @Override
                public boolean accept(File file2, String s) {
                    return s.startsWith("loc-") && s.endsWith(".json");
                }
            })) {
                locations.add(loadLocation(locationFile));
            }
        }
        return locations;
    }

    public static Location loadLocation(File locationFile) {
        Location location;
        try {
            synchronized (objectMapper) {
                Location.Builder locationBuilder = objectMapper.readValue(locationFile, Location.Builder.class);
                location = locationBuilder.build();
                locationIdProvider.registerId(location.getId());
            }
        } catch (IOException e) {
            throw new RuntimeException("Unexpected I/O error", e);
        }
        return location;
    }

    public static Location loadLocationId(int id) throws FileNotFoundException {
        File locationFile = getFileForId(id, JsonStoreType.LOCATION );
        if (!locationFile.exists()) {
            throw new FileNotFoundException(locationFile.getName());
        }
        return loadLocation(locationFile);
    }

    public static String generateLocation(Location location) throws JsonProcessingException {
        return objectMapper.writeValueAsString(location);
    }

    /**
     * Given a ClueRideState, return the JSON representation for serialization across the wire.
     * @param clueRideState - ClueRideState instance to be turned into JSON.
     * @return JSON String representing the state.
     * @throws JsonProcessingException
     */
    public static String generateClueRideState(ClueRideState clueRideState) throws JsonProcessingException {
        return objectMapper.writeValueAsString(clueRideState);
    }

    public static List<Path> loadPaths() {
        final JsonStoreType storeType = JsonStoreType.PATH;
        List<Path> paths = new ArrayList<>();
        File directory = new File(JsonStoreLocation.toString(storeType));
        if (!directory.canWrite()) {
            if (!directory.mkdir()) {
                throw new RuntimeException("Unable to create directory " + directory.getName());
            }
        } else {
            for (File child : directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    LOGGER.debug("Checking for file at " + s);
                    return s.contains(JsonPrefixMap.toString(storeType));
                }
            })) {
                LOGGER.debug("Checking for JSON file in the directory " + child.getName());
                for (File file : child.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File file2, String s) {
                        return s.startsWith(JsonPrefixMap.toString(storeType)) && s.endsWith(".json");
                    }
                })) {
                    paths.add(loadPath(file));
                }
            }
        }
        return paths;
    }

    private static Path loadPath(File file) {
        Path path;
        try {
            synchronized (objectMapper) {
                GamePath.Builder builder = objectMapper.readValue(file, GamePath.Builder.class);
                path = builder.build();
                pathIdProvider.registerId(path.getId());
            }
        } catch (IOException e) {
            throw new RuntimeException("Unexpected I/O error", e);
        }
        return path;
    }

    public static List<Course> loadCourses() {
        final JsonStoreType storeType = JsonStoreType.COURSE;
        List<Course> courses = new ArrayList<>();
        File directory = new File(JsonStoreLocation.toString(storeType));
        if (!directory.canWrite()) {
            if (!directory.mkdir()) {
                throw new RuntimeException("Unable to create directory " + directory.getName());
            }
        } else {
            for (File child : directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    LOGGER.debug("Checking for file at " + s);
                    return s.contains(JsonPrefixMap.toString(storeType));
                }
            })) {
                LOGGER.debug("Checking for JSON file in the directory " + child.getName());
                for (File file : child.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File file2, String s) {
                        return s.startsWith(JsonPrefixMap.toString(storeType)) && s.endsWith(".json");
                    }
                })) {
                    courses.add(loadCourse(file));
                }
            }
        }
        return courses;
    }

    private static Course loadCourse(File file) {
        Course course;
        try {
            synchronized (objectMapper) {
                GameCourse.Builder builder = objectMapper.readValue(file, GameCourse.Builder.class);
                course = builder.build();
                pathIdProvider.registerId(course.getId());
            }
        } catch (IOException e) {
            throw new RuntimeException("Unexpected I/O error while reading Course:", e);
        }
        return course;
    }

    public static Clue loadClue(Integer clueId) {
        final JsonStoreType storeType = JsonStoreType.CLUE;
        File directory = new File(JsonStoreLocation.toString(storeType));
        if (!directory.canWrite()) {
            if (!directory.mkdir()) {
                throw new RuntimeException("Unable to create directory " + directory.getName());
            }
        } else {
            for (File file : directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.startsWith(JsonPrefixMap.toString(storeType))
                            && s.endsWith(".json");
                }
            })) {
                LOGGER.debug("Checking for clue " + clueId + " in file " + file.getName());
                Clue clue = loadClue(file);
                if (clue.getId().equals(clueId)) {
                    return clue;
                }
            }
        }
            return null;
    }

    private static Clue loadClue(File file) {
        Clue clue;
        try {
            synchronized (objectMapper) {
                Clue.Builder builder = objectMapper.readValue(file, Clue.Builder.class);
                clue = builder.build();
                clueIdProvider.registerId(clue.getId());
            }
        } catch (IOException e) {
            throw new RuntimeException("Unexpected I/O error while reading Clue: ", e);
        }
        return clue;
    }

    public static Collection<Clue> loadClues() {
        Collection<Clue> clues = new ArrayList<>();
        final JsonStoreType storeType = JsonStoreType.CLUE;
        File directory = new File(JsonStoreLocation.toString(storeType));
        if (!directory.canWrite()) {
            if (!directory.mkdir()) {
                throw new RuntimeException("Unable to create directory " + directory.getName());
            }
        } else {
            for (File file : directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.startsWith(JsonPrefixMap.toString(storeType))
                            && s.endsWith(".json");
                }
            })) {
                clues.add(loadClue(file));
            }
        }
        return clues;
    }

    private static Invitation loadInvitation(File file) {
        Invitation invitation;
        try {
            synchronized (objectMapper) {
                Invitation.Builder builder = objectMapper.readValue(file, Invitation.Builder.class);
                invitation = builder.build();
                invitationIdProvider.registerId(invitation.getId());
            }
        } catch (IOException e) {
            throw new RuntimeException("Unexpected I/O error while reading Clue: ", e);
        }
        return invitation;
    }

    public static List<Invitation> loadInvitations() {
        List<Invitation> invitations = new ArrayList<>();
        final JsonStoreType storeType = JsonStoreType.INVITATION;
        File directory = new File(JsonStoreLocation.toString(storeType));
        if (!directory.canWrite()) {
            if (!directory.mkdir()) {
                throw new RuntimeException("Unable to create directory " + directory.getName());
            }
        } else {
            for (File file : directory.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File file, String s) {
                    return s.startsWith(JsonPrefixMap.toString(storeType))
                            && s.endsWith(".json");
                }
            })) {
                invitations.add(loadInvitation(file));
            }
        }
        return invitations;
    }
}
