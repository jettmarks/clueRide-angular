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
 * Created Oct 5, 2015
 */
package com.clueride.service;

/**
 * Support for populating the private final ID for instances of TrackImpl and
 * its derivatives.
 * 
 * @author jett
 *
 */
public interface EdgeIDProvider {
    /**
     * Whenever the Constructors for TrackImpl and derivatives ask for a new ID,
     * this service will be invoked to obtain that ID.
     * 
     * @return
     */
    Integer getId();

    /**
     * Only used by providers that handle pre-assigned IDs.
     * 
     * @param id
     */
    void registerId(Integer id);

    /**
     * Provides the current state; what is the most recently provided ID?
     * 
     * @return
     */
    int getLastId();

}
