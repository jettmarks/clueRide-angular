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
 * Created Aug 15, 2015
 */
package com.clueride.domain;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Point;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.dev.Node;
import com.clueride.domain.dev.NodeNetworkState;
import com.clueride.domain.dev.UnratedSegment;
import com.clueride.feature.Edge;
import com.clueride.feature.TrackFeature;

/**
 * Description.
 * 
 * TODO: Needs to take advantage of the Recommendation Structure to get rid of a
 * lot of the baggage being carried around by this implementation class.
 *
 * @author jett
 *
 */
public class DefaultGeoNode implements GeoNode {
    protected Point point;
    private Integer id;
    private String name;
    private NodeNetworkState nodeNetworkState = NodeNetworkState.UNDEFINED;
    private List<UnratedSegment> segments = new ArrayList<>();
    private List<GeoNode> nearByNodes = new ArrayList<>();
    private List<SimpleFeature> tracks = new ArrayList<>();
    private GeoNode selectedNode;
    private Boolean selected = false;
    private Edge proposedSegment;

    /**
     * @param point - Supplies coordinates using this Geometry instance.
     */
    public DefaultGeoNode(Point point) {
        this.setPoint(point);
    }

    /**
     * 
     */
    public DefaultGeoNode() {
        // TODO Auto-generated constructor stub -- Do we use a no-arg constructor anywhere?  Why?
    }

    /**
     * @return the point
     */
    public Point getPoint() {
        return point;
    }

    /**
     * @param point
     *            the point to set
     */
    public void setPoint(Point point) {
        this.point = point;
    }

    /**
     * Checks that the underlying coordinates are the same for each instance.
     * 
     * @see com.clueride.domain.dev.Node#matchesLocation(com.clueride.domain.dev.Node)
     */
    public boolean matchesLocation(Node node) {
        if (node instanceof DefaultGeoNode) {
            return (this.point.getCoordinate().equals2D(((GeoNode) node)
                    .getPoint().getCoordinate()));
        } else {
            throw new IllegalArgumentException(
                    "Expected DefaultGeoNode implementation, not "
                            + node.getClass());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.clueride.domain.dev.Node#getState()
     */
    public NodeNetworkState getState() {
        return nodeNetworkState;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.clueride.domain.dev.Node#getSegments()
     */
    public List<UnratedSegment> getSegments() {
        return segments;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.clueride.domain.dev.Node#getTracks()
     */
    public List<SimpleFeature> getTracks() {
        return tracks;
    }

    public Double getLat() {
        return getPoint().getY();
    }

    public Double getLon() {
        return getPoint().getX();
    }

    public Double getElevation() {
        return getPoint().getCoordinates()[0].z;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(getPoint()
                .getCoordinateSequence());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        GeoNode rhs = (GeoNode) obj;
        return (this.getLat().equals(rhs.getLat()) && this.getLon().equals(
                rhs.getLon()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.clueride.domain.dev.Node#setState(com.clueride.domain.dev.
     * NodeNetworkState)
     */
    public void setState(NodeNetworkState nodeNetworkState) {
        this.nodeNetworkState = nodeNetworkState;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.clueride.domain.dev.Node#addSegment(com.clueride.domain.dev.Segment)
     */
    public void addSegment(Edge segment) {
        segments.add((UnratedSegment) segment);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.clueride.domain.GeoNode#setNearbyNodes(java.util.Set)
     */
    @Override
    public void setNearByNodes(List<GeoNode> nearestNodes) {
        nearByNodes = nearestNodes;
    }

    /**
     * @see com.clueride.domain.GeoNode#getNearByNodes()
     */
    @Override
    public List<GeoNode> getNearByNodes() {
        return nearByNodes;
    }

    /**
     * @deprecated - should be part of the NetworkProposal instead.
     */
    public void addTrack(SimpleFeature feature) {
        tracks.add(feature);
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @see com.clueride.domain.GeoNode#getNearByNodeCount()
     */
    @Override
    public int getNearByNodeCount() {
        return nearByNodes.isEmpty() ? 0 : nearByNodes.size();
    }

    /**
     * @see com.clueride.domain.GeoNode#getTrackCount()
     */
    @Override
    public int getTrackCount() {
        return tracks.isEmpty() ? 0 : tracks.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DefaultGeoNode [id=" + id + (selected ? " POINT=" : " point=")
                + point + "@"
                + getElevation() + ": "
                + getState()
                + " NbN: " + getNearByNodeCount()
                + " Tk: " + getTrackCount()
                + " Prop?: " + hasProposedTrack() + "]";
    }

    /**
     * @see com.clueride.domain.GeoNode#setSelectedNode(com.clueride.domain.GeoNode)
     */
    @Override
    public void setSelectedNode(GeoNode node) {
        selectedNode = node;
        selectedNode.setSelected(true);
    }

    /**
     * @see com.clueride.domain.GeoNode#getSelectedNode()
     */
    @Override
    public GeoNode getSelectedNode() {
        return selectedNode;
    }

    /**
     * @see com.clueride.domain.GeoNode#isSelected()
     */
    @Override
    public Boolean isSelected() {
        return selected;
    }

    /**
     * @see com.clueride.domain.GeoNode#setSelected(java.lang.Boolean)
     */
    @Override
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    /**
     * @return the proposedSegment
     */
    public Edge getProposedTrack() {
        return proposedSegment;
    }

    /**
     * @deprecated - should be part of the NetworkProposal instead.
     */
    public void setProposedSegment(Edge proposedSegment) {
        this.proposedSegment = proposedSegment;
    }

    /**
     * @see com.clueride.domain.GeoNode#hasProposedTrack()
     */
    @Override
    public boolean hasProposedTrack() {
        return (this.proposedSegment != null);
    }

    /**
     * @see com.clueride.domain.GeoNode#addScoredNode(com.clueride.domain.GeoNode)
     */
    @Override
    public void addScoredNode(GeoNode proposedNode) {
        this.nearByNodes.add(proposedNode);
    }

    /**
     * @see com.clueride.domain.dev.Node#addSegment(com.clueride.domain.dev.UnratedSegment)
     */
    @Override
    public void addSegment(UnratedSegment segment) {
        // TODO Auto-generated method stub

    }

    /**
     * @see com.clueride.domain.GeoNode#addTrack(com.clueride.feature.TrackFeature)
     */
    @Override
    public void addTrack(TrackFeature trackFeature) {
        // TODO Auto-generated method stub

    }

    /**
     * @see com.clueride.domain.GeoNode#setProposedTrack(com.clueride.feature.TrackFeature)
     */
    @Override
    public void setProposedTrack(TrackFeature trackFeature) {
        // TODO Auto-generated method stub

    }
}
