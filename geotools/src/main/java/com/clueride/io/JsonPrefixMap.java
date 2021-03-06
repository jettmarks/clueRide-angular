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
 * Created by jett on 1/17/16.
 */
package com.clueride.io;

import java.util.HashMap;
import java.util.Map;

/**
 * Relationship between a Json Store and the file prefix used to persist.
 */
public class JsonPrefixMap {
    private static Map<JsonStoreType, String> prefixMap;

    static {
        prefixMap = new HashMap<>();
        prefixMap.put(JsonStoreType.LOCATION, "loc");
        prefixMap.put(JsonStoreType.PATH, "path");
        prefixMap.put(JsonStoreType.COURSE, "course");
        prefixMap.put(JsonStoreType.COURSE_TYPE, "courseType");
        prefixMap.put(JsonStoreType.CLUE, "clue");
        prefixMap.put(JsonStoreType.INVITATION, "invite");
        prefixMap.put(JsonStoreType.MEMBER, "member");
        prefixMap.put(JsonStoreType.OUTING, "outing");
    }

    public static String toString(JsonStoreType storeType) {
        return prefixMap.get(storeType);
    }
}
