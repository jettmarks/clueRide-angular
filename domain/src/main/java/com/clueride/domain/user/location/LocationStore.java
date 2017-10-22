/**
 * Copyright 2015 Jett Marks
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Created by jett on 11/23/15.
 */
package com.clueride.domain.user.location;

import java.io.IOException;
import java.util.Collection;

// TODO: CA-309: Location Store API is straddling two implementations; clean these up.
public interface LocationStore {

    /**
     * Accepts a fully constructed Location to the store and returns the ID.
     * Implementations are expected to write to permanent storage.
     * @param location newly and fully constructed Location, ready to persist.
     * @return ID of the new Location.
     * @deprecated prefer Builder.
     */
    @Deprecated
    Integer addNew(Location location) throws IOException;

    /**
     * Accepts a partially constructed Location to the store and returns the ID.
     * Implementations are expected to write to permanent storage.
     * @param locationBuilder newly proposed Location, ready to persist.
     * @return ID of the new Location.
     */
    Integer addNew(Location.Builder locationBuilder) throws IOException;

    /**
     * Returns the Location matching the ID from the store.
     *
     * @param id of the Location we expect to retrieve from the store.
     * @return Location with the given ID or null if no location is found.
     * @deprecated prefer the Builder.
     */
    @Deprecated
    Location getLocationById(Integer id);

    /**
     * Returns the Location Builder matching the unique ID.
     * @param id unique identifier for the Location Builder.
     * @return fully-populated Location Builder.
     */
    Location.Builder getLocationBuilderById(Integer id);

    /**
     * Returns the list of Locations maintained by this store.
     *
     * The Store is populated from persisted store upon instantiation, so
     * the initial contents will come from some permanent storage.
     * @return Collection of all Locations in the store.
     * @deprecated - Prefer returning Builders so the Service can handle build() problems.
     */
    @Deprecated
    Collection<Location> getLocations();

    /**
     * Returns the list of Location Builders maintained by this store.
     * @return Collection of all Locations in the store.
     */
    Collection<Location.Builder> getLocationBuilders();

    /**
     * Accepts an existing Location and updates the persistent record with new information.
     * @param location to be updated.
     * @deprecated prefer Builder.
     */
    @Deprecated
    void update(Location location);

    /**
     * Accepts an existing Location and updates the persistent record with new information.
     * @param locationBuilder to be updated.
     */
    void update(Location.Builder locationBuilder);

}
