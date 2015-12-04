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
package com.clueride.apps;

import com.clueride.io.GeoJsonUtil;
import com.clueride.io.JsonStoreType;
import org.geotools.feature.DefaultFeatureCollection;

import java.io.IOException;

/**
 * Description.
 *
 * @author jett
 *
 */
public class PrepareBikeTrainCollection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GeoJsonUtil jsonUtilBikeTrain = new GeoJsonUtil(JsonStoreType.RAW);
		GeoJsonUtil jsonUtilLocal = new GeoJsonUtil(".");

		try {
			DefaultFeatureCollection features = jsonUtilBikeTrain
					.readFeatureCollection();

			jsonUtilLocal.writeFeaturesToFile(features, "rawSegments.geojson");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
