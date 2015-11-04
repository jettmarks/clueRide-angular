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

import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.rec.*;
import com.clueride.feature.Edge;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.SplitLineString;
import com.clueride.geo.TranslateUtil;
import com.clueride.service.TrackEval;
import org.apache.log4j.Logger;
import org.opengis.feature.simple.SimpleFeature;

import java.util.ArrayList;
import java.util.List;

import static com.clueride.geo.SplitLineString.END;
import static com.clueride.geo.SplitLineString.START;

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
//    private final SplitLineString splitLineString;

    private GeoNode networkNodeStart;
    private GeoNode networkNodeEnd;
    private Edge networkEdgeEnd;
    private Edge networkEdgeStart;
    private boolean preferEdgeStart;
    private boolean preferEdgeEnd;

    private TrackFeature trackStart;
    private TrackFeature trackEnd;

    private int nodeStartPointCount;

    private int nodeEndPointCount;

    private int edgeStartPointCount;

    private int edgeEndPointCount;

    private Double startDistance;
    private Double endDistance;

    private DefaultGeoNode splittingNodeStart;

    private DefaultGeoNode splittingNodeEnd;

    private boolean diagnosticMode = false;

    private TrackEval[] trackEvalArray = new TrackEval[2];
    private List<TrackEval> trackEvals = new ArrayList<>();

    /**
     * @param newLoc is the GeoNode we're adding to the network.
     * @param track is the Track that could get us onto the network.
     */
    public TrackRecBuilder(GeoNode newLoc, TrackFeature track) {
        LOGGER.debug("Preparing Track Rec for " + track);
        this.newLoc = newLoc;
        this.track = track;

        // Two ends to be evaluated for this single Track
        SplitLineString splitLineString = new SplitLineString(track, newLoc);
        for (int i = START; i<=END; i++) {
            trackEvalArray[i] = new TrackEval(splitLineString.getSubTrackFeature(i));
        }

        switch (trackEvalArray[START].getTrackEvalType()) {
            case NODE:
                this.addNetworkNodeStart(trackEvalArray[START].getNetworkNode())
                        .addStartDistance(trackEvalArray[START].getNodeDistance());
                break;
            case EDGE:
                this.addEdgeAtStart(trackEvalArray[START].getNetworkEdge())
                        .addSplittingNodeAtStart(
                                trackEvalArray[START].getSplittingNode())
                        .addStartDistance(trackEvalArray[START].getEdgeDistance());
                break;
            case DIAGNOSTIC:
                this.addDiagnostic(trackEvalArray[START]);
                break;
            case NO_CONNECTION:
                break;
            case UNDEFINED:
                break;
            default:
                break;
        }
        switch (trackEvalArray[END].getTrackEvalType()) {
            case NODE:
                this.addNetworkNodeEnd(trackEvalArray[END].getNetworkNode())
                        .addEndDistance(trackEvalArray[END].getNodeDistance());
                break;
            case EDGE:
                this.addEdgeAtEnd(trackEvalArray[END].getNetworkEdge())
                        .addSplittingNodeAtEnd(trackEvalArray[END].getSplittingNode())
                        .addEndDistance(trackEvalArray[END].getEdgeDistance());
                break;
            case DIAGNOSTIC:
                this.addDiagnostic(trackEvalArray[END]);
                break;
            case NO_CONNECTION:
                break;
            case UNDEFINED:
                break;
            default:
                break;
        }
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
     * @param networkEdge
     * @return
     */
    public TrackRecBuilder addEdgeAtStart(Edge networkEdge) {
        // Record this now, not yet prepared to calculate distances or Edges.
        this.networkEdgeStart = networkEdge;
        return this;
    }

    /**
     * @param networkEdge
     */
    public TrackRecBuilder addEdgeAtEnd(Edge networkEdge) {
        // Record this now, not yet prepared to calculate distances or Edges.
        this.networkEdgeEnd = networkEdge;
        return this;
    }

    /**
     * Once the details of the proposal have been added, prepare the recommendation record.
     *
     * The details are brought together across both ends of the starting Track and are
     * trimmed to bring the new location onto the network.  This provides the user
     * with a better feel for what would be added to the network.  Assumption however
     * is that the adding of the detail is the time to perform this trimming.
     *
     * @return
     */
    public Rec build() {
        if (diagnosticMode) {
            return diagnosticBuild();
        }

        OnTrack rec = null;
        switch (numberOfConnections()) {
        case 2:
            populateProposedTracks();
            prepareEnds();
            LOGGER.info("Track " + track + " connected both ends");
            if (networkNodeStart != null) {
                if (networkNodeEnd != null) {
                    rec = new ToTwoNodesImpl(newLoc, track, networkNodeStart,
                            networkNodeEnd);
                } else {
                    rec = new ToSegmentAndNodeImpl(newLoc, track,
                            networkEdgeEnd, splittingNodeEnd, networkNodeStart);
                }
            } else {
                // networkEdgeStart != null
                if (networkNodeEnd != null) {
                    rec = new ToSegmentAndNodeImpl(newLoc, track,
                            networkEdgeStart, splittingNodeStart,
                            networkNodeEnd);
                } else {
                    rec = new ToTwoSegmentsImpl(newLoc, track,
                            networkEdgeStart,
                            networkEdgeEnd,
                            splittingNodeStart,
                            splittingNodeEnd);
                }
            }
            rec.addProposedTrack(trackStart);
            rec.addProposedTrack(trackEnd);
            break;

        case 1:
            LOGGER.info("Track " + track + " connected on single end");
            populateProposedTracks();
            prepareEnds();

            // Order based on which one is closer for this end
            if (preferEdgeStart) {
                if (networkEdgeStart != null) {
                    rec = new ToSegmentImpl(newLoc, track,
                            networkEdgeStart,
                            splittingNodeStart);
                    rec.addProposedTrack(trackStart);
                } else if (networkNodeStart != null) {
                    rec = new ToNodeImpl(newLoc, track, networkNodeStart);
                    rec.addProposedTrack(trackStart);
                }
            } else {
                if (networkNodeStart != null) {
                    rec = new ToNodeImpl(newLoc, track, networkNodeStart);
                    rec.addProposedTrack(trackStart);
                } else if (networkEdgeStart != null) {
                    rec = new ToSegmentImpl(newLoc, track,
                            networkEdgeStart,
                            splittingNodeStart);
                    rec.addProposedTrack(trackStart);
                }
            }

            // Order based on which one is closer for this end
            if (preferEdgeEnd) {
                if (networkEdgeEnd != null) {
                    rec = new ToSegmentImpl(newLoc, track, networkEdgeEnd,
                            splittingNodeEnd);
                    rec.addProposedTrack(trackEnd);
                } else if (networkNodeEnd != null) {
                    rec = new ToNodeImpl(newLoc, track, networkNodeEnd);
                    rec.addProposedTrack(trackEnd);
                }
            } else {
                if (networkNodeEnd != null) {
                    rec = new ToNodeImpl(newLoc, track, networkNodeEnd);
                    rec.addProposedTrack(trackEnd);
                } else if (networkEdgeEnd != null) {
                    rec = new ToSegmentImpl(newLoc, track, networkEdgeEnd,
                            splittingNodeEnd);
                    rec.addProposedTrack(trackEnd);
                }
            }
            break;
        case 0:
        default:
            LOGGER.info("Track " + track.getId() + " unconnected");
            break;
        }
        return rec;
    }

    /**
     * @return
     */
    private Rec diagnosticBuild() {
        DiagnosticRec diagRec = new DiagnosticRec(newLoc);
        for (TrackEval trackEval : trackEvals) {
            for (GeoNode geoNode : trackEval.getDistancePerNode().keySet()) {
                SimpleFeature feature = TranslateUtil.geoNodeToFeature(geoNode);
                diagRec.addFeature(feature);
            }
            for (Edge edge : trackEval.getDistancePerEdge().keySet()) {
                diagRec.addFeature(edge.getFeature());
            }
            for (GeoNode splitNode : trackEval.getSplitPerEdge().values()) {
                diagRec.addFeature(TranslateUtil.geoNodeToFeature(splitNode));
            }
        }
        return diagRec;
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
     * TODO: Consider using different field to convey this is a proposed Feature.
     */
    private void populateProposedTracks() {
        trackStart = trackEvalArray[START].getProposedTrack();
        if (trackStart != null) {
            trackStart.setDisplayName("Proposed");
        }

        trackEnd = trackEvalArray[END].getProposedTrack();
        if (trackEnd != null) {
            trackEnd.setDisplayName("Proposed");
        }
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
     * @param distance
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
     * @param distance
     */
    public TrackRecBuilder addEndDistance(Double distance) {
        this.endDistance = distance;
        return this;
    }

    /**
     * @param trackEval
     */
    public void addDiagnostic(TrackEval trackEval) {
        diagnosticMode = true;
        trackEvals.add(trackEval);
    }
}
