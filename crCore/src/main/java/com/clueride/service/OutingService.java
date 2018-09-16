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
 * Created by jett on 3/5/16.
 */
package com.clueride.service;

import com.clueride.domain.Outing;
import com.clueride.domain.Team;
import com.clueride.domain.course.Course;
import com.clueride.rest.dto.OutingState;

/**
 * Service supporting the Outing Model which ties together a particular {@link Team}
 * on a particular {@link Course} at a scheduled time.
 */
public interface OutingService {
    /**
     * Given the ID, return the matching Outing.
     * @param outingId - Unique Identifier for the Outing.
     * @return the Outing.
     */
    Outing getByOutingId(Integer outingId);

    /**
     * Given the Team's ID, return the matching Outing or throw an exception if
     * the Team isn't found.
     * @param teamId - Unique identifier for the Team.
     * @return the Outing.
     */
    Outing getByTeamId(Integer teamId);

    /**
     * Given a completely populated Outing, store and index it in the Store.
     * @param outing - Instance to add to the list of outings.
     * @return - Integer ID of the new outing.
     */
    Outing createOuting(Outing outing);

    /**
     * Given an Outing, replace any matching existing Outing with this instance.
     * This will be more useful for Outings which have already been advertised,
     * announced or otherwise made public and now have changes in either the
     * Course or the Scheduled Time.
     * TODO: This would be more clear if it accepted a Builder and returned an Immutable instance carrying the Integer.
     * @param outing - Outing instance whose details have changed.
     */
    Integer updateOuting(Outing outing);

    /**
     * Given the ID of the Outing, return the Long timestamp of the last State Change.
     * @param outingId - Unique identifier of the Outing.
     * @return - Long representing the Java-time when last update has occurred.
     */
    Long lastStateChange(Integer outingId);

    /**
     * Given the ID of the Outing, return the Long timestamp of the last Position Change.
     * @param outingId - Unique identifier of the Outing.
     * @return - Long representing the Java-time when last change in position has occurred;
     * zero if the Outing isn't yet active.
     */
    Long lastPositionChange(Integer outingId);

    /**
     * Update the State timestamp to the current time on the given Outing.
     * @param outingId - Unique identifier of the Outing.
     */
    void updateState(Integer outingId);

    /**
     * Update the timestamp of the most recent position.
     * GPS service is used to request the most recent position.
     * @param outingId - Unique identifier of the Outing.
     */
    void updatePosition(Integer outingId);

    /**
     * Given the Outing ID, return the state of that Outing.
     * @param outingId - Unique identifier of the Outing.
     * @return - Current instance of Outing State.
     */
    OutingState getState(Integer outingId);

    /**
     * Given the Outing ID, return the state of that Outing.
     * @param outingState - Updated instance of the state, typically a result of an event coming in.
     * @return - Long timestamp of the updated state.
     */
    Long updateOutingState(OutingState outingState);
}
