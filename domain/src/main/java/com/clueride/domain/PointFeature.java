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
 * Created Aug 27, 2015
 */
package com.clueride.domain;

import com.clueride.domain.dev.Node;

/**
 * Description.
 *
 * @author jett
 *
 */
public interface PointFeature {
	/**
	 * Unique identifier.
	 * 
	 * @return
	 */
	Integer getPointId();

	void setPointId(Integer id);

	/**
	 * Human readable identifier for Location.
	 * 
	 * @return
	 */
	String getName();

	void setName(String name);

	Node getNode();

	void setNode(Node node);

	NodeGroup getNodeGroup();

	void setNodeGroup(NodeGroup nodeGroup);
}
