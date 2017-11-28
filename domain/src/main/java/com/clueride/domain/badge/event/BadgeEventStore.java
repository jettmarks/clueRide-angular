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
 * Persistence operations for Badge Events.
 */
public interface BadgeEventStore {
    /**
     * Create a new instance of Badge Event from the Builder.
     * @param badgeEventBuilder instance of Builder for the BadgeEvent.
     * @return Unique ID of the newly created record.
     */
    Integer add(BadgeEvent.Builder badgeEventBuilder);

    /**
     * Return the matching BadgeEvent (as a Builder) given the ID.
     * @param badgeEventId unique identifier for the Badge Event.
     * @return partially populated BadgeEvent; needs other services to fill remaining fields.
     */
    BadgeEvent.Builder getById(Integer badgeEventId);

}
