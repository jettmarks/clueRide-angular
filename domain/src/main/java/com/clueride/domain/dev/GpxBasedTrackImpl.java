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
 * Created by jett on 12/26/15.
 */
package com.clueride.domain.dev;

import com.clueride.service.IdProvider;
import com.clueride.service.TrackIdProvider;

/**
 * When creating Tracks from GPX-based Tracks, we assign a different set of IDs
 * using this class.
 *
 * Simplest thing that works.
 */
public class GpxBasedTrackImpl extends TrackImpl {
    private final IdProvider idProvider = new TrackIdProvider();

    /**
     * Canonical constructor.
     *
     * @param displayName
     * @param url
     */
    public GpxBasedTrackImpl(
            String displayName,
            String url
    ) {
        super(displayName, url);
        this.id = idProvider.getId();
    }
}
