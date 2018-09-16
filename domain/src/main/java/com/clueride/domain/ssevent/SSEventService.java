/*
 * Copyright 2018 Jett Marks
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
 * Created by jett on 9/15/18.
 */
package com.clueride.domain.ssevent;

/**
 * Handles the generation and propagation of Events from the Game's point of view.
 *
 * Implementations will call into the Server-Sent Event Server to actuall perform the
 * broadcast to subscribers.
 */
public interface SSEventService {
    /**
     * After the team is assembled and ready to play, this is the signal
     * we're beginning the game.
     *
     * @param outingId Unique identifier for the Outing that is beginning play; indicates which channel is updated.
     * @return ID of the message.
     */
    Integer sendTeamReadyEvent(Integer outingId);

    Integer sendArrivalEvent(Integer outingId);

    Integer sendDepartureEvent(Integer outingId);

}
