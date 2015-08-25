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
 * Created Aug 23, 2015
 */
package com.clueride.domain;

import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.dev.Node;
import com.vividsolutions.jts.geom.LineString;

/**
 * Implementation which understands the specific data we save with a
 * LineString-based SimpleFeature.
 * 
 * TODO: There may be a better way with setting up a specific type that knows
 * about these guys.
 *
 * @author jett
 *
 */
public class DefaultLineFeature implements LineFeature {

	private int id;
	private String name;
	private LineString lineString;

	/**
	 * @param simpleLineStringFeature
	 */
	public DefaultLineFeature(SimpleFeature simpleLineStringFeature) {
		Object obj = simpleLineStringFeature.getDefaultGeometry();
		if (!(obj instanceof LineString)) {
			throw new IllegalArgumentException(
					"Expecting geometry to be LineString instead of "
							+ obj.getClass());
		}
		this.lineString = (LineString) obj;
		this.id = Integer.parseInt((String) simpleLineStringFeature
				.getAttribute(1));
		this.name = (String) simpleLineStringFeature.getAttribute(0);
	}

	/**
	 * @see com.clueride.domain.LineFeature#getId()
	 */
	@Override
	public int getId() {
		return id;
	}

	/**
	 * @see com.clueride.domain.LineFeature#setId(int)
	 */
	@Override
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @see com.clueride.domain.LineFeature#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @see com.clueride.domain.LineFeature#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see com.clueride.domain.LineFeature#getStart()
	 */
	@Override
	public Node getStart() {
		return null;
	}

	/**
	 * @see com.clueride.domain.LineFeature#getEnd()
	 */
	@Override
	public Node getEnd() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DefaultLineFeature [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", lineString=");
		builder.append(lineString);
		builder.append("]");
		return builder.toString();
	}

}
