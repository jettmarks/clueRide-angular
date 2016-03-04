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

import com.clueride.domain.Step;
import com.clueride.service.IdProvider;
import com.clueride.service.MemoryBasedLocationIdProvider;
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
    private List<Integer> clueIds;
    private final List<URL> imageUrls;
    private final Optional<Integer> locationGroupId;
    private final Optional<Establishment> establishment;
    private final Map<String,Optional<Double>> tagScores;

    private final static Integer SYNCH_LOCK = -1;

    public Location(Builder builder) {
        // Required values
        id = requireNonNull(builder.getId(), "Location ID missing");
        name = requireNonNull(builder.getName(), "Location name missing");
        description = requireNonNull(builder.getDescription(), "Location description missing");
        locationType = requireNonNull(builder.getLocationType(), "Location Type missing");
        nodeId = requireNonNull(builder.getNodeId(), "Location Node (point) missing");
        // Lists that cannot be empty
        imageUrls = requireNonNull(builder.getImageUrls(), "Location images missing");
        if (imageUrls.isEmpty()) {
            throw new IllegalArgumentException("Location must have at least one image");
        }
        clueIds = requireNonNull(builder.getClueIds(), "Location clues missing");
        if (clueIds.isEmpty()) {
            throw new IllegalArgumentException("Location must have at least one clue");
        }

        // Possibly empty list
        tagScores = requireNonNull(builder.getTagScores());

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

    public List<URL> getImageUrls() {
        return imageUrls;
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

    public static final class Builder {
        private Integer id;
        private String name;
        private String description;
        private LocationType locationType;
        private Integer nodeId;
        private List<Integer> clueIds;
        private List<URL> imageUrls;

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

        public Builder setId(Integer id) {
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

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public LocationType getLocationType() {
            return locationType;
        }

        public Builder setLocationType(LocationType locationType) {
            this.locationType = locationType;
            return this;
        }

        public Integer getNodeId() {
            return nodeId;
        }

        public Builder setNodeId(Integer nodeId) {
            this.nodeId = nodeId;
            return this;
        }

        public Map<String, Optional<Double>> getTagScores() {
            return tagScores;
        }

        public Builder setTagScores(Map<String, Optional<Double>> tagScores) {
            this.tagScores = tagScores;
            return this;
        }

        public Optional<Integer> getLocationGroupId() {
            return locationGroupId;
        }

        public Builder setLocationGroupId(Optional<Integer> locationGroupId) {
            this.locationGroupId = locationGroupId;
            return this;
        }

        public List<Integer> getClueIds() {
            return clueIds;
        }

        public Builder setClueIds(List<Integer> clueIds) {
            validateClueIds(clueIds);
            this.clueIds = clueIds;
            return this;
        }

        private void validateClueIds(List<Integer> clueIds) {

        }

        public Optional<Establishment> getEstablishment() {
            return establishment;
        }

        public Builder setEstablishment(Optional<Establishment> establishment) {
            this.establishment = establishment;
            return this;
        }

        public List<URL> getImageUrls() {
            return imageUrls;
        }

        public Builder setImageUrls(List<URL> imageUrls) {
            this.imageUrls = imageUrls;
            return this;
        }

        public Location build() {
            return new Location(this);
        }

        /**
         * For creating a copy from a DTO instance.
         * @param locationDto - inbound from the Web REST API.
         * @return populated Builder.
         */
        public static Builder builder(com.clueride.rest.dto.Location locationDto) {
            return builder()
                    .setName(locationDto.name)
                    .setId(locationDto.id)
                    .setDescription(locationDto.description)
                    .setNodeId(locationDto.nodeId)
                    .setLocationType(locationDto.locationType)
                    .setLocationGroupId(Optional.fromNullable(locationDto.locationGroupId))
                    .setClueIds(Arrays.asList(locationDto.clueIds))
                    .setImageUrls(locationDto.imageUrls)
//                    .setEstablishment(Optional.<Establishment>fromNullable(locationDto.establishment))
            ;
        }
    }
}
