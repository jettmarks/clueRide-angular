/*
 * Copyright 2018 Jett Marks
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
 * Created by jett on 8/31/18.
 */
package com.clueride.exc;

/**
 * Turns MalformedUrlException into a RuntimeException for the case of URLs
 * mistakenly placed in the DB that don't make sense.
 */
public class MalformedUrlWithinDBException extends RuntimeException {

    public MalformedUrlWithinDBException(Exception e) {
        super(e);
    }

}
