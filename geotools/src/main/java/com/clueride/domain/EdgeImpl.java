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
package com.clueride.domain;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import org.apache.log4j.Logger;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.dev.Node;
import com.clueride.domain.dev.TrackImpl;
import com.clueride.exception.UnmatchedPointException;
import com.clueride.feature.Edge;
import com.clueride.feature.FeatureType;
import com.clueride.service.NodeEval;

/**
 * Close to a TrackFeatureImpl, but this holds Network Segments before they are
 * fully rated.
 *
 * The guts of this that come from the Track/Edge set of attributes
 * is borrowed from the underlying implementation; this class takes
 * advantage of those protected fields.
 *
 * @author jett
 *
 */
public class EdgeImpl extends TrackFeatureImpl implements Edge {
    private static final Logger LOGGER = Logger.getLogger(EdgeImpl.class);

    /**
     * Members
     */
    private boolean selected;
    private Node startNode;
    private Node endNode;

    /*
     * Setup the helper instances.
     */
    private static final SimpleFeatureBuilder EDGE_FEATURE_BUILDER =
            new SimpleFeatureBuilder(FeatureType.EDGE_FEATURE_TYPE);
    private static final NodeEval NODE_EVAL = NodeEval.getInstance();

    /**
     * @param track - Details of the display name and URL.
     * @param lineString - Geometry of this Track.
     */
    public EdgeImpl(TrackImpl track, LineString lineString) {
        super(track, lineString);
    }

    /**
     * Constructor where the Feature already has the domain-specific attributes.
     *
     * This constructor mimics the one for TrackFeatureImpl except it uses the
     * EDGE_FEATURE_BUILDER.
     *
     * @param lineStringFeature holding all information we need to instantiate.
     */
    public EdgeImpl(SimpleFeature lineStringFeature) {
        super((Integer) lineStringFeature.getAttribute("edgeId"));
        this.displayName = (String) lineStringFeature.getAttribute("name");
        this.url = (String) lineStringFeature.getAttribute("url");
        this.lineString = (LineString) lineStringFeature.getDefaultGeometry();
        this.selected = false;  // Default value
        this.feature = buildFeature();
        this.startNode = findNodeForPoint(lineString.getStartPoint());
        this.endNode = findNodeForPoint(lineString.getEndPoint());
    }

    private Node findNodeForPoint(Point startPoint) {
        try {
            return NODE_EVAL.getMatchingNode(startPoint);
        } catch (UnmatchedPointException e) {
            LOGGER.error("Edge ID " + getId() + " has endpoint that is not a Node");
            // TODO: there's probably a better solution, but I don't know what it is yet.
            return null;
        }
    }

    @Override
    protected SimpleFeature buildFeature() {
        // TODO: shared instance or separate instances?
        synchronized(EDGE_FEATURE_BUILDER) {
            EDGE_FEATURE_BUILDER.add(getId());
            EDGE_FEATURE_BUILDER.add(displayName);
            EDGE_FEATURE_BUILDER.add(url);
            EDGE_FEATURE_BUILDER.add(selected);
            EDGE_FEATURE_BUILDER.add(lineString);
            return EDGE_FEATURE_BUILDER.buildFeature(null);
        }
    }

    /**
     * Stopgap sort of constructor for adjusting the coordinates of an existing Edge.
     * TODO: This can probably be improved; look at callers and make smaller.
     */
    public EdgeImpl(Edge originalEdge, LineString newLineString) {
        super(originalEdge.getId());
        this.displayName = originalEdge.getDisplayName();
        this.url = originalEdge.getUrl();
        this.lineString = newLineString;
        this.selected = false;
        this.feature = buildFeature();
    }

    /**
     * @see com.clueride.domain.dev.UnratedSegment#getStart()
     */
    @Override
    public Node getStart() {
        return startNode;
    }

    /**
     * @see com.clueride.domain.dev.UnratedSegment#getEnd()
     */
    @Override
    public Node getEnd() {
        return endNode;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EdgeImpl [getId()=").append(getId()).append(
                ", getDisplayName()=").append(getDisplayName()).append(
                ", getUrl()=").append(getUrl()).append("]");
        return builder.toString();
    }

}
