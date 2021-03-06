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
 * Created by jett on 1/16/16.
 */
package com.clueride.service;

import com.clueride.domain.user.path.Path;

/**
 * Evaluations of Network Connectivity outside of a Geometry-dependent level.
 */
public interface NetworkEval {
    /**
     * Start at the Starting Node and walk the segment/edges in sequence to make
     * sure they end at the Ending Node.
     * @param path - Path to be evaluated.
     * @throws IllegalStateException if there is a gap in the sequence.
     */
    void checkPathEdgesFromStartToEnd(Path path);
}
