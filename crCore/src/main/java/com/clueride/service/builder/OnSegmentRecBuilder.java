/*
 * Copyright 2016 Jett Marks
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
 * Created by jett on 2/7/16.
 */
package com.clueride.service.builder;

import org.apache.log4j.Logger;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkRecommendation;
import com.clueride.domain.dev.rec.OnSegment;
import com.clueride.domain.dev.rec.OnSegmentImpl;
import com.clueride.feature.Edge;

/**
 * Constructs a Recommendation based on an existing segment which will be split
 * at the given location to provide two segments and the new node.
 */
public class OnSegmentRecBuilder {
    private static final Logger LOGGER = Logger.getLogger(OnSegmentRecBuilder.class);

    private final GeoNode newNode;
    private final Edge existingEdge;


    public OnSegmentRecBuilder(GeoNode newNode, Edge existingEdge) {
        this.newNode = newNode;
        this.existingEdge = existingEdge;
    }


    public NetworkRecommendation build() {
        OnSegment rec = new OnSegmentImpl(newNode, existingEdge);
        return rec;
    }
}
