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
 * Created Oct 6, 2015
 */
package com.clueride.dao;

import com.clueride.service.EdgeIDProvider;

/**
 * Implementation of EdgeIDProvider that takes into account what's happening in
 * the space of pre-assigned IDs.
 *
 * @author jett
 */
public class DataBasedEdgeIDProvider implements EdgeIDProvider {

    private static Integer lastId = 0;

    /**
     * @see com.clueride.service.EdgeIDProvider#getId()
     */
    @Override
    public Integer getId() {
        lastId++;
        return lastId;
    }

    /**
     * Any existing ID is registered here so we know which ones can be handed
     * out.
     * 
     * @param id
     */
    public void registerId(Integer id) {
        // Simple implementation is to keep increasing the number
        if (id > lastId) {
            lastId = id;
        }
    }

    /**
     * @see com.clueride.service.EdgeIDProvider#getLastId()
     */
    @Override
    public int getLastId() {
        return lastId;
    }

}
