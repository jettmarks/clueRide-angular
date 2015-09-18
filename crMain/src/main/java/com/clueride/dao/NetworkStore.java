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
import com.clueride.domain.dev.Segment;
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
     * This holds the segments -- if we need them, this is the place to go.
     * 
     * @return
     */
    Set<Segment> getSegments();

    /**
     * Choose a particular segment by its ID.
     * 
     * @param id
     * @return
     */
    Segment getSegmentById(Integer id);

    /**
     * Brings the geometry and other details for creation of a new Segment along
     * with the assignment of an ID.
     * 
     * @param segment
     * @return
     */
    Integer addNew(Segment segment);

    /**
     * When it's time to split one of these, we want the Store to know about it.
     * 
     * @param id
     * @param geoNode
     */
    void splitSegment(Integer id, GeoNode geoNode);

    /**
     * When it's time to split one of these, we want the Store to know about it.
     * 
     * This version knows how to work with a Point.
     * 
     * @param id
     * @param point
     */
    void splitSegment(Integer id, Point point);
}
