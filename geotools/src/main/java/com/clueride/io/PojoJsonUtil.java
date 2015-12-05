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

import com.clueride.domain.user.Location;
import com.clueride.service.MemoryBasedLocationIdProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility for reading and writing JSON files and strings without the GeoJson
 * features.
 */
public class PojoJsonUtil {
    private static final Logger LOGGER = Logger.getLogger(PojoJsonUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static MemoryBasedLocationIdProvider idProvider = new MemoryBasedLocationIdProvider();

    /**
     * From the values in the location instance, create a File appropriate for storing
     * the location.
     * The file is created if it doesn't exist.
     * @param location to be persisted.
     * @return File which can be used to store the location.
     */
    public static File getFileForLocation(Location location) throws IOException {
        Integer locationId = location.getId();
        File candidateFile = getFileForLocationId(locationId);
        if (!candidateFile.canWrite()) {
            // Possible that the directory doesn't exist either
            if (!candidateFile.getParentFile().canWrite()) {
                candidateFile.getParentFile().mkdir();
            }
            candidateFile.createNewFile();
        }
        return candidateFile;
    }

    private static File getFileForLocationId(Integer locationId) {
        String locationSpecificName = "loc-" + String.format("%05d", locationId);
        StringBuffer newFileNameBuffer = new StringBuffer()
                .append(JsonStoreLocation.toString(JsonStoreType.LOCATION))
                .append(File.separator)
                .append(locationSpecificName)
                .append(File.separator)
                .append(locationSpecificName)
                .append(".json");
        return new File(newFileNameBuffer.toString());
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
        Location location = null;
        try {
            synchronized (objectMapper) {
                Location.Builder locationBuilder = objectMapper.readValue(locationFile, Location.Builder.class);
                location = locationBuilder.build();
                idProvider.registerId(location.getId());
            }
        } catch (IOException e) {
            throw new RuntimeException("Unexpected I/O error", e);
        }
        return location;
    }

    public static Location loadLocationId(int id) throws FileNotFoundException {
        File locationFile = getFileForLocationId(id);
        if (!locationFile.exists()) {
            throw new FileNotFoundException(locationFile.getName());
        }
        return loadLocation(locationFile);
    }

    public static String generateLocation(Location location) throws JsonProcessingException {
        return objectMapper.writeValueAsString(location);
    }
}
