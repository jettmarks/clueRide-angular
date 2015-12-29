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
 * Created Oct 26, 2015
 */
package com.clueride.domain.dev.rec;

import com.clueride.domain.GeoNode;
import org.geotools.feature.DefaultFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Class for passing back a whole mess of Features I want to present on the map.
 *
 * @author jett
 *
 */
public class DiagnosticRec extends RecImpl {

    /** One big bucket of features. */
    private DefaultFeatureCollection features;

    /**
     * @param requestedNode
     */
    public DiagnosticRec(GeoNode requestedNode) {
        super(requestedNode);
    }

    /**
     * @see com.clueride.domain.dev.rec.NetworkRecImpl#getFeatureCollection()
     */
    @Override
    public DefaultFeatureCollection getFeatureCollection() {
        return super.getFeatureCollection();
    }

    /**
     * @see com.clueride.domain.dev.rec.NetworkRecImpl#logRecommendationSummary()
     */
    @Override
    public void logRecommendationSummary() {
        // TODO Auto-generated method stub
        super.logRecommendationSummary();
    }

    @Override
    public int getFeatureCount() {
        return features.size();
    }

    public void addFeature(SimpleFeature feature) {
        super.addFeature(feature);
    }
}
