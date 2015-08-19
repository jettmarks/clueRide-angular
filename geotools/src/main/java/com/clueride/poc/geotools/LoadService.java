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
 * Created Aug 18, 2015
 */
package com.clueride.poc.geotools;

import java.io.IOException;

import org.geotools.feature.DefaultFeatureCollection;

import com.clueride.io.JsonStoreType;
import com.clueride.io.JsonUtil;
import com.clueride.poc.Network;

/**
 * Description.
 *
 * @author jett
 *
 */
public class LoadService {

	private static LoadService instance;

	/**
	 * @return
	 */
	public static LoadService getInstance() {
		if (instance == null) {
			instance = new LoadService();
		}
		return instance;
	}

	/**
	 * For the time being, we don't have one; building an empty one from
	 * scratch.
	 * 
	 * @return
	 */
	public Network loadNetwork() {
		Network network = new Network(new DefaultFeatureCollection());
		return network;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public TrackStore loadTrackStore() throws IOException {
		JsonUtil jsonUtil = new JsonUtil(JsonStoreType.RAW);
		DefaultFeatureCollection features = jsonUtil.readFeatureCollection();
		return new TrackStore(features);
	}

}
