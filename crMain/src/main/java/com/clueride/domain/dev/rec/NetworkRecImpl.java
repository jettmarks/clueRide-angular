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
 * Created Sep 28, 2015
 */
package com.clueride.domain.dev.rec;

import static com.clueride.domain.dev.rec.NetworkRecType.UNDEFINED;

import com.clueride.domain.dev.NetworkRecommendation;

/**
 * Base Class of much of the Recommendation class tree.
 *
 * @author jett
 *
 */
public abstract class NetworkRecImpl implements NetworkRecommendation {

    private Integer id;
    private String name;

    /**
     * @see com.clueride.domain.dev.NetworkRecommendation#getId()
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * @see com.clueride.domain.dev.NetworkRecommendation#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @see com.clueride.domain.dev.NetworkRecommendation#getRecType()
     */
    @Override
    public NetworkRecType getRecType() {
        return UNDEFINED;
    }
}
