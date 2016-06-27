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
 * Created Aug 22, 2015
 */
package com.clueride.domain;

import java.util.List;

import com.vividsolutions.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.dev.Node;
import com.clueride.feature.Edge;
import com.clueride.feature.TrackFeature;

/**
 * Refinement of Node that knows about the GeoTools objects.
 *
 * @author jett
 *
 */
public interface GeoNode extends Node {

    /**
     * @return
     */
    Integer getId();

    void setId(Integer pointId);

    String getName();

    void setName(String name);

    /**
     * @return
     */
    Point getPoint();

    /**
     * @param point
     */
    void setPoint(Point point);

    Double getLon();

    Double getLat();

    Double getElevation();

    /**
     * @deprecated
     */
    void setNearByNodes(List<GeoNode> nearestNodes);

    /**
     * @deprecated
     */
    List<GeoNode> getNearByNodes();

    /**
     * @deprecated
     */
    int getNearByNodeCount();

    /**
     * @deprecated
     */
    int getTrackCount();

    /**
     * @param trackFeature
     * @deprecated
     */
    void addTrack(TrackFeature trackFeature);

    /**
     * @deprecated
     */
    List<SimpleFeature> getTracks();

    /**
     * @param set
     */
    void setSelectedNode(GeoNode node);

    GeoNode getSelectedNode();

    /**
     * @return
     */
    Boolean isSelected();

    /**
     * 
     * @deprecated
     */
    void setSelected(Boolean selected);

    /**
     * @param trackFeature
     * @deprecated
     */
    void setProposedTrack(TrackFeature trackFeature);

    /**
     * Holds the recommended track from this point to the network using some
     * portion of a Track.
     * 
     * @return
     * @deprecated
     */
    Edge getProposedTrack();

    /**
     * Evaluation of whether or not we have a proposed track.
     * 
     * @return
     * @deprecated
     */
    boolean hasProposedTrack();

    /**
     * @param proposedNode
     * @deprecated
     */
    void addScoredNode(GeoNode proposedNode);
}
