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
 * Created by jett on 12/3/15.
 */
package com.clueride.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.io.BaseEncoding;
import com.vividsolutions.jts.geom.Point;
import org.apache.log4j.Logger;

import com.clueride.dao.CourseStore;
import com.clueride.dao.ImageStore;
import com.clueride.dao.PathStore;
import com.clueride.domain.course.Course;
import com.clueride.domain.user.image.Image;
import com.clueride.domain.user.image.ImageService;
import com.clueride.domain.user.latlon.LatLon;
import com.clueride.domain.user.latlon.LatLonService;
import com.clueride.domain.user.location.Location;
import com.clueride.domain.user.location.LocationStore;
import com.clueride.domain.user.loctype.LocationType;
import com.clueride.domain.user.loctype.LocationTypeService;
import com.clueride.domain.user.path.Path;
import com.clueride.domain.user.place.ScoredLocationService;
import com.clueride.infrastructure.Jpa;
import com.clueride.io.PojoJsonUtil;
import com.clueride.service.builder.LocationBuilder;

/**
 * Services requests for Locations.
 */
public class DefaultLocationService implements LocationService {
    private static final Logger LOGGER = Logger.getLogger(DefaultLocationService.class);

    private final CourseStore courseStore;
    private final ImageStore imageStore;
    private final LocationBuilder locationBuilder;
    private final LocationStore locationStoreJpa;
    private final NodeService nodeService;
    private final PathStore pathStore;
    private final LatLonService latLonService;
    private final LocationTypeService locationTypeService;
    private final ScoredLocationService scoredLocationService;
    private final ImageService imageService;

    @Inject
    public DefaultLocationService(
            CourseStore courseStore,
            ImageStore imageStore,
            LocationBuilder locationBuilder,
            @Jpa LocationStore locationStoreJpa,
            NodeService nodeService,
            PathStore pathStore,
            LatLonService latLonService,
            LocationTypeService locationTypeService,
            ScoredLocationService scoredLocationService,
            ImageService imageService
    ) {
        this.courseStore = courseStore;
        this.imageStore = imageStore;
        this.locationBuilder = locationBuilder;
        this.locationStoreJpa = locationStoreJpa;
        this.nodeService = nodeService;
        this.pathStore = pathStore;
        this.latLonService = latLonService;
        this.locationTypeService = locationTypeService;
        this.scoredLocationService = scoredLocationService;
        this.imageService = imageService;
    }

    @Override
    public Location getLocationById(Integer locationId) {
        Location.Builder locationBuilder = locationStoreJpa.getLocationBuilderById(locationId);
        fillAndGradeLocation(locationBuilder);
        return locationBuilder.build();
    }

    @Override
    public String getLocation(Integer locationId) {
        String result = null;
        Location location = locationStoreJpa.getLocationById(locationId);
        try {
            result = PojoJsonUtil.generateLocation(location);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String getLocationGeometry(Integer locationId) {
        Location location = locationStoreJpa.getLocationById(locationId);
        return locationBuilder.getLocationFeatureCollection(location);
    }

    @Override
    public String getNearestLocations(Double lat, Double lon) {
        LOGGER.info("Retrieving Nearest Locations for (" + lat + ", " + lon + ")");
        // Brute force approach of running through all locations and keeping the top few
        List<Location> locationList = new ArrayList<>();
        locationList.addAll(locationStoreJpa.getLocations());
        Collections.sort(locationList, new LocationDistanceComparator(lat, lon));

        return getJsonStringForLocationList(locationList);
    }

    @Override
    public String getNearestMarkerLocations(Double lat, Double lon) {
        LOGGER.info("Retrieving Nearest Marker Locations for (" + lat + ", " + lon + ")");
        List<Location> locationList = new ArrayList<>();
        for (Location.Builder builder : locationStoreJpa.getLocationBuilders()) {
            fillAndGradeLocation(builder);
            locationList.add(builder.build());
        }
        // TODO: consider letting Jackson convert automatically
        return getJsonStringForLocationList(locationList);
    }

    private void fillAndGradeLocation(Location.Builder builder) {
        /* Assemble the derived transient fields. */
        builder.withLatLon(latLonService.getLatLonById(builder.getNodeId()));
        builder.withLocationType(locationTypeService.getById(builder.getLocationTypeId()));
        builder.withFeaturedImage(imageService.getImageUrl(builder.getFeaturedImageId()));

        /* Last thing to assemble; after other pieces have been put into place. */
        builder.withReadinessLevel(scoredLocationService.calculateReadinessLevel(builder));
    }

    private String getJsonStringForLocationList(List<Location> locationList) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append('[');
        int count = 0;
        for (Location location : locationList) {
            if (count > 0) {
                jsonBuilder.append("\n,");
            }
            try {
                jsonBuilder.append(PojoJsonUtil.generateLocation(location));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            count++;
        }
        jsonBuilder.append(']');
        return jsonBuilder.toString();
    }

    @Override
    public String getCourseLocations(Integer courseId) {
        LOGGER.info("Retrieving Course Locations for Course " + courseId);
        List<Location> locations = new ArrayList<>();
        Course course = courseStore.getCourseById(courseId);
        Path path = null;
        for (Integer pathId : course.getPathIds()) {
            path = pathStore.getPathById(pathId);
            locations.add(locationStoreJpa.getLocationById(path.getStartLocationId()));
        }
        if (path != null) {
            locations.add(locationStoreJpa.getLocationById(path.getEndLocationId()));
        }
        return getJsonStringForLocationList(locations);
    }

    @Override
    public void updateLocation(Location.Builder locationBuilder) {
        LOGGER.info("Updating Location " + locationBuilder.toString());
        Location.Builder existingLocationBuilder = locationStoreJpa.getLocationBuilderById(locationBuilder.getId());
        LOGGER.info("Found matching Location " + existingLocationBuilder.toString());
        existingLocationBuilder.updateFrom(locationBuilder);
        validateUpdatedLocationBuilder(existingLocationBuilder);
        locationStoreJpa.update(existingLocationBuilder);
    }

    @Override
    public Location proposeLocation(LatLon latLon, String locationTypeName) {
        LOGGER.info("Proposing a new Location of type " + locationTypeName + " at " + latLon);
        latLonService.addNew(latLon);
        LocationType locationType = locationTypeService.getByName(locationTypeName);
        Location.Builder locationBuilder = Location.Builder.builder()
                .withLatLon(latLon)
                .withLocationType(locationType);
        Integer id = null;
        try {
            id = locationStoreJpa.addNew(locationBuilder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locationStoreJpa.getLocationById(id);
    }

    @Override
    public Location unlinkFeaturedImage(Integer locationId) {
        Location.Builder locationBuilder = locationStoreJpa.getLocationBuilderById(locationId);
        locationBuilder.clearFeaturedImage();
        locationStoreJpa.update(locationBuilder);
        fillAndGradeLocation(locationBuilder);
        return locationBuilder.build();
    }

    void validateUpdatedLocationBuilder(Location.Builder locationBuilder) {
        // TODO: CA-324 - Switch this over to Puzzle.Builder check ?
//        List<Integer> validatedClueIds = new ArrayList<>();
//        for (Integer clueId : locationBuilder.getClueIds()) {
//            if (clueStore.hasValidClue(clueId)) {
//                if (!validatedClueIds.contains(clueId)) {
//                    validatedClueIds.add(clueId);
//                }
//            } else {
//                LOGGER.warn("Unable to find valid Clue with ID " + clueId);
//            }
//        }
//        locationBuilder.withClueIds(ImmutableList.copyOf(validatedClueIds));
    }

    /**
     * Generate the name and pass along to the ImageStore.
     * If the locationId isn't present, we are likely attempting to create a new Location
     * at the coordinates provided.  If we do have a locationId, then this location is used
     * and would "adjust" the lat/lon provided.
     * (Option considered: have the lat/lon stored with the image.)
     *
     * If the Location doesn't have a Featured Image, this image is added as that Featured Image.
     *
     * @param lat - Double latitude of device location.
     * @param lon - Double longitude of device location.
     * @param locationId - Optional Integer representing an existing location (which may not have been created yet).
     * @param fileData - InputStream from which we read the image data to put into a file.
     * @return Integer representing the ID of the newly created Image.
     */
    @Override
    public Image saveLocationImage(Double lat, Double lon, Integer locationId, InputStream fileData) {
        LOGGER.info("Requesting Save of image for Location ID " + locationId);
        Integer newSeqId = persistImageContent(locationId, fileData);
        Image image = persistImageMetadata(locationId, newSeqId);
        Location.Builder locationBuilder = locationStoreJpa.getLocationBuilderById(locationId);
        if (locationBuilder.hasNoFeaturedImage()) {
            locationBuilder.withFeaturedImageId(image.getId());
            locationStoreJpa.update(locationBuilder);
        }
        return image;
    }

    private Integer persistImageContent(Integer locationId, InputStream fileData) {
        InputStream convertedFileData = decodeBase64(fileData);
        Integer newSeqId = null;
        try {
            newSeqId = imageStore.addNew(locationId, convertedFileData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return newSeqId;
    }

    private Image persistImageMetadata(Integer locationId, Integer newSeqId) {
        // TODO: URL responsibility probably belongs with the image service.
        String imageUrlString =
                "https://images.clueride.com/img/" + locationId + "/" + newSeqId + ".jpg";
        Image.Builder imageBuilder = Image.Builder.builder()
                .withUrlString(imageUrlString);
        try {
            Integer recordId = imageService.addNewToLocation(imageBuilder, locationId);
            imageBuilder.withId(recordId);
        } catch (MalformedURLException e) {
            LOGGER.error("Messed up the URL creation: " + imageUrlString, e);
        }
        return imageBuilder.build();
    }

    InputStream decodeBase64(InputStream fileData) {
        InputStreamReader reader = new InputStreamReader(fileData);
        try {
            while (reader.read() != ',') {
                // skip the header
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BaseEncoding.base64().decodingStream(reader);
    }

    private class LocationDistanceComparator implements Comparator<Location> {
        private final Double lat, lon;

        LocationDistanceComparator(Double lat, Double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        /** Brute force assumption #2: We're flat enough near the location of interest we don't worry about curvature. */
        @Override
        public int compare(Location l1, Location l2) {
            Point p1 = nodeService.getPointByNodeId(l1.getNodeId());
            Double distanceToL1 = Math.pow((p1.getCoordinate().y - lat),2)
                    + Math.pow((p1.getCoordinate().x - lon),2);
            Point node2 = nodeService.getPointByNodeId(l2.getNodeId());
            Double distanceToL2 = Math.pow((node2.getCoordinate().y - lat),2)
                    + Math.pow((node2.getCoordinate().x - lon),2);
            return distanceToL1.compareTo(distanceToL2);
        }

        @Override
        /** All objects passed to this Comparator are expected to be distinct; base on distance alone. */
        public boolean equals(Object o) {
            return false;
        }
    }

}
