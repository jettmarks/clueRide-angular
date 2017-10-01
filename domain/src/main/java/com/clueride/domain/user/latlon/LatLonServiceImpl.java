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
 * Created by jett on 9/10/17.
 */
package com.clueride.domain.user.latlon;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Basically a wrapper for the DAO at this time.
 */
public class LatLonServiceImpl implements LatLonService {
    private final LatLonStore latLonStore;

    @Inject
    public LatLonServiceImpl(
            LatLonStore latLonStore
    ) {
        this.latLonStore = latLonStore;
    }

    @Override
    public LatLon addNew(LatLon latLon) {
        try {
            latLonStore.addNew(latLon);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Storage problem");
        }
        return latLon;
    }

    @Override
    public LatLon getLatLonById(Integer id) {
        return latLonStore.getLatLonById(id);
    }

}
