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
 * Created Aug 24, 2015
 */
package com.clueride.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.geotools.feature.DefaultFeatureCollection;

import com.clueride.config.TestModeAware;
import com.clueride.domain.EdgeImpl;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.TrackImpl;
import com.clueride.feature.Edge;
import com.clueride.feature.LineFeature;
import com.clueride.feature.SegmentFeature;
import com.clueride.geo.TranslateUtil;
import com.clueride.io.JsonStoreLocation;
import com.clueride.io.JsonStoreType;
import com.clueride.io.JsonUtil;
import com.clueride.service.EdgeIDProvider;
import com.clueride.service.MemoryBasedEdgeIDProvider;
import com.vividsolutions.jts.geom.Point;

/**
 * TODO: Description.
 *
 * @author jett
 *
 */
public class DefaultNetworkStore implements NetworkStore, TestModeAware {
    private static final Logger LOGGER = Logger
            .getLogger(DefaultNetworkStore.class);

    private static DefaultNetworkStore instance = null;
    private EdgeIDProvider idProvider = new DataBasedEdgeIDProvider();

    // TODO: EDGE for unrated segments, NETWORK for fully-formed SegmentFeature.

    private Set<LineFeature> allLineFeatures = new HashSet<>();
    private Set<Edge> allEdges = new HashSet<>();
    private Set<SegmentFeature> allSegments = new HashSet<>();

    private static String[] storeLocations = {
            JsonStoreLocation.toString(JsonStoreType.EDGE),
            JsonStoreLocation.toString(JsonStoreType.SEGMENTS)
    };

    private static JsonUtil jsonUtilEdge = new JsonUtil(JsonStoreType.EDGE);
    private static JsonUtil jsonUtilNetwork = new JsonUtil(
            JsonStoreType.NETWORK);

    private Map<Integer, LineFeature> allFeatureMap = new HashMap<>();

    private boolean testMode;

    /**
     * Preferred method of obtaining an instance which allows lazy
     * initialization of the network.
     * 
     * @return
     */
    public static DefaultNetworkStore getInstance() {
        if (instance == null) {
            instance = new DefaultNetworkStore();
        }
        return instance;
    }

    /**
     * Use {@link:getInstance()}
     */
    private DefaultNetworkStore() {
    }

    /**
     * Write our edges out to disk. TODO: Segments too.
     * 
     * This implementation is dependent on the org.geotools.gt-referencing
     * package, although it is far from clear that this is the case because it
     * gets reported as an inability to parse the incoming null. The problem is
     * actually writing the null out there. If the identifier for the CRS can't
     * be pulled up, why write it out when you know it can't be read back in?
     * Ugh.
     * 
     * TODO: Creating a Stack Overflow ticket for this.
     * 
     * TODO: Is this why I was having trouble getting the Schema to be read
     * automatically?
     * 
     * This is being revamped. It had been reading from a single file which gets
     * updated, but now, we're moving toward use of a file per record. This
     * means that we've got a whole bunch of records to keep track of.
     * 
     * It may make sense to bring in a copy of at least the IDs, figure out what
     * changes are required, and then persist just the changes. This also means
     * however, that we're not really reloading and the name of this method
     * should change.
     * 
     * Temporarily (maybe?) moved to OTHER directory.
     * 
     * @throws IOException
     * @see com.clueride.dao.NetworkStore#persistAndReload()
     * @deprecated
     */
    @Override
    public void persistAndReload() throws IOException {
        JsonUtil jsonUtilOther = new JsonUtil(JsonStoreType.OTHER);
        DefaultFeatureCollection features = TranslateUtil
                .lineFeatureSetToFeatureCollection(allLineFeatures);
        jsonUtilOther.writeFeaturesToFile(features, "mainNetwork.geojson");

        allLineFeatures.clear();

        features = jsonUtilOther
                .readFeatureCollection("mainNetwork.geojson");
        allLineFeatures = TranslateUtil
                .featureCollectionToLineFeatures(features);
        refreshSegmentData();
    }

    /**
     * This stores both the Segments and Edges out to disk by comparing what is
     * in memory per ID with what is on disk by ID.
     * 
     * Edges and Segments are held in two separate locations and are compared
     * independently of each other.
     * 
     * It is understood that each particular record is immutable; if we need
     * changes, the change is a new record with new ID and the old record/file
     * is removed.
     * 
     * @throws IOException
     */
    public void persist() throws IOException {
        List<Integer> inMemoryIDs = new ArrayList<>();
        List<Integer> onDiskIDs = new ArrayList<>();

        for (LineFeature feature : allLineFeatures) {
            inMemoryIDs.add(feature.getId());
        }
        dumpList("In Memory IDs", inMemoryIDs);

        JsonUtil jsonUtilEdges = new JsonUtil(JsonStoreType.EDGE);
        List<LineFeature> edges = jsonUtilEdges.readLineFeatures();
        for (LineFeature feature : edges) {
            onDiskIDs.add(feature.getId());
        }
        dumpList("On Disk IDs", onDiskIDs);

        List<Integer> toBeAdded = new ArrayList<>();
        for (Integer rec : inMemoryIDs) {
            if (!onDiskIDs.contains(rec)) {
                toBeAdded.add(rec);
                LOGGER.info("Adding " + rec);
            }
        }

        List<Integer> toBeRemoved = new ArrayList<>();
        for (Integer rec : onDiskIDs) {
            if (!inMemoryIDs.contains(rec)) {
                toBeRemoved.add(rec);
                LOGGER.info("Removing " + rec);
            }
        }

        if (toBeAdded.isEmpty()) {
            LOGGER.info("No records to be Added");
        } else {
            // Add the instances to be Added
        }

        if (toBeRemoved.isEmpty()) {
            LOGGER.info("No records to be Removed");
        } else {
            // Delete files for the records to be removed
        }

    }

    /**
     * Lists out in order the values in the passed list.
     * 
     * Package visibility for diagnostics and testing.
     * 
     * @param string
     * @param toBeRemoved
     */
    void dumpList(String message, List<Integer> listToDump) {
        System.out.println(message);
        List<Integer> sortedList = new ArrayList<>(listToDump);
        Collections.sort(sortedList);
        for (Integer id : sortedList) {
            System.out.println(id);
        }
    }

    /**
     * @see com.clueride.dao.NetworkStore#getEdges()
     */
    @Override
    public Set<Edge> getEdges() {
        return allEdges;
    }

    /**
     * @see com.clueride.dao.NetworkStore#getEdgeById(java.lang.Integer)
     */
    @Override
    public Edge getEdgeById(Integer id) {
        return (Edge) allFeatureMap.get(id);
    }

    /**
     * @see com.clueride.dao.NetworkStore#getSegments()
     */
    @Override
    public Set<SegmentFeature> getSegments() {
        return allSegments;
    }

    /**
     * @see com.clueride.dao.NetworkStore#getSegmentById(java.lang.Integer)
     */
    @Override
    public SegmentFeature getSegmentById(Integer id) {
        return (SegmentFeature) allFeatureMap.get(id);
    }

    /**
     * @see com.clueride.dao.NetworkStore#getLineFeatures()
     */
    @Override
    public Set<LineFeature> getLineFeatures() {
        synchronized (allLineFeatures) {
            if (allLineFeatures.isEmpty()) {
                loadAllFeatures();
            }
        }
        return allLineFeatures;
    }

    /**
     * Brings in both the Network/Segment/Rated {@link LineString} features as
     * well as the yet-unrated Segments otherwise known as {@link Edge}s.
     * 
     * This version reads the format where there is a single record per file.
     * 
     * Package visibility for testing.
     */
    @SuppressWarnings("unchecked")
    void loadAllFeatures() {
        try {
            allSegments
                    .addAll((Collection<? extends SegmentFeature>) jsonUtilNetwork
                            .readLineFeatures());
            allLineFeatures.addAll(allSegments);
            allEdges.addAll((Collection<? extends Edge>) jsonUtilEdge
                    .readLineFeatures());
            allLineFeatures.addAll(allEdges);
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshSegmentData();
    }

    /**
     * @throws IOException
     * @deprecated - use {@link loadAllFeatures()} instead.
     */
    private void loadFromDefault() {
        JsonUtil networkStorageUtil = new JsonUtil(JsonStoreType.NETWORK);
        DefaultFeatureCollection features = null;
        try {
            features = networkStorageUtil
                    .readFeatureCollection("mainNetwork.geojson");
        } catch (IOException e) {
            e.printStackTrace();
        }
        allLineFeatures = TranslateUtil
                .featureCollectionToLineFeatures(features);
        refreshSegmentData();
    }

    /**
     * Builds some internal data structures based on a refreshed set of
     * LineFeatures.
     */
    private void refreshSegmentData() {
        allFeatureMap.clear();
        for (LineFeature lineFeature : allLineFeatures) {
            int id = lineFeature.getId();
            idProvider.registerId(id);
            allFeatureMap.put(id, lineFeature);
        }
    }

    /**
     * Part of the Use Case where an additional Edge is being added to the
     * network, but there are probably existing Edges whose ID must be avoided.
     * 
     * Ignores the incoming segment ID if there is one and replaces it with the
     * next in sequence.
     * 
     * Note that DB implementations may be using the database's algorithm for
     * assigning PKs.
     * 
     * @return
     * @see com.clueride.dao.NetworkStore#addNew(com.com.clueride.feature.Edge)
     */
    @Override
    public Integer addNew(Edge edge) {
        // For creation of new instance, we make a copy TODO: Looks cumbersome
        TrackImpl newTrack = new TrackImpl(edge.getDisplayName(), edge.getUrl());
        Integer id = newTrack.getId();
        Edge newEdge = new EdgeImpl(newTrack,
                edge.getLineString());

        allLineFeatures.add(newEdge);
        allFeatureMap.put(id, newEdge);
        return id;
    }

    /**
     * @see com.clueride.dao.NetworkStore#splitSegment(java.lang.Integer,
     *      com.clueride.domain.GeoNode)
     * @Override public void splitSegment(Integer id, GeoNode geoNode) {
     *           LineString originalLineString = (LineString) TranslateUtil
     *           .segmentToFeature(segmentMap.get(id)).getDefaultGeometry();
     *           LineString[] splitPair =
     *           TranslateUtil.split(originalLineString,
     *           geoNode.getPoint().getCoordinate(), true); Segment segmentA =
     *           TranslateUtil.lineStringToSegment(splitPair[0]); Segment
     *           segmentB = TranslateUtil.lineStringToSegment(splitPair[1]);
     *           segmentA.setUrl(segmentMap.get(id).getUrl());
     *           segmentA.setName(segmentMap.get(id).getName());
     *           segmentB.setUrl(segmentMap.get(id).getUrl());
     *           segmentB.setName(segmentMap.get(id).getName());
     *           addNew(segmentA); addNew(segmentB);
     *           segments.remove(segmentMap.get(id)); segmentMap.remove(id); }
     */

    /**
     * @see com.clueride.dao.NetworkStore#splitEdge(java.lang.Integer,
     *      com.vividsolutions.jts.geom.Point)
     */
    @Override
    public void splitEdge(Integer id, Point point) {
        // TODO Auto-generated method stub

    }

    /**
     * @see com.clueride.dao.NetworkStore#splitEdge(java.lang.Integer,
     *      com.clueride.domain.GeoNode)
     */
    @Override
    public void splitEdge(Integer id, GeoNode geoNode) {
        // TODO Auto-generated method stub

    }

    /**
     * @see com.clueride.config.TestModeAware#setTestMode(boolean)
     */
    @Override
    public void setTestMode(boolean isInTest) {
        testMode = isInTest;
        if (isInTest) {
            idProvider = new MemoryBasedEdgeIDProvider();
            TrackImpl.defineIdProvider(idProvider);
        } else {
            idProvider = new DataBasedEdgeIDProvider();
            TrackImpl.defineIdProvider(idProvider);
        }
    }

    /**
     * @see com.clueride.config.TestModeAware#isTestMode()
     */
    @Override
    public boolean isTestMode() {
        return testMode;
    }

    /**
     * Allows checking the current directory where network info is pulled from.
     * 
     * @see com.clueride.dao.NetworkStore#getStoreLocation()
     */
    @Override
    public String[] getStoreLocations() {
        return storeLocations;
    }

    /**
     * @return
     */
    public int getLastEdgeId() {
        return idProvider.getLastId();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DefaultNetworkStore [lineFeatures.size()=").append(
                allLineFeatures.size())
                .append(", idProvider=").append(idProvider).append(
                        ", testMode=").append(testMode)
                .append(", getLastEdgeId()=").append(getLastEdgeId()).append(
                        "]");
        return builder.toString();
    }

}
