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
 * Created Oct 4, 2015
 */
package com.clueride.domain.dev;

import java.util.List;

import com.clueride.service.EdgeIDProvider;
import com.clueride.service.MemoryBasedEdgeIDProvider;

/**
 * Primary Implementation of Track.
 *
 * @author jett
 *
 */
public class TrackImpl implements Track {

    protected Integer id;
    protected String displayName;
    protected String url;

    static EdgeIDProvider ID_PROVIDER = new MemoryBasedEdgeIDProvider();

    /**
     * Canonical constructor.
     * 
     * @param displayName - String displayed as the Name of this Track.
     * @param url - portion of RideWithGPS.com URL that is specific to this Track.
     */
    public TrackImpl(String displayName, String url) {
        this.displayName = displayName;
        this.url = url;
        this.id = ID_PROVIDER.getId();
    }

    public static void defineIdProvider(EdgeIDProvider idProvider) {
        ID_PROVIDER = idProvider;
    }

    /**
     * @see com.clueride.domain.dev.Track#getId()
     */
    public Integer getId() {
        return id;
    }

    /**
     * @see com.clueride.domain.dev.Track#getDisplayName()
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @see com.clueride.domain.dev.Track#setDisplayName(java.lang.String)
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @see com.clueride.domain.dev.Track#getUrl()
     */
    public String getUrl() {
        return url;
    }

    /**
     * TODO: waiting to see if I'll actually use a Node List that comes from
     * this instance.
     * 
     * For the most part, this object will be used to communicate about the
     * Track and the TrackFeatureImpl will be the one used for mapping and other
     * Geometry based work.
     * 
     * @see com.clueride.domain.dev.Track#getNodeList()
     */
    public List<Node> getNodeList() {
        // TODO Auto-generated method stub
        return null;
    }

}
