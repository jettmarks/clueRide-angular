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
package com.clueride.domain.user.location;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.clueride.domain.Step;
import com.clueride.domain.user.ReadinessLevel;
import com.clueride.domain.user.latlon.LatLon;
import com.clueride.domain.user.loctype.LocationType;
import com.clueride.domain.user.puzzle.Puzzle;
import com.clueride.service.IdProvider;
import com.clueride.service.MemoryBasedLocationIdProvider;

/**
 * Holds the data for a Location, and provides some of the logic to provide derived properties.
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
    private final URL featuredImage;
    private final Integer googlePlaceId;
    private final LatLon latLon;
    private final ReadinessLevel readinessLevel;
    private List<Puzzle.Builder> puzzleBuilders;
    private final List<URL> imageUrls;
    private final Integer locationGroupId;
    private final String establishment;
    private final Integer establishmentId;
    private final Map<String,Optional<Double>> tagScores;
    private final static Integer SYNCH_LOCK = -1;

    /**
     * Constructor accepting Builder instance.
     * @param builder instance carrying mutable Location.
     */
    public Location(Builder builder) {
        id = builder.getId();
        nodeId = builder.getNodeId();
        latLon = builder.getLatLon();
        readinessLevel = builder.getReadinessLevel();

        // If any of these are missing, we're at the Draft level
        name = builder.getName();
        description = builder.getDescription();
        locationType = builder.getLocationType();
        featuredImage = builder.getFeaturedImage();

        // If we have multiple images, the ranking goes up
        imageUrls = builder.getImageUrls();

        // If this is missing, we're at the Place level; present, we're at the Attraction level
        puzzleBuilders = builder.getPuzzleBuilders();

        // Featured Level requires the following
        googlePlaceId = builder.getGooglePlaceId();

        // Possibly empty list
        tagScores = builder.getTagScores();

        // Optional values
        locationGroupId = builder.getLocationGroupId();
        establishmentId = builder.getEstablishmentId();
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
    public Integer getLocationTypeId() {
        return locationType.getId();
    }

    public String getLocationTypeName() {
        return locationType.getName();
    }

    /**
     * Link over to the geographical representation of this Location.
     * @return ID of the LatLon associated with this location for placement on a map.
     */
    public Integer getNodeId() {
        return nodeId;
    }

    public LatLon getLatLon() {
        return latLon;
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
        if(locationGroupId != null) {
            return locationGroupId;
        } else {
            return null;
        }
    }

    public Integer getEstablishment() {
        return establishmentId;
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
    public ReadinessLevel getReadinessLevel() {
        return readinessLevel;
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

    // TODO: CA-324 -- Move this logic into the service
    public void removePuzzle(Integer puzzleId) {
        synchronized(SYNCH_LOCK) {
            List<Puzzle.Builder> remainingPuzzles = new ArrayList<>();
            for (Puzzle.Builder builder : puzzleBuilders) {
                if (!puzzleId.equals(builder.getId()) && builder != null) {
                    remainingPuzzles.add(builder);
                }
            }
            this.puzzleBuilders = ImmutableList.copyOf(remainingPuzzles);
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Knows how to assemble the parts of a Location.
     */
    @Entity(name="location")
    public static final class Builder {
        @Id
        @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="location_pk_sequence")
        @SequenceGenerator(name="location_pk_sequence",sequenceName="location_id_seq", allocationSize=1)
        private Integer id;

        private String name;
        private String description;

        @Column(name= "location_type_id") private Integer locationTypeId;

        @Column(name="node_id") private Integer nodeId;
        @Column(name="featured_image_id") private Integer featuredImageId;

        @OneToMany(mappedBy = "locationBuilder")
        private List<Puzzle.Builder> puzzleBuilders;

        // TODO: CA-325 - Coming out after we abandon the Json Clues
        @Transient
        private List<Integer> clueIds;

        @Transient
        private LocationType locationType;
        @Transient
        private String locationTypeName;
        @Transient
        private LatLon latLon;
        @Transient
        private List<URL> imageUrls;
        @Transient
        private URL featuredImage;
        @Transient
        private Integer googlePlaceId;

        @Transient
        private Integer establishmentId;
        @Column(name="location_group_id") private Integer locationGroupId;

        @Transient
        private Map<String,Optional<Double>> tagScores = new HashMap<>();

        @Transient
        private IdProvider idProvider;

        @Transient
        private String establishment;

        @Transient
        private ReadinessLevel readinessLevel;

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
                    .withLatLon(location.latLon)
                    .withPuzzleBuilders(location.puzzleBuilders)
                    .withFeaturedImage(location.featuredImage)
                    .withImageUrls(location.imageUrls)
                    .withEstablishmentId(location.establishmentId)
                    .withTagScores(location.tagScores)
                    ;
        }

        public Location build() {
            if (locationType == null) {
                throw new IllegalStateException("Location Type cannot be null");
            }
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

        public Builder withLocationTypeId(Integer locationTypeId) {
            this.locationTypeId = locationTypeId;
            return this;
        }

        public Integer getLocationTypeId() {
            return locationTypeId;
        }

        public Builder withLocationTypeName(String locationTypeName) {
            this.locationTypeName = locationTypeName;
            return this;
        }

        public String getLocationTypeName() {
            return this.locationTypeName;
        }

        public void setLocationType(LocationType locationType) {
            this.withLocationType(locationType);
        }

        public Builder withLocationType(LocationType locationType) {
            this.locationType = locationType;
            this.locationTypeId = locationType.getId();
            return this;
        }

        public Builder withReadinessLevel(ReadinessLevel readinessLevel) {
            this.readinessLevel = readinessLevel;
            return this;
        }

        public Integer getNodeId() {
            return nodeId;
        }

        public Builder withNodeId(Integer nodeId) {
            this.nodeId = nodeId;
            return this;
        }

        public List<Puzzle.Builder> getPuzzleBuilders() {
            return puzzleBuilders;
        }

        public Builder withPuzzleBuilders(List<Puzzle.Builder> puzzleBuilders) {
            this.puzzleBuilders = puzzleBuilders;
            return this;
        }

        public Builder addPuzzleBuilder(Puzzle.Builder puzzleBuilder) {
            this.puzzleBuilders.add(puzzleBuilder);
            return this;
        }

        public Map<String, Optional<Double>> getTagScores() {
            return tagScores;
        }

        public Builder withTagScores(Map<String, Optional<Double>> tagScores) {
            this.tagScores = tagScores;
            return this;
        }

        public Integer getLocationGroupId() {
            return locationGroupId;
        }

        public Builder withLocationGroupId(Integer locationGroupId) {
            this.locationGroupId = locationGroupId;
            return this;
        }

        public Integer getEstablishmentId() {
            return establishmentId;
        }

        public Builder withEstablishmentId(Integer establishmentId) {
            this.establishmentId = establishmentId;
            return this;
        }

        public String getEstablishment() {
            return establishment;
        }

        public Builder withEstablishment(String establishment) {
            this.establishment = establishment;
            return this;
        }

        public List<URL> getImageUrls() {
            if (imageUrls != null) {
                return imageUrls;
            } else {
                return Collections.emptyList();
            }
        }

        public Builder withImageUrls(List<URL> imageUrls) {
            this.imageUrls = imageUrls;
            return this;
        }

        public LatLon getLatLon() {
            return latLon;
        }

        public Builder withLatLon(LatLon latLon) {
            this.latLon = latLon;
            if (latLon != null) {
                this.nodeId = latLon.getId();
            }
            return this;
        }

        public URL getFeaturedImage() {
            return featuredImage;
        }

        public Builder withFeaturedImage(URL featuredImage) {
            this.featuredImage = featuredImage;
            return this;
        }

        public Builder clearFeaturedImage() {
            this.featuredImageId = null;
            this.featuredImage = null;
            return this;
        }

        public boolean hasNoFeaturedImage() {
            return featuredImageId == null;
        }

        public Integer getFeaturedImageId() {
            return featuredImageId;
        }

        public Builder withFeaturedImageId(int imageId) {
            this.featuredImageId = imageId;
            return this;
        }

        public Integer getGooglePlaceId() {
            return googlePlaceId;
        }

        public Builder withGooglePlaceId(Integer googlePlaceId) {
            this.googlePlaceId = googlePlaceId;
            return this;
        }

        public ReadinessLevel getReadinessLevel() {
            return readinessLevel;
        }

        /**
         * Accepts a partial set of information -- generally posted from REST API -- and updates this copy with the
         * new fields.
         * @param locationBuilder instance with the new info.
         */
        public void updateFrom(Builder locationBuilder) {
            this
                    .withName(locationBuilder.name)
                    .withId(locationBuilder.id)
                    .withDescription(locationBuilder.description)
                    .withNodeId(locationBuilder.nodeId)
                    .withLocationTypeId(locationBuilder.locationTypeId)
                    .withLocationGroupId(locationBuilder.locationGroupId)
                    .withImageUrls(locationBuilder.imageUrls);
        }

        /* TODO: CA-325 Only useful for moving from JSON to JPA; remove after JSON is abandoned for Clues. */
        public List<Integer> getClueIds() {
            return clueIds;
        }

        public Builder withClueIds(List<Integer> clueIds) {
            this.clueIds = clueIds;
            return this;
        }
    }

}
