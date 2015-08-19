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
 * Created Aug 16, 2015
 */
package com.clueride.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.clueride.io.JsonStoreType;
import com.clueride.service.SegmentService;

/**
 * Maps between the raw segments endpoint and the SegmentService which knows how
 * to retrieve the raw segments and turn it into JSON.
 *
 * @author jett
 */
@Path("segments/raw")
public class RawSegments {

	/**
	 * Method handling HTTP GET requests. The returned object is GeoJSON
	 * representing a FeatureCollection.
	 *
	 * @return String representing the raw segments as GeoJSON.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getFeatureCollection() {
		return SegmentService.getFeatureCollection(JsonStoreType.RAW);
	}

}
