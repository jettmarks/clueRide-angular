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
package com.clueride.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.domain.GeoNode;
import com.clueride.geo.TranslateUtil;

/**
 * Utility for working with the file-based JSON datastore.
 *
 * @author jett
 *
 */
public class JsonUtil {

    private static int DIGITS_OF_PRECISION = 5;

    private JsonStoreType currentType = JsonStoreType.SEGMENTS;
    private String currentPath = JsonStoreLocation.toString(currentType);

    public JsonUtil(JsonStoreType storeType) {
        currentType = storeType;
        currentPath = JsonStoreLocation.toString(currentType);
    }

    public JsonUtil(String path) {
        currentType = JsonStoreType.OTHER;
        currentPath = path;
    }

    /**
     * Brings in all the files in the current location to a FeatureCollection.
     * 
     * @return
     * @throws IOException
     */
    public DefaultFeatureCollection readFeatureCollection() throws IOException {
        DefaultFeatureCollection features = new DefaultFeatureCollection();
        // Get a list of the files
        File file = new File(JsonStoreLocation.toString(currentType));
        for (File child : file.listFiles()) {
            // System.out.println(child.getCanonicalPath());
            SimpleFeature feature = readFeature(child);
            features.add(feature);
        }
        return features;
    }

    public DefaultFeatureCollection readFeatureCollection(String fileName)
            throws IOException {
        File file = new File(JsonStoreLocation.toString(currentType)
                + File.separator + fileName);
        DefaultFeatureCollection features = new DefaultFeatureCollection();
        GeometryJSON geometryJson = new GeometryJSON(DIGITS_OF_PRECISION);
        FeatureJSON featureJson = new FeatureJSON(geometryJson);
        FeatureIterator<SimpleFeature> featureIterator = featureJson
                .streamFeatureCollection(file.getCanonicalFile());
        while (featureIterator.hasNext()) {
            features.add(featureIterator.next());
        }
        return features;
    }

    /**
     * @param child
     * @return
     */
    private SimpleFeature readFeature(File child) {
        SimpleFeature feature = null;
        GeometryJSON geometryJson = new GeometryJSON(DIGITS_OF_PRECISION);
        FeatureJSON featureJson = new FeatureJSON(geometryJson);
        InputStream iStream = null;
        try {
            iStream = new FileInputStream(child);
            feature = featureJson.readFeature(iStream);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return feature;
    }

    /**
     * @param features
     * @param string
     */
    public void writeFeaturesToFile(DefaultFeatureCollection features,
            String fileName) {

        GeometryJSON geometryJson = new GeometryJSON(DIGITS_OF_PRECISION);
        FeatureJSON featureJson = new FeatureJSON(geometryJson);

        File outFile = new File(currentPath + File.separator + fileName);
        OutputStream oStream = null;
        try {
            oStream = new FileOutputStream(outFile);
            featureJson.writeFeatureCollection(features, oStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (oStream != null) {
                    oStream.close();
                }
            } catch (IOException e) {
                // Don't care
            }
        }

    }

    /**
     * @param features
     * @param string
     */
    public void writeFeatureToFile(SimpleFeature feature, String fileName) {

        GeometryJSON geometryJson = new GeometryJSON(DIGITS_OF_PRECISION);
        FeatureJSON featureJson = new FeatureJSON(geometryJson);

        File outFile = new File(JsonStoreLocation.toString(currentType)
                + File.separator + fileName);
        OutputStream oStream = null;
        try {
            oStream = new FileOutputStream(outFile);
            featureJson.writeFeature(feature, oStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (oStream != null) {
                    oStream.close();
                }
            } catch (IOException e) {
                // Don't care
            }
        }

    }

    /**
     * @param features
     * @return
     */
    public String toString(DefaultFeatureCollection features) {
        String result = "";
        GeometryJSON geometryJson = new GeometryJSON(DIGITS_OF_PRECISION);
        FeatureJSON featureJson = new FeatureJSON(geometryJson);
        try {
            result = featureJson.toString(features);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    public String toString(GeoNode geoNode) {
        String result = "";
        GeometryJSON geometryJson = new GeometryJSON(DIGITS_OF_PRECISION);
        FeatureJSON featureJson = new FeatureJSON(geometryJson);
        DefaultFeatureCollection features = TranslateUtil
                .geoNodeToFeatureCollection(geoNode);
        try {
            result = featureJson.toString(features);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
