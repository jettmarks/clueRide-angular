package com.clueride.service;

/**
 * Copyright 2015 Jett Marks
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Created by jett on 11/24/15.
 */
public interface IdProvider {
    /**
     * Whenever the Store asks for a new Id, services which implement this interface
     * will be invoked to obtain that ID.
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