/**
 *   Copyright 2015 Jett Marks
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created Aug 15, 2015
 */
package com.clueride.domain.user;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.clueride.domain.Step;
import com.clueride.service.IdProvider;
import com.clueride.service.MemoryBasedLocationIdProvider;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Objects.requireNonNull;

/**
 * Holds the data for a Point of Interest, or easier to say, a Location.
 *
 * @author jett
 */
@Immutable
public class Location implements Step {
    private final Integer id;
    private final String name;
    private final String description;
    private final LocationType locationType;
    private final Integer nodeId;
    private final Location.Point point;
    private final URL featuredImage;
    private final Integer googlePlaceId;
    private List<Integer> clueIds;
    private final List<URL> imageUrls;
    private final Optional<Integer> locationGroupId;
    private final Optional<Establishment> establishment;
    private final Map<String,Optional<Double>> tagScores;
    private final static Integer SYNCH_LOCK = -1;

    /**
     * Constructor accepting Builder instance.
     * @param builder
     */
    private Location(Builder builder) {
        id = requireNonNull(builder.getId(), "Location ID missing");
        nodeId = requireNonNull(builder.getNodeId(), "Location Node (point) missing");
        point = builder.getPoint();

        // If any of these are missing, we're at the Draft level
        name = builder.getName();
        description = builder.getDescription();
        locationType = builder.getLocationType();
        featuredImage = builder.getFeaturedImage();

        // If we have multiple images, the ranking goes up
        imageUrls = builder.getImageUrls();

        // If this is missing, we're at the Place level; present, we're at the Attraction level
        clueIds = builder.getClueIds();

        // Featured Level requires the following
        googlePlaceId = builder.getGooglePlaceId();

        // Possibly empty list
        tagScores = builder.getTagScores();

        // Optional values
        locationGroupId = builder.getLocationGroupId();
        establishment = builder.getEstablishment();
    }

    public Integer getId() {
        return id;
    }

    /**
     * Human readable name for this location.
     * Shows up in headings for the Location, Lists of Locations, and as pop-ups on the map.
     * @return String representing human-readable name of Location.
     */
    public String getName() {
        return name;
    }

    /**
     * Textual description of this Location.
     * Describes why this location is interesting.
     * @return String representing a description of this Location.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Enumeration of the type of Location.
     * Plays a role in helping select locations and categorizes them.
     * @return Enumeration of the type of Location.
     */
    public LocationType getLocationType() {
        return locationType;
    }

    /**
     * Link over to the geographical representation of this Location.
     * @return ID of the Node associated with this location for placement on a map.
     */
    public Integer getNodeId() {
        return nodeId;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", locationType=" + locationType +
                ", nodeId=" + nodeId +
                ", clueIds=" + clueIds +
                ", imageUrls=" + imageUrls +
                ", locationGroupId=" + locationGroupId +
                ", establishment=" + establishment +
                ", tagScores=" + tagScores +
                '}';
    }

    public Map<String, Optional<Double>> getTagScores() {
        return tagScores;
    }

    /**
     * This method returns null for an empty Location Group to
     * accommodate the JSON writer, but not sure this is what
     * we'll continue to want.
     * @return Integer or null if not present.
     */
    public Integer getLocationGroupId() {
        if(locationGroupId != null && locationGroupId.isPresent()) {
            return locationGroupId.get();
        } else {
            return null;
        }
    }

    public List<Integer> getClueIds() {
        return clueIds;
    }

    public Optional<Establishment> getEstablishment() {
        return establishment;
    }

    public URL getFeaturedImage() {
        return featuredImage;
    }

    public List<URL> getImageUrls() {
        return imageUrls;
    }

    /**
     * Determines progress against criteria described here: http://bikehighways.wikidot.com/clueride-location-details
     * @return Readiness level based on completeness of the fields for this object.
     */
    public LocationLevel getReadinessLevel() {
        /* Handle anything that could make this a draft. */
        if (isNullOrEmpty(name)
                || isNullOrEmpty(description)
                || isNullOrEmpty(String.valueOf(featuredImage))
                || isNullOrEmpty(String.valueOf(locationType))
        ) {
            return LocationLevel.DRAFT;
        }

        /* If we're missing the Clues, we're just a Place. */
        if (clueIds.size() == 0) {
            return LocationLevel.PLACE;
        }

        /* If everything else is defined except our Google Place ID, we're an Attraction. */
        if (googlePlaceId == null) {
            return LocationLevel.ATTRACTION;
        } else {
            return LocationLevel.FEATURED;
        }

    }

    public Integer getGooglePlaceId() {
        return googlePlaceId;
    }

    /**
     * List of Strings that in addition to LocationType, each categorize the location.
     * Perhaps a placeholder at this time until I think through further what sorts of tags might be applied.
     * @return Set of Strings, one for each tag added to this location.
    public Set<String> getTags() {
    return tagScores.keySet();
    }
     */

    // TODO: not settled on this API
    public Optional<Double> getScorePerTag(String tag) {
        return tagScores.get(tag);
    }

    public void removeClue(Integer clueId) {
        synchronized(SYNCH_LOCK) {
            List<Integer> remainingClueIds = new ArrayList<>();
            for (Integer clueToCheck : clueIds) {
                if (!clueId.equals(clueToCheck) && clueToCheck != null) {
                    remainingClueIds.add(clueToCheck);
                }
            }
            this.clueIds = ImmutableList.copyOf(remainingClueIds);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }


    /**
     * Temporary class representing a point until we can sort out how we want to use this.
     */
    public static final class Point {
        public Double lat;
        public Double lon;
        public Double elev;
    }


    /**
     * Knows how to assemble the parts of a Location.
     */
    public static final class Builder {
        private Integer id;
        private String name;
        private String description;
        private LocationType locationType;
        private Integer nodeId;
        private Location.Point point;
        private List<Integer> clueIds;
        private List<URL> imageUrls;
        private URL featuredImage;
        private Integer googlePlaceId;

        private Optional<Establishment> establishment;
        private Optional<Integer> locationGroupId;
        private Map<String,Optional<Double>> tagScores = new HashMap<>();

        private IdProvider idProvider;

        public Builder() {
            idProvider = new MemoryBasedLocationIdProvider();
        }

        public static Builder builder() {
            return new Builder();
        }

        public static Builder from(Location location) {
            return builder()
                    .withId(location.id)
                    .withName(location.name)
                    .withDescription(location.description)
                    .withLocationType(location.locationType)
                    .withNodeId(location.getNodeId())
                    .withPoint(location.point)
                    .withClueIds(location.clueIds)
                    .withImageUrls(location.imageUrls)
                    .withEstablishment(location.establishment)
                    .withTagScores(location.tagScores)
                    ;
        }

        public Location build() {
            return new Location(this);
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        /**
         * If the value has been set (by reading from the LocationStore), use that
         * value, otherwise, we're creating a new instance the and idProvider should
         * give us the next available value.
         * @return Unique ID for this instance of Location.
         */
        public Integer getId() {
            return (id == null ? idProvider.getId() : id);
        }

        public String getName() {
            return name;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public LocationType getLocationType() {
            return locationType;
        }

        public Builder withLocationType(LocationType locationType) {
            this.locationType = locationType;
            return this;
        }

        public Integer getNodeId() {
            return nodeId;
        }

        public Builder withNodeId(Integer nodeId) {
            this.nodeId = nodeId;
            return this;
        }

        public Map<String, Optional<Double>> getTagScores() {
            return tagScores;
        }

        public Builder withTagScores(Map<String, Optional<Double>> tagScores) {
            this.tagScores = tagScores;
            return this;
        }

        public Optional<Integer> getLocationGroupId() {
            return locationGroupId;
        }

        public Builder withLocationGroupId(Optional<Integer> locationGroupId) {
            this.locationGroupId = locationGroupId;
            return this;
        }

        public List<Integer> getClueIds() {
            return clueIds;
        }

        public Builder withClueIds(List<Integer> clueIds) {
            validateClueIds(clueIds);
            this.clueIds = clueIds;
            return this;
        }

        private void validateClueIds(List<Integer> clueIds) {

        }

        public Optional<Establishment> getEstablishment() {
            return establishment;
        }

        public Builder withEstablishment(Optional<Establishment> establishment) {
            this.establishment = establishment;
            return this;
        }

        public List<URL> getImageUrls() {
            return imageUrls;
        }

        public Builder withImageUrls(List<URL> imageUrls) {
            this.imageUrls = imageUrls;
            return this;
        }

        public Point getPoint() {
            return point;
        }

        public Builder withPoint(Point point) {
            this.point = point;
            return this;
        }

        public URL getFeaturedImage() {
            return featuredImage;
        }

        public Builder withFeaturedImage(URL featuredImage) {
            this.featuredImage = featuredImage;
            return this;
        }

        public Integer getGooglePlaceId() {
            return googlePlaceId;
        }

        public Builder withGooglePlaceId(Integer googlePlaceId) {
            this.googlePlaceId = googlePlaceId;
            return this;
        }

        /**
         * For creating a copy from a DTO instance.
         * @param locationDto - inbound from the Web REST API.
         * @return populated Builder.
         */
        public static Builder builder(com.clueride.rest.dto.Location locationDto) {
            return builder()
                    .withName(locationDto.name)
                    .withId(locationDto.id)
                    .withDescription(locationDto.description)
                    .withNodeId(locationDto.nodeId)
                    .withLocationType(locationDto.locationType)
                    .withLocationGroupId(Optional.fromNullable(locationDto.locationGroupId))
                    .withClueIds(Arrays.asList(locationDto.clueIds))
                    .withImageUrls(locationDto.imageUrls)
//                    .withEstablishment(Optional.<Establishment>fromNullable(locationDto.establishment))
                    ;
        }
    }

}
