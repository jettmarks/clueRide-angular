/*
 * Copyright 2017 Jett Marks
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
 * Created by jett on 8/1/17.
 */
package com.clueride.token;

/**
 * Provides, persists and verifies jti claims against a JWT token.
 */
public interface JtiService {
    /**
     * Creates and persists a random ID used to avoid replaying someone else's token.
     * @return random String that we remember.
     */
    String registerNewId();

    /**
     * The given ID has expired and is no longer needed.
     * @param jtiId to be removed from the store.
     */
    void retireId(String jtiId);

    /**
     * Throws exception if the given ID isn't found in the store.
     * @param jtiId
     */
    void validateId(String jtiId);

}
