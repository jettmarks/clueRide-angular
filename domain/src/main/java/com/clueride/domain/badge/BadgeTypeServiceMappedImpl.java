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
 * Created by jett on 8/27/18.
 */
package com.clueride.domain.badge;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation that "hard-codes" the relationships between certain Post IDs
 * and the corresponding BadgeType.
 */
public class BadgeTypeServiceMappedImpl implements BadgeTypeService {
    private static Map<Integer,BadgeType> badgeTypeMap = new HashMap<>();

    static {
        /* These IDs are obtained from records in the `badge_brief` view. */
        badgeTypeMap.put(3376, BadgeType.SEEKER);
        badgeTypeMap.put(3391, BadgeType.SEEKER);
        badgeTypeMap.put(3444, BadgeType.GUIDE);
    }

    /**
     * Temporary Implementation that returns Guide when the badge isn't recognized.
     * @param badge instance populated from source that doesn't know about ClueRide's Badge Types.
     * @return Type of Badge for authorization.
     */
    @Override
    public BadgeType getTypeOfBadge(Badge.Builder badge) {
        Integer badgeId = badge.getId();
        if (badgeTypeMap.containsKey(badgeId)) {
            return badgeTypeMap.get(badgeId);
        } else {
            return BadgeType.GUIDE;
        }
    }

}
