/**
 *   Copyright 2015 Jett Marks
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created Aug 16, 2015
 */
package com.clueride.io;

import java.util.HashMap;
import java.util.Map;

import com.clueride.config.TestModeAware;

/**
 * Maps between the Store Types and Locations on disk.
 *
 * @author jett
 *
 */
public class JsonStoreLocation implements TestModeAware {
    private static Map<JsonStoreType, String> map;
    private static Map<JsonStoreType, String> testMap;
    private static boolean testMode = false;

    static {
        map = new HashMap<>();
        map.put(JsonStoreType.BASE, "/home/jett/jsonFeatures");
        map.put(JsonStoreType.SEGMENTS, "/home/jett/jsonFeatures/segments");
        map.put(JsonStoreType.RAW, "/home/jett/jsonFeatures/segments/raw");
        map.put(JsonStoreType.EDGE, "/home/jett/jsonFeatures/segments/edge");
        map.put(JsonStoreType.PATH, "/home/jett/jsonFeatures/paths");
        map.put(JsonStoreType.COURSE, "/home/jett/jsonFeatures/courses");
        map.put(JsonStoreType.INVITATION, "/home/jett/jsonFeatures/invitations");
        map.put(JsonStoreType.MEMBER, "/home/jett/jsonFeatures/members");
        map.put(JsonStoreType.NETWORK,
                "/home/jett/jsonFeatures/segments/network");
        map.put(JsonStoreType.LOCATION, "/home/jett/jsonFeatures/locations");
        map.put(JsonStoreType.CLUE, "/home/jett/jsonFeatures/clues");
        map.put(JsonStoreType.OTHER, "/home/jett/jsonFeatures/other");

        testMap = new HashMap<>();
        testMap.put(JsonStoreType.BASE, "/home/jett/jsonFeatures-test");
        testMap.put(JsonStoreType.SEGMENTS,
                "/home/jett/jsonFeatures-test/segments");
        testMap.put(JsonStoreType.RAW,
                "/home/jett/jsonFeatures-test/segments/raw");
        testMap.put(JsonStoreType.EDGE,
                "/home/jett/jsonFeatures-test/segments/edge");
        testMap.put(JsonStoreType.PATH, "/home/jett/jsonFeatures-test/paths");
        testMap.put(JsonStoreType.COURSE, "/home/jett/jsonFeatures-test/courses");
        testMap.put(JsonStoreType.INVITATION, "/home/jett/jsonFeatures-test/invitations");
        testMap.put(JsonStoreType.MEMBER, "/home/jett/jsonFeatures-test/members");
        testMap.put(JsonStoreType.NETWORK,
                "/home/jett/jsonFeatures-test/segments/network");
        testMap.put(JsonStoreType.LOCATION,
                "/home/jett/jsonFeatures-test/locations");
        testMap.put(JsonStoreType.CLUE,
                "/home/jett/jsonFeatures-test/clues");
        testMap.put(JsonStoreType.OTHER, "/home/jett/jsonFeatures-test/other");

    }

    public static String toString(JsonStoreType jsonStoreType) {
        if (testMode) {
            return testMap.get(jsonStoreType);
        } else {
            return map.get(jsonStoreType);
        }
    }

    public static void clearTestMode() {
        testMode = false;
    }

    public static void setTestMode() {
        testMode = true;
    }

    /**
     * TODO: Either convert to a static method, or figure out a better way to
     * get this in the state we want.
     * 
     * @see com.clueride.config.TestModeAware#setTestMode(boolean)
     */
    @Override
    public void setTestMode(boolean isInTest) {
        if (isInTest) {
            setTestMode();
        } else {
            clearTestMode();
        }
    }

    /**
     * @see com.clueride.config.TestModeAware#isTestMode()
     */
    @Override
    public boolean isTestMode() {
        return testMode;
    }
}
