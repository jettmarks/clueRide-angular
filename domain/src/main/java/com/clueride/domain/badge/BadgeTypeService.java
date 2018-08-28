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

/**
 * Maps from a Badge to a Badge Type.
 */
public interface BadgeTypeService {

    /**
     * Given a Badge, determine the BadgeType.
     * @param builder instance of Builder populated from source that doesn't know about ClueRide's Badge Types.
     * @return Enumeration of the BadgeType which provides authorization for certain activities.
     */
    BadgeType getTypeOfBadge(Badge.Builder builder);

}
