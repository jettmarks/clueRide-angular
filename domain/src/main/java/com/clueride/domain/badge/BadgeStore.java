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
 * Created by jett on 8/26/18.
 */
package com.clueride.domain.badge;

import java.util.List;

/**
 * Defines the DAO for Badges.
 *
 * Badges are created and maintained within the BadgeOS system and for that reason,
 * when we read Badges from the database, we're reading from the BadgeOS / WordPress
 * database.
 */
public interface BadgeStore {
    /**
     * Retrieves list of currently awarded badges for the session's user.
     * @return List of Badges for display.
     */
    List<Badge> getAwardedBadgesForUser();

}
