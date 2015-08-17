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
 * Created Aug 4, 2015
 */
package com.clueride.poc.geotools;

import java.io.File;
import java.io.IOException;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;

/**
 * Description.
 *
 * @author jett
 *
 */
public class Quickstart {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		File file = JFileDataStoreChooser.showOpenFile("shp", null);
		if (file == null) {
			return;
		}

		FileDataStore store = FileDataStoreFinder.getDataStore(file);
		SimpleFeatureSource featureSource = store.getFeatureSource();

		// Create a map content and add our shapefile to it
		MapContent map = new MapContent();
		map.setTitle("Quickstart");

		Style style = SLD.createSimpleStyle(featureSource.getSchema());
		Layer layer = new FeatureLayer(featureSource, style);
		map.addLayer(layer);

		// Now display the map
		JMapFrame.showMap(map);

	}

}
