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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.geotools.feature.DefaultFeatureCollection;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.Segment;
import com.clueride.geo.TranslateUtil;
import com.clueride.io.JsonStoreLocation;
import com.clueride.io.JsonStoreType;
import com.clueride.io.JsonUtil;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * Description.
 *
 * @author jett
 *
 */
public class DefaultNetworkStore implements NetworkStore {

    private static DefaultNetworkStore instance = null;

    private JsonStoreType ourStoreType = JsonStoreType.NETWORK;
    private Integer maxSegmentId = 0;
    private Set<Segment> segments = new HashSet<>();
    private Map<Integer, Segment> segmentMap = new HashMap<>();

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
     * @see com.clueride.dao.NetworkStore#getStoreLocation()
     */
    @Override
    public String getStoreLocation() {
        return JsonStoreLocation.toString(ourStoreType);
    }

    /**
     * Write our segments out to disk.
     * 
     * This implementation is dependent on the org.geotools.gt-referencing
     * package, although it is far from clear that this is the case because it
     * gets reported as an inability to parse the incoming null. The problem is
     * actually writing the null out there. If the identifier for the CRS can't
     * be pulled up, why write it out when you know it can't be read back in?
     * Ugh.
     * 
     * Creating a Stack Overflow ticket for this.
     * 
     * @throws IOException
     * @see com.clueride.dao.NetworkStore#persistAndReload()
     */
    @Override
    public void persistAndReload() throws IOException {
        JsonUtil networkStorageUtil = new JsonUtil(ourStoreType);
        DefaultFeatureCollection features = TranslateUtil
                .segmentsToFeatureCollection(segments);
        networkStorageUtil.writeFeaturesToFile(features, "mainNetwork.geojson");

        segments.clear();

        // DefaultFeatureCollection features = networkStorageUtil
        features = networkStorageUtil
                .readFeatureCollection("mainNetwork.geojson");
        segments = TranslateUtil.featureCollectionToSegments(features);
        refreshSegmentData();
    }

    /**
     * @see com.clueride.dao.NetworkStore#getSegments()
     */
    @Override
    public Set<Segment> getSegments() {
        synchronized (segments) {
            if (segments.isEmpty()) {
                loadFromDefault();
            }
        }
        return segments;
    }

    /**
     * @throws IOException
     * 
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
        segments = TranslateUtil.featureCollectionToSegments(features);
        refreshSegmentData();
    }

    /**
     * 
     */
    private void refreshSegmentData() {
        maxSegmentId = 0;
        segmentMap.clear();
        for (Segment segment : segments) {
            int segmentId = segment.getSegId();
            if (segmentId > maxSegmentId) {
                maxSegmentId = segmentId;
            }
            segmentMap.put(segmentId, segment);
        }
    }

    /**
     * @see com.clueride.dao.NetworkStore#getSegmentById(java.lang.Integer)
     */
    @Override
    public Segment getSegmentById(Integer id) {
        return segmentMap.get(id);
    }

    /**
     * Ignores the incoming segment ID if there is one and replaces it with the
     * next in sequence.
     * 
     * Note that DB implementations may be using the database's algorithm for
     * assigning PKs.
     * 
     * @return
     * @see com.clueride.dao.NetworkStore#addNew(com.clueride.domain.dev.Segment)
     */
    @Override
    public Integer addNew(Segment segment) {
        maxSegmentId++;
        segment.setSegId(maxSegmentId);
        segments.add(segment);
        segmentMap.put(maxSegmentId, segment);
        return maxSegmentId;
    }

    /**
     * @see com.clueride.dao.NetworkStore#splitSegment(java.lang.Integer,
     *      com.clueride.domain.GeoNode)
     */
    @Override
    public void splitSegment(Integer id, GeoNode geoNode) {
        LineString originalLineString = (LineString) TranslateUtil
                .segmentToFeature(segmentMap.get(id)).getDefaultGeometry();
        LineString[] splitPair = TranslateUtil.split(originalLineString,
                geoNode.getPoint().getCoordinate(), true);
        Segment segmentA = TranslateUtil.lineStringToSegment(splitPair[0]);
        Segment segmentB = TranslateUtil.lineStringToSegment(splitPair[1]);
        segmentA.setUrl(segmentMap.get(id).getUrl());
        segmentA.setName(segmentMap.get(id).getName());
        segmentB.setUrl(segmentMap.get(id).getUrl());
        segmentB.setName(segmentMap.get(id).getName());
        addNew(segmentA);
        addNew(segmentB);
        segments.remove(segmentMap.get(id));
        segmentMap.remove(id);
    }

    /**
     * @see com.clueride.dao.NetworkStore#splitSegment(java.lang.Integer,
     *      com.vividsolutions.jts.geom.Point)
     */
    @Override
    public void splitSegment(Integer id, Point point) {
        // TODO Auto-generated method stub

    }

}
