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

import com.clueride.domain.GeoNode;
import com.clueride.geo.TranslateUtil;

/**
 * Implementation of a NetworkRecommendation based on a new Location, but since
 * this is the primary one at this time, we're getting away with just calling it
 * 'Rec' instead of 'NewLocRec'.
 *
 * @author jett
 *
 */
public abstract class RecImpl extends NetworkRecImpl implements Rec {
    private GeoNode newLoc;

    /**
     * Should only be called by subclasses.
     * 
     * @param requestedNode
     */
    protected RecImpl(GeoNode requestedNode) {
        super();
        this.newLoc = requestedNode;
        super.addFeature(TranslateUtil.geoNodeToFeature(requestedNode));
    }

    /**
     * @see com.clueride.domain.dev.rec.Rec#getNewLoc()
     */
    @Override
    public GeoNode getNewLoc() {
        return newLoc;
    };
}
