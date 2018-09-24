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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import com.clueride.domain.CourseWithGeo;
import com.clueride.domain.GamePath;
import com.clueride.domain.account.member.Member;
import com.clueride.domain.common.Builder;
import com.clueride.domain.course.Course;
import com.clueride.domain.course.CourseType;
import com.clueride.domain.invite.Invitation;
import com.clueride.domain.outing.Outing;
import com.clueride.domain.user.Clue;
import com.clueride.domain.user.location.Location;
import com.clueride.domain.user.path.Path;
import com.clueride.rest.dto.ClueRideState;
import com.clueride.service.IdProvider;
import com.clueride.service.MemoryBasedClueIdProvider;
import com.clueride.service.MemoryBasedInvitationIdProvider;
import com.clueride.service.MemoryBasedLocationIdProvider;
import com.clueride.service.MemoryBasedPathIdProvider;

/**
 * Utility for reading and writing JSON files and strings without the GeoJson
 * features.
 * TODO: CA-311: Numerous refactorings:
 * - IDE's checks turned off for ignoring return value
 * - Yellow blocks where 'directory' isn't being checked for null
 * - Repeated code
 * - Existing CA-266 ticket
 */
public class PojoJsonUtil implements PojoJsonService {
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
     * TODO: CA-266 Refactoring to make this more friendly to the various types of file/object organization.
     * @param id - Integer representing the object being persisted.
     * @param storeType - Provides persistence details particular to the Type of object.
     * @return File instance ready to read/write.
     * @throws IOException
     */
    public static File getFile(Integer id, JsonStoreType storeType) throws IOException {
        File candidateFile = (storeType == JsonStoreType.INVITATION)
                ? getFileForId(id, storeType)
                : getFileWithDirectoryForId(id, storeType);
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

    /**
     * Creates a File spec with the pattern "<type>/</type>-<id>/<type>-<id>.json" where the 'id' field is 5 digits.
     * This is suitable for most objects being persisted, but for invitations, we don't require the directory
     * of the same name.
     * @param id - Unique identifier for the particular record.
     * @param storeType - Enumeration serving as index into the prefix map.
     * @return File instance with a name expected to be used, including directory as part of path.
     */
    private static File getFileWithDirectoryForId(Integer id, JsonStoreType storeType) {
        String specificName = JsonPrefixMap.toString(storeType) + "-" + String.format("%05d", id);
        return new File(JsonStoreLocation.toString(storeType)
                + File.separator
                + specificName
                + File.separator
                + specificName
                + ".json");
    }

    /**
     * Creates a File spec with the pattern "<type>/<type>-<id>.json" where the 'id' field is 5 digits.
     * This is suitable for objects which all reside in a single directory for the given storeType.
     * @param id - Integer uniquely identifying the object of the given type.
     * @param storeType - Type of object being persisted.
     * @return File which may or may not exist; this only specifies the path and name of the file.
     */
    private static File getFileForId(Integer id, JsonStoreType storeType) {
        String specificName = JsonPrefixMap.toString(storeType) + "-" + String.format("%05d", id);
        return new File(JsonStoreLocation.toString(storeType)
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

    public List<Location> loadLocations() {
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
        File locationFile = getFileWithDirectoryForId(id, JsonStoreType.LOCATION);
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

    public List<Path> loadPaths() {
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

    @Override
    public List<Location.Builder> loadLocationBuilders() {
        List<Location.Builder> locations = new ArrayList<>();
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
                locations.add(loadLocationBuilder(locationFile));
            }
        }
        return locations;
    }

    private Location.Builder loadLocationBuilder(File locationFile) {
        Location.Builder locationBuilder;
        try {
            synchronized (objectMapper) {
                locationBuilder = objectMapper.readValue(locationFile, Location.Builder.class);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unexpected I/O error", e);
        }
        return locationBuilder;
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
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                CourseWithGeo.Builder builder = objectMapper.readValue(file, CourseWithGeo.Builder.class);
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
            throw new RuntimeException("Unexpected I/O error while reading Invitation: ", e);
        }
        return invitation;
    }

    public static <T> T loadJsonObject(File file, com.clueride.domain.common.Builder<T> builder) {
        T jsonObject = null;
        try {
            Builder<T> populatedBuilder;
            populatedBuilder = objectMapper.readValue(file, builder.getClass());
            jsonObject = populatedBuilder.build();
//            IdProvider.registerId(jsonObject.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static <T> List<T> loadJsonObjects(final JsonStoreType storeType) {
        List<T> jsonObjects = new ArrayList<>();
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
                switch (storeType) {
                    case INVITATION:
                        jsonObjects.add((T) loadJsonObject(file, Invitation.Builder.builder()));
                        break;
                    case COURSE_TYPE:
                        jsonObjects.add((T) loadJsonObject(file, CourseType.Builder.builder()));
                        break;
                    case OUTING:
                        jsonObjects.add((T) loadJsonObject(file, Outing.Builder.builder()));
                        break;
                    case MEMBER:
                        jsonObjects.add((T) loadJsonObject(file, Member.Builder.builder()));
                        break;
                    default:
                        LOGGER.error("Unrecognized Store Type: " + storeType);
                        break;
                }
            }
        }
        return jsonObjects;
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

    public static List<Member> loadMembers() {
        return loadJsonObjects(JsonStoreType.MEMBER);
    }

    public static List<Outing> loadOutings() {
        return loadJsonObjects(JsonStoreType.OUTING);
    }

    public static List<CourseType> loadCourseTypes() {
        return loadJsonObjects(JsonStoreType.COURSE_TYPE);
    }
}
