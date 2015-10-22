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
 * Created Oct 18, 2015
 */
package com.clueride.geo.score;

import com.clueride.domain.GeoNode;
import com.clueride.feature.Edge;

/**
 * For a single track, holds the results of testing how it might connect to the
 * existing network.
 * 
 * Testing is performed by business logic using GeoEval and results are recorded
 * in this instance for later interpretation by a NetworkRecommendation builder.
 *
 * The only helper methods provided are
 * <UL>
 * <LI>Whether the associated Track is connected to something or nothing.
 * <LI>Whether that connection is an Edge or not.
 * </UL>
 * 
 * @author jett
 */
public class TrackConnection {
    private Edge edge;
    private GeoNode geoNode;

    public TrackConnection(Edge edge) {
        this.edge = edge;
    }

    public TrackConnection(GeoNode geoNode) {
        this.geoNode = geoNode;
    }

    /**
     * Empty constructor indicates an instance where no network connection was
     * found.
     */
    public TrackConnection() {
    }

    /**
     * @return the edge
     */
    public Edge getEdge() {
        return edge;
    }

    /**
     * @param edge
     *            the edge to set
     */
    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    /**
     * @return the geoNode
     */
    public GeoNode getGeoNode() {
        return geoNode;
    }

    /**
     * @param geoNode
     *            the geoNode to set
     */
    public void setGeoNode(GeoNode geoNode) {
        this.geoNode = geoNode;
    }

    public boolean isConnected() {
        return (edge != null || geoNode != null);
    }

    /**
     * @return
     */
    public boolean hasEdge() {
        return edge != null;
    }
}
