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
 * Created by jett on 1/15/16.
 */
package com.clueride.domain;

import java.util.List;

import com.clueride.domain.user.location.Location;
import com.clueride.domain.user.path.Path;

/**
 * Representation of a Course, including an ordered list of Locations and the Paths
 * which move from one Location to the next.
 */
public interface Course {
    Integer getId();
    String getName();
    String getDescription();
    List<Integer> getPathIds();
    Integer getCourseTypeId();

//    Location getDeparture();
//    Location getDestination();

    Step nextStep();
    Step currentStep();

    /**
     * As progress is made along the course, each step is completed by calling
     * this method.
     * For example, upon departure from the first {@link Location}, that step is completed
     * and the state changes to the first {@link Path}.
     * @return The last current step which we've marked as completed (giving us a new current Step).
     */
    Step completeCurrentStep();

//    List<Step> getSteps();
}
