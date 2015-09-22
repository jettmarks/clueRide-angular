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
 * Created Sep 1, 2015
 */
package com.clueride.config;

import java.util.Hashtable;
import java.util.Properties;

/**
 * Mix of configured constants and hardcoded constants until I see a reason to
 * break this out further.
 *
 * @author jett
 *
 */
public class GeoProperties extends Properties {
    /**  */
    private static final long serialVersionUID = 3618933981252565451L;

    static GeoProperties instance = new GeoProperties();

    public static final double BUFFER_TOLERANCE = 0.00007;

    static {
        instance.put("group.radius", 50.0);
        instance.put("group.radius.degrees", 0.00056);
    }

    /**
     * @return
     */
    public static Hashtable<Object, Object> getInstance() {
        return instance;
    }
}
