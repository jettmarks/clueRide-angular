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

import com.clueride.dao.ImageStore;
import com.clueride.dao.LocationStore;
import com.clueride.domain.user.Location;
import com.clueride.io.PojoJsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.vividsolutions.jts.geom.Point;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Services requests for Locations.
 */
public class DefaultLocationService implements LocationService {

    private final LocationStore locationStore;
    private final NodeService nodeService;
    private final ImageStore imageStore;

    @Inject
    public DefaultLocationService(
            LocationStore locationStore,
            ImageStore imageStore,
            NodeService nodeService
    ) {
        this.locationStore = locationStore;
        this.imageStore = imageStore;
        this.nodeService = nodeService;
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
    public String getNearestLocations(Double lat, Double lon) {
        // Brute force approach of running through all locations and keeping the top five
        List<Location> locationList = new ArrayList<>();
        for (Location location : locationStore.getLocations()) {
            locationList.add(location);
        }
        Collections.sort(locationList, new LocationDistanceComparator(lat, lon));

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
            if (++count == 5) break;
        }
        jsonBuilder.append(']');
        return jsonBuilder.toString();
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
        Integer newSeqId = null;
        try {
            newSeqId = imageStore.addNew(locationId, fileData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        URL imageUrl = null;
        try {
            imageUrl = new URL("http://img.clueride.com/img/" + locationId + "/" + newSeqId + ".jpg");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Location location = locationStore.getLocationById(locationId);
        location.getImageUrls().add(imageUrl);
        locationStore.update(location);
    }
}
