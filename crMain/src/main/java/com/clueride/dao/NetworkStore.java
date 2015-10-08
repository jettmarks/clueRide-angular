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
import java.util.Set;

import com.clueride.domain.GeoNode;
import com.clueride.feature.Edge;
import com.clueride.feature.LineFeature;
import com.clueride.feature.SegmentFeature;
import com.vividsolutions.jts.geom.Point;

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
     * @return
     */
    String getStoreLocation();

    /**
     * Takes the current state of the network, saves it to the store's location
     * (with backup preferably), and then reloads from that store.
     * 
     * Can be used to initially load as well if the memory copy is empty.
     * 
     * @throws IOException
     */
    void persistAndReload() throws IOException;

    /**
     * This holds the LineFeatures (Segments); when we need them, this is the
     * place to get them.
     * 
     * @return
     * @deprecated - Use either {@link getEdges()} or {@link
     *             getSegmentFeatures()} at this time.
     */
    // Set<LineFeature> getLineFeatures();

    /**
     * SegmentFeatures and not {@link Edge}s.
     * 
     * @return
     */
    Set<LineFeature> getLineFeatures();

    /**
     * Edges and not {@link SegmentFeature}s.
     * 
     * @return Set of the Edges defined for the network.
     */
    Set<Edge> getEdges();

    /**
     * Choose a particular segment by its ID.
     * 
     * @param id
     * @return
     */
    Edge getEdgeById(Integer id);

    /**
     * Brings the geometry and other details for creation of a new Segment along
     * with the assignment of an ID.
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
