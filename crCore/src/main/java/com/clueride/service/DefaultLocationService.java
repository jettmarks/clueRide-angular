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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import com.vividsolutions.jts.geom.Point;
import org.apache.log4j.Logger;

import com.clueride.dao.ClueStore;
import com.clueride.dao.CourseStore;
import com.clueride.dao.ImageStore;
import com.clueride.dao.LocationStore;
import com.clueride.dao.PathStore;
import com.clueride.domain.Course;
import com.clueride.domain.user.Location;
import com.clueride.domain.user.LocationType;
import com.clueride.domain.user.Path;
import com.clueride.io.PojoJsonUtil;
import com.clueride.service.builder.LocationBuilder;

/**
 * Services requests for Locations.
 */
public class DefaultLocationService implements LocationService {
    private static final Logger LOGGER = Logger.getLogger(DefaultLocationService.class);

    private final ClueStore clueStore;
    private final CourseStore courseStore;
    private final ImageStore imageStore;
    private final LocationBuilder locationBuilder;
    private final LocationStore locationStore;
    private final NodeService nodeService;
    private final PathStore pathStore;

    @Inject
    public DefaultLocationService(
            ClueStore clueStore,
            CourseStore courseStore,
            ImageStore imageStore,
            LocationBuilder locationBuilder,
            LocationStore locationStore,
            NodeService nodeService,
            PathStore pathStore
    ) {
        this.clueStore = clueStore;
        this.courseStore = courseStore;
        this.imageStore = imageStore;
        this.locationBuilder = locationBuilder;
        this.locationStore = locationStore;
        this.nodeService = nodeService;
        this.pathStore = pathStore;
    }

    @Override
    public String getLocation(Integer locationId) {
        String result = null;
        Location location = locationStore.getLocationById(locationId);
        try {
            result = PojoJsonUtil.generateLocation(location);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String getLocationGeometry(Integer locationId) {
        Location location = locationStore.getLocationById(locationId);
        return locationBuilder.getLocationFeatureCollection(location);
    }

    @Override
    public String getNearestLocations(Double lat, Double lon) {
        LOGGER.info("Retrieving Nearest Locations for (" + lat + ", " + lon + ")");
        // Brute force approach of running through all locations and keeping the top few
        List<Location> locationList = new ArrayList<>();
        for (Location location : locationStore.getLocations()) {
            locationList.add(location);
        }
        Collections.sort(locationList, new LocationDistanceComparator(lat, lon));

        return getJsonStringForLocationList(locationList);
    }

    @Override
    public String getNearestMarkerLocations(Double lat, Double lon) {
        LOGGER.info("Retrieving Nearest Marker Locations for (" + lat + ", " + lon + ")");
        // Brute force approach of running through all locations and keeping the top few
        List<Location> locationList = new ArrayList<>();
        for (Location location : locationStore.getLocations()) {
            Point point = nodeService.getPointByNodeId(location.getNodeId());
            Location.Point locPoint = new Location.Point();
            locPoint.lat = point.getY();
            locPoint.lon = point.getX();
            Location.Builder locationBuilder = Location.Builder.from(location).withPoint(locPoint);
            locationList.add(locationBuilder.build());
        }
//        Collections.sort(locationList, new LocationDistanceComparator(lat, lon));

        return getJsonStringForLocationList(locationList);
    }

    public String getJsonStringForLocationList(List<Location> locationList) {
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
            locations.add(locationStore.getLocationById(path.getStartLocationId()));
        }
        if (path != null) {
            locations.add(locationStore.getLocationById(path.getEndLocationId()));
        }
        return getJsonStringForLocationList(locations);
    }

    @Override
    public void updateLocation(com.clueride.rest.dto.Location location) {
        LOGGER.info("Updating Location " + location.toString());
        Location existingLocation = locationStore.getLocationById(location.id);
        LOGGER.info("Found matching Location " + existingLocation.toString());
        Location.Builder locationBuilder = Location.Builder.builder(location);
        validateUpdatedLocationBuilder(locationBuilder);
        locationStore.update(locationBuilder.build());
    }

    void validateUpdatedLocationBuilder(Location.Builder locationBuilder) {
        List<Integer> validatedClueIds = new ArrayList<>();
        for (Integer clueId : locationBuilder.getClueIds()) {
            if (clueStore.hasValidClue(clueId)) {
                if (!validatedClueIds.contains(clueId)) {
                    validatedClueIds.add(clueId);
                }
            } else {
                LOGGER.warn("Unable to find valid Clue with ID " + clueId);
            }
        }
        locationBuilder.withClueIds(ImmutableList.copyOf(validatedClueIds));
    }

    @Override
    public String getLocationTypes() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        boolean firstTimeThrough = true;
        for (LocationType locationType : LocationType.values()) {
            if (!firstTimeThrough) {
                stringBuilder.append(",");
            }
            stringBuilder.append("\"").append(locationType).append("\"");
            firstTimeThrough = false;
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private class LocationDistanceComparator implements Comparator<Location> {
        private final Double lat, lon;

        public LocationDistanceComparator(Double lat, Double lon) {
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

    /**
     * Generate the name and pass along to the ImageStore.
     * If the locationId isn't present, we are likely attempting to create a new Location
     * at the coordinates provided.  If we do have a locationId, then this location is used
     * and would "adjust" the lat/lon provided.
     * (Option considered: have the lat/lon stored with the image.)
     * @param lat - Double latitude of device location.
     * @param lon - Double longitude of device location.
     * @param locationId - Optional Integer representing an existing location (which may not have been created yet).
     * @param fileData - InputStream from which we read the image data to put into a file.
     */
    @Override
    public void saveLocationImage(Double lat, Double lon, Integer locationId, InputStream fileData) {
        LOGGER.info("Requesting Save of image for Location ID " + locationId);
        Integer newSeqId = null;
        try {
            newSeqId = imageStore.addNew(locationId, fileData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        URL imageUrl = null;
        try {
            imageUrl = new URL("https://images.clueride.com/img/" + locationId + "/" + newSeqId + ".jpg");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Location location = locationStore.getLocationById(locationId);
        location.getImageUrls().add(imageUrl);
        locationStore.update(location);
    }
}
