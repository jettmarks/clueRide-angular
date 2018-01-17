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
 * Created by jett on 1/16/18.
 */
package com.clueride.domain.badge;

import java.util.List;

import com.clueride.domain.user.Badge;

/**
 * Defines what operations are provided for Badges.
 */
public interface BadgeService {

    /**
     * Based on the user's session, return the list of badges accumulated
     * by this user.
     * @return List of user's Badges.
     */
    List<Badge> getBadges();
}
