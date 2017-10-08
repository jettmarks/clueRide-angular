/*
 * Copyright 2017 Jett Marks
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
 * Created by jett on 10/4/17.
 */
package com.clueride.domain.user.loctype;

import java.util.List;

import com.google.inject.Inject;

/**
 * Service layer for Location Types.
 * Cacheing would occur at this layer.
 */
public class LocationTypeServiceImpl implements LocationTypeService {
    private final LocationTypeStore locationTypeStore;

    @Inject
    public LocationTypeServiceImpl(LocationTypeStore locationTypeStore) {
        this.locationTypeStore = locationTypeStore;
    }

    @Override
    public List<LocationType> getLocationTypes() {
        return locationTypeStore.getLocationTypes();
    }

    @Override
    public LocationType getByName(String locationTypeName) {
        return locationTypeStore.getLocationTypeByName(locationTypeName);
    }

    @Override
    public LocationType getById(Integer locationTypeId) {
        return locationTypeStore.getLocationTypeById(locationTypeId);
    }
}
