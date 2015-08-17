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

/**
 * Maps between the Store Types and Locations on disk.
 *
 * @author jett
 *
 */
public class JsonStoreLocation {
	private static Map<JsonStoreType, String> map;

	static {
		map = new HashMap<JsonStoreType, String>();
		map.put(JsonStoreType.BASE, "/home/jett/jsonFeatures");
		map.put(JsonStoreType.SEGMENTS, "/home/jett/jsonFeatures/segments");
		map.put(JsonStoreType.RAW, "/home/jett/jsonFeatures/segments/raw");
		map.put(JsonStoreType.NETWORK,
				"/home/jett/jsonFeatures/segments/network");
		map.put(JsonStoreType.LOCATION, "/home/jett/jsonFeatures/locations");
	}

	public static String toString(JsonStoreType jsonStoreType) {
		return map.get(jsonStoreType);
	}
}
