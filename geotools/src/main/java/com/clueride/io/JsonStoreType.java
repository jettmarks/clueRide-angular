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

/**
 * Enumeration of the recognized types of JSON Stores.
 *
 * These serve as keys into a few Maps:
 * <ul>
 * <li>{@link JsonStoreLocation}</li>
 * <li>{@link JsonSchemaTypeMap}</li>
 * <li>{@link JsonPrefixMap}</li>
 * </ul>
 *
 * @author jett
 */
public enum JsonStoreType {
    UNKNOWN, OTHER, BASE, SEGMENTS, RAW, EDGE, NETWORK, LOCATION, LOCATION_GROUP, PATH;
}
