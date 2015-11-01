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

import com.clueride.domain.dev.NetworkRecommendation;
import com.clueride.service.DefaultRecIdProvider;
import com.clueride.service.RecIdProvider;
import org.apache.log4j.Logger;
import org.geotools.feature.DefaultFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;

import static com.clueride.domain.dev.rec.NetworkRecType.UNDEFINED;

/**
 * Base Class of much of the Recommendation class tree.
 *
 * @author jett
 *
 */
public class NetworkRecImpl implements NetworkRecommendation {
    private static final Logger LOGGER = Logger.getLogger(NetworkRecImpl.class);

    private static final RecIdProvider ID_PROVIDER = new DefaultRecIdProvider();
    private Integer id;
    private String name;
    protected DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();

    public NetworkRecImpl() {
        id = ID_PROVIDER.getId();
    }

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

    /**
     * @see com.clueride.domain.dev.NetworkRecommendation#getFeatureCollection()
     */
    @Override
    public DefaultFeatureCollection getFeatureCollection() {
        return featureCollection;
    }

    protected void addFeature(SimpleFeature feature) {
        featureCollection.add(feature);
    }

    /**
     * Although this is expected to generally be overridden, we can return a
     * value of zero if nothing else can be used for the score.
     * 
     * TODO: There may be a better implementation of this later on.
     * 
     * @see com.clueride.domain.dev.NetworkRecommendation#getScore()
     */
    @Override
    public Double getScore() {
        return 0.0;
    }

    public void dumpRecommendationSummary() {
        int recNumber = featureCollection.toArray().length;
        LOGGER.debug("Rec " + toString() + " has " + recNumber + " features:");
        for (SimpleFeature feature : featureCollection) {
            LOGGER.debug(feature.getAttribute("name")+" ("
                    + feature.getAttribute(0) + "): "
                    + feature.getFeatureType().getTypeName());
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NetworkRecImpl [id=").append(id).append(", name=")
                .append(name).append(", getRecType()=").append(getRecType())
                .append(", getScore()=").append(getScore()).append("]");
        return builder.toString();
    }
}
