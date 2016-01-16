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
import java.util.List;
import java.util.Set;

import com.vividsolutions.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.GeoNode;
import com.clueride.feature.Edge;
import com.clueride.feature.LineFeature;
import com.clueride.feature.SegmentFeature;

/**
 * Definition of how to pull -- and add -- records for the NetworkStore.
 * 
 * 
 *
 * @author jett
 *
 */
/**
 * Description.
 *
 * @author jett
 *
 */
public interface NetworkStore {

    /**
     * Tells us where the network data can be found.
     * 
     * @return - String array listing the paths of the stores.
     */
    String[] getStoreLocations();

    /**
     * Takes the current state of the network, saves it to the store's location
     * (with backup preferably), and then reloads from that store.
     * 
     * Can be used to initially load as well if the memory copy is empty.
     * 
     * @throws IOException
     * @deprecated - Use {@link this.persist} instead.
     */
    void persistAndReload() throws IOException;

    /**
     * Updates the disk with the state of what's in memory.
     * 
     * Since each record is immutable, changes are implemented by removing the
     * old record and writing a new record; no longer a need for Reload.
     */
    void persist() throws IOException;

    /**
     * This goes against the "each record is immutable" current because it became
     * clear that a course with references to Edge/Segment IDs would be better off
     * if the IDs were not changed each time some trivial aspect of the path had
     * changed.  So we have this method now.
     * @param feature - what we want to persist.
     */
    void persistFeature(SimpleFeature feature);

    /**
     * All Line Features in the network: {@link SegmentFeature}s as well as
     * {@link Edge}s.
     * 
     * @return - Set of the Line Features in the Network.
     */
    Set<LineFeature> getLineFeatures();

    /**
     * Edges and not {@link SegmentFeature}s.
     * 
     * @return Set of the Edges defined for the network.
     */
    List<Edge> getEdges();

    /**
     * Segments and not {@link Edge}s.
     * 
     * @return - Set of the Segments defined in the network.
     */
    Set<SegmentFeature> getSegments();

    /**
     * Choose a particular Edge by its ID.
     * 
     * @param id - Unique identifier for the Edge of interest.
     * @return the matching Edge or null if not found.
     */
    Edge getEdgeById(Integer id);

    /**
     * Choose a particular segment by its ID.
     * 
     * @param id
     * @return
     */
    SegmentFeature getSegmentById(Integer id);

    /**
     * Brings the geometry and other details for creation of a new Segment along
     * with the assignment of an ID.
     * 
     * To be officially added to the network, the LineFeature must be at least
     * an Edge if not a (rated) Segment.
     * 
     * @param edge
     * @return
     */
    Integer addNew(Edge edge);

    /**
     * When it's time to split one of these, we want the Store to know about it.
     * 
     * The NetworkStore would be removing the existing Edge, and replacing it
     * with a new pair of Edges.
     * 
     * @param id
     * @param geoNode
     */
    void splitEdge(Integer id, GeoNode geoNode);

    /**
     * When it's time to split one of these, we want the Store to know about it.
     * 
     * This version knows how to work with a Point.
     * 
     * @param id
     * @param point
     */
    void splitEdge(Integer id, Point point);

}
