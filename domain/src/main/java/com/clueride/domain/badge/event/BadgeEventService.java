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
 * Created by jett on 11/25/17.
 */
package com.clueride.domain.badge.event;

/**
 * Operations on Badge Events.
 */
public interface BadgeEventService {
    /**
     * Passes captured Badge Event to the service thread that persists the event.
     * @param badgeEvent instance of captured Badge Event.
     */
    void send(BadgeEvent.Builder badgeEvent);

    /**
     * Given the unique identifier for a Badge Event, retrieve that badge event.
     * @param badgeEventId unique identifier for the Badge Event.
     * @return fully-populated Badge Event.
     */
    BadgeEvent getBadgeEventById(Integer badgeEventId);

}
