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
 * Created Oct 25, 2015
 */
package com.clueride.service;

/**
 * Implements the RecIdProvider; based on MemoryBased providers.
 * 
 * These IDs are unlikely to be persisted, yet it's useful for them to have
 * separate IDs.
 *
 * @author jett
 *
 */
public class DefaultRecIdProvider implements RecIdProvider {

    private static Integer nextId = 1;

    /**
     * @see com.clueride.service.RecIdProvider#getId()
     */
    public synchronized Integer getId() {
        return nextId++;
    }

    /**
     * @see com.clueride.service.RecIdProvider#registerId(java.lang.Integer)
     */
    public void registerId(Integer id) {
        if (id > nextId) {
            nextId = id;
        }
    }

    /**
     * @see com.clueride.service.RecIdProvider#getLastId()
     */
    public int getLastId() {
        return nextId;
    }

}
