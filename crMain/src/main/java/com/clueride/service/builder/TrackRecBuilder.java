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
 * Created Oct 21, 2015
 */
package com.clueride.service.builder;

import org.apache.log4j.Logger;

import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.rec.Rec;
import com.clueride.domain.dev.rec.ToNodeImpl;
import com.clueride.domain.dev.rec.ToSegmentAndNodeImpl;
import com.clueride.domain.dev.rec.ToSegmentImpl;
import com.clueride.domain.dev.rec.ToTwoNodesImpl;
import com.clueride.domain.dev.rec.ToTwoSegmentsImpl;
import com.clueride.feature.Edge;
import com.clueride.feature.LineFeature;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.SplitLineString;

/**
 * Given a Node and the Tracks covering that node, prepare a set of track-based
 * recommendations we can add to the NetworkProposal.
 *
 * @author jett
 *
 */
public class TrackRecBuilder {
    private static final Logger LOGGER = Logger
            .getLogger(TrackRecBuilder.class);

    private final GeoNode newLoc;
    private final TrackFeature track;
    private final SplitLineString splitLineString;

    private GeoNode networkNodeStart;
    private GeoNode networkNodeEnd;
    private Edge networkEdgeEnd;
    private Edge networkEdgeStart;
    private boolean preferEdgeStart;
    private boolean preferEdgeEnd;

    private LineFeature trackStart;
    private LineFeature trackEnd;

    private int nodeStartPointCount;

    private int nodeEndPointCount;

    private int edgeStartPointCount;

    private int edgeEndPointCount;

    private Double startDistance;
    private Double endDistance;

    private DefaultGeoNode splittingNodeStart;

    private DefaultGeoNode splittingNodeEnd;

    /**
     * @param geoNode
     * @param coveringTracks
     */
    public TrackRecBuilder(GeoNode newLoc, TrackFeature track) {
        this.newLoc = newLoc;
        this.track = track;
        this.splitLineString = new SplitLineString(track, newLoc);
        LOGGER.debug("Preparing Track Rec for " + track);
    }

    /**
     * Accepts a Node in the Network that we have verified is covered by the
     * "To Start" portion of the Track we're building a recommendation for.
     * 
     * Based on this Node (if not null), we can calculate the Edge suggested by
     * the Track that runs from the newLoc and this potential Network Node. If
     * we get a closer Segment, we would recommend that instead.
     * 
     * @param networkNode
     *            - represents shortest connection of the Track to the Network.
     * @return this - to allow chaining of the builder.
     */
    public TrackRecBuilder addNetworkNodeStart(GeoNode networkNode) {
        this.networkNodeStart = networkNode;
        return this;
    }

    /**
     * Accepts a Node in the Network that we have verified is covered by the
     * "To End" portion of the Track we're building a recommendation for.
     * 
     * Based on this Node (if not null), we can calculate the Edge suggested by
     * the Track that runs from the newLoc and this potential Network Node. If
     * we get a closer Segment, we would recommend that instead.
     * 
     * @param networkNode
     *            - represents shortest connection of the Track to the Network.
     * @return this - to allow chaining of the builder.
     */
    public TrackRecBuilder addNetworkNodeEnd(GeoNode networkNode) {
        // Record this now, not yet prepared to calculate distances or Edges.
        this.networkNodeEnd = networkNode;
        return this;
    }

    /**
     * @param nearestNetworkEdge
     * @return
     */
    public TrackRecBuilder addEdgeAtStart(Edge networkEdge) {
        // Record this now, not yet prepared to calculate distances or Edges.
        this.networkEdgeStart = networkEdge;
        return this;
    }

    /**
     * @param nearestNetworkEdge
     */
    public TrackRecBuilder addEdgeAtEnd(Edge networkEdge) {
        // Record this now, not yet prepared to calculate distances or Edges.
        this.networkEdgeEnd = networkEdge;
        return this;
    }

    /**
     * @return
     */
    public Rec build() {
        // TODO Auto-generated method stub
        switch (numberOfConnections()) {
        case 2:
            populateProposedTracks();
            prepareEnds();
            LOGGER.info("Track " + track + " connected both ends");
            if (networkNodeStart != null) {
                if (networkNodeEnd != null) {
                    return new ToTwoNodesImpl(newLoc, track, networkNodeStart,
                            networkNodeEnd);
                } else {
                    return new ToSegmentAndNodeImpl(newLoc, track,
                            networkEdgeEnd, splittingNodeEnd, networkNodeStart);
                }
            } else {
                // networkEdgeStart != null
                if (networkNodeEnd != null) {
                    return new ToSegmentAndNodeImpl(newLoc, track,
                            networkEdgeStart, splittingNodeStart,
                            networkNodeEnd);
                } else {
                    return new ToTwoSegmentsImpl(newLoc, track,
                            networkEdgeStart,
                            networkEdgeEnd,
                            splittingNodeStart,
                            splittingNodeEnd);
                }
            }
            // no need to break
        case 1:
            LOGGER.info("Track " + track + " connected on single end");
            populateProposedTracks();
            prepareEnds();

            // Order based on which one is closer for this end
            if (preferEdgeStart) {
                if (networkEdgeStart != null) {
                    // LineString lsStart = (LineString)
                    // trackStart.getGeometry();
                    // GeoNode splittingNode = new DefaultGeoNode();
                    // splittingNode.setPoint(lsStart.getEndPoint());
                    return new ToSegmentImpl(newLoc, trackStart,
                            networkEdgeStart,
                            splittingNodeStart);
                }
                if (networkNodeStart != null) {
                    return new ToNodeImpl(newLoc, trackStart, networkNodeStart);
                }
            } else {
                if (networkNodeStart != null) {
                    return new ToNodeImpl(newLoc, trackStart, networkNodeStart);
                }
                if (networkEdgeStart != null) {
                    return new ToSegmentImpl(newLoc, trackStart,
                            networkEdgeStart,
                            splittingNodeStart);
                }
            }

            // Order based on which one is closer for this end
            if (preferEdgeEnd) {
                if (networkEdgeEnd != null) {
                    return new ToSegmentImpl(newLoc, trackEnd, networkEdgeEnd,
                            splittingNodeEnd);
                }
                if (networkNodeEnd != null) {
                    return new ToNodeImpl(newLoc, trackEnd, networkNodeEnd);
                }
            } else {
                if (networkNodeEnd != null) {
                    return new ToNodeImpl(newLoc, trackEnd, networkNodeEnd);
                }
                if (networkEdgeEnd != null) {
                    return new ToSegmentImpl(newLoc, trackEnd, networkEdgeEnd,
                            splittingNodeEnd);
                }
            }
            break;
        case 0:
        default:
            LOGGER.info("Track " + track.getId() + " unconnected");
            break;
        }
        return null;
    }

    /**
     * 
     */
    private void prepareEnds() {
        boolean startHasChoice = networkNodeStart != null
                && networkEdgeStart != null;
        boolean endHasChoice = networkNodeEnd != null && networkEdgeEnd != null;

        if (startHasChoice) {
            preferEdgeStart = edgeStartPointCount < nodeStartPointCount;
        } else {
            preferEdgeStart = networkEdgeStart != null;
        }

        if (endHasChoice) {
            preferEdgeEnd = edgeEndPointCount < nodeEndPointCount;
        } else {
            preferEdgeEnd = networkEdgeEnd != null;
        }
    }

    /**
     * Only prepare these LineFeatures if we know we'll be using them.
     */
    private void populateProposedTracks() {
        trackStart = splitLineString.getLineFeatureToStart();
        trackEnd = splitLineString.getLineFeatureToEnd();
    }

    // TODO: Simplify this by recording when we set either the start or the end.
    private int numberOfConnections() {
        int connectionCount = 0;
        if (hasStartConnection()) {
            connectionCount++;
        }
        if (hasEndConnection()) {
            connectionCount++;
        }
        return connectionCount;
    }

    public boolean hasStartConnection() {
        return networkNodeStart != null || networkEdgeStart != null;
    }

    public boolean hasEndConnection() {
        return networkNodeEnd != null || networkEdgeEnd != null;
    }

    /**
     * @param nodeDistance
     */
    public TrackRecBuilder addStartDistance(Double distance) {
        this.startDistance = distance;
        return this;
    }

    /**
     * @param splittingNode
     * @return
     */
    public TrackRecBuilder addSplittingNodeAtStart(DefaultGeoNode splittingNode) {
        this.splittingNodeStart = splittingNode;
        return this;
    }

    /**
     * @param splittingNode
     * @return
     */
    public TrackRecBuilder addSplittingNodeAtEnd(DefaultGeoNode splittingNode) {
        this.splittingNodeEnd = splittingNode;
        return this;
    }

    /**
     * @param nodeDistance
     */
    public TrackRecBuilder addEndDistance(Double distance) {
        this.endDistance = distance;
        return this;
    }
}
