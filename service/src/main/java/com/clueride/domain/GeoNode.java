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
 * Created Aug 22, 2015
 */
package com.clueride.domain;

import java.util.List;

import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.dev.Node;
import com.vividsolutions.jts.geom.Point;

/**
 * Refinement of Node that knows about the GeoTools objects.
 *
 * @author jett
 *
 */
public interface GeoNode extends Node {

	/**
	 * @return
	 */
	public Point getPoint();

	public Double getLon();

	public Double getLat();

	public abstract Double getElevation();

	/**
	 * @param nearestNodes
	 */
	public void setNearByNodes(List<GeoNode> nearestNodes);

	public List<GeoNode> getNearByNodes();

	/**
	 * @param feature
	 */
	public void addTrack(SimpleFeature feature);

	public List<SimpleFeature> getTracks();
}
