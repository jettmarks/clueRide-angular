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

import java.util.Set;

import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.Segment;
import com.clueride.io.JsonStoreLocation;
import com.clueride.io.JsonStoreType;
import com.vividsolutions.jts.geom.Point;

/**
 * Description.
 *
 * @author jett
 *
 */
public class DefaultNetworkStore implements NetworkStore {

	JsonStoreType ourStoreType = JsonStoreType.NETWORK;

	/**
	 * @see com.clueride.dao.NetworkStore#getStoreLocation()
	 */
	@Override
	public String getStoreLocation() {
		return JsonStoreLocation.toString(ourStoreType);
	}

	/**
	 * @see com.clueride.dao.NetworkStore#persistAndReload()
	 */
	@Override
	public void persistAndReload() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.clueride.dao.NetworkStore#getSegments()
	 */
	@Override
	public Set<Segment> getSegments() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.clueride.dao.NetworkStore#getSegmentById(java.lang.Integer)
	 */
	@Override
	public Segment getSegmentById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.clueride.dao.NetworkStore#newSegmentId()
	 */
	@Override
	public Integer newSegmentId() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.clueride.dao.NetworkStore#addNew(com.clueride.domain.dev.Segment)
	 */
	@Override
	public void addNew(Segment segment) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.clueride.dao.NetworkStore#splitSegment(java.lang.Integer,
	 *      com.clueride.domain.GeoNode)
	 */
	@Override
	public void splitSegment(Integer id, GeoNode geoNode) {
		// TODO Auto-generated method stub

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
