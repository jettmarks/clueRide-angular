/*
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
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.LineString;
import org.apache.log4j.Logger;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.clueride.domain.EdgeImpl;
import com.clueride.domain.GeoNode;
import com.clueride.domain.SegmentFeatureImpl;
import com.clueride.domain.TrackFeatureImpl;
import com.clueride.feature.Edge;
import com.clueride.feature.FeatureType;
import com.clueride.feature.LineFeature;
import com.clueride.feature.SegmentFeature;
import com.clueride.geo.TranslateUtil;

/**
 * Utility for working with the file-based JSON datastore.
 *
 * @author jett
 *
 */
public class GeoJsonUtil {
    private static final Logger LOGGER = Logger.getLogger(GeoJsonUtil.class);

    private static int DIGITS_OF_PRECISION = 5;

    private JsonStoreType currentType = JsonStoreType.SEGMENTS;
    private String currentPath = JsonStoreLocation.toString(currentType);

    private boolean haveSchema = false;

    public GeoJsonUtil(JsonStoreType storeType) {
        currentType = storeType;
        currentPath = JsonStoreLocation.toString(currentType);
    }

    public GeoJsonUtil(String path) {
        currentType = JsonStoreType.OTHER;
        currentPath = path;
    }

    /**
     * Brings in all the files in the current location to a FeatureCollection,
     * one feature per file.
     * 
     * @return all features -- without our specific FeatureType -- in a
     *         DefaultFeatureCollection.
     * @throws IOException
     */
    public DefaultFeatureCollection readFeatureCollection() throws IOException {
        DefaultFeatureCollection features = new DefaultFeatureCollection();
        // Get a list of the files
        File directory = new File(JsonStoreLocation.toString(currentType));
        for (File child : directory.listFiles(new GeoJsonFileFilter())) {
            LOGGER.debug("Reading from: " + child.getCanonicalPath());
            SimpleFeature feature = readFeature(child);
            features.add(feature);
        }
        return features;
    }

    /**
     * Creates FeatureType-based instances from single-record files in the
     * Store's directory.
     * 
     * TODO: This is more of a factory-type of thing that could be moved out to
     * a different class.
     * 
     * @return
     * @throws IOException
     */
    public List<Edge> readLineFeatures()
            throws IOException {
        List<Edge> resultSet = new ArrayList<>();
        File directory = new File(JsonStoreLocation.toString(currentType));
        for (File child : directory.listFiles(new GeoJsonFileFilter())) {
            LOGGER.debug("Reading LineFeature from: "
                    + child.getCanonicalPath());
            SimpleFeature feature = readFeature(child);
            // TODO: Factory part here may come out
            resultSet.add((Edge) getInstance(feature));
        }
        return resultSet;
    }

    // TODO: factor out the common parts into a separate method
    public List<SegmentFeature> readSegments() throws IOException {
        List<SegmentFeature> resultSet = new ArrayList<>();
        File directory = new File(JsonStoreLocation.toString(currentType));
        for (File child : directory.listFiles(new GeoJsonFileFilter())) {
            LOGGER.debug("Reading LineFeature from: "
                    + child.getCanonicalPath());
            SimpleFeature feature = readFeature(child);
            // TODO: Factory part here may come out
            resultSet.add((SegmentFeature) getInstance(feature));
        }
        return resultSet;
    }

    public List<Edge> readEdges() throws IOException {
        List<Edge> resultSet = new ArrayList<>();
        File directory = new File(JsonStoreLocation.toString(currentType));
        for (File child : directory.listFiles(new GeoJsonFileFilter())) {
            LOGGER.debug("Reading LineFeature from: "
                    + child.getCanonicalPath());
            SimpleFeature feature = readFeature(child);
            // TODO: Factory part here may come out
            resultSet.add((Edge) getInstance(feature));
        }
        return resultSet;
    }

    /**
     * Limit the file names being read to those ending with the extension
     * 'geojson'.
     * 
     * Shared locally amongst a few methods.
     *
     * @author jett
     */
    class GeoJsonFileFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return (name.endsWith("geojson"));
        }
    }

    /**
     * Give a feature, instantiate a LineFeature instance that supports the
     * FeatureType being read.
     * 
     * TODO: Anonymous instance of this class may work better.
     * 
     * TODO: This doesn't follow the maps I've created to help with this.
     * 
     * @param feature
     * @return
     */
    private LineFeature getInstance(SimpleFeature feature) {
        LineFeature lineFeature = null;
        switch (currentType) {
        case EDGE:
            lineFeature = new EdgeImpl(feature);
            break;
        case NETWORK:
            lineFeature = new SegmentFeatureImpl(feature);
            break;
        default:
            lineFeature = new TrackFeatureImpl(feature);
        }
        return lineFeature;
    }

    public DefaultFeatureCollection readFeatureCollection(String fileName)
            throws IOException {
        File file = new File(JsonStoreLocation.toString(currentType)
                + File.separator + fileName);
        DefaultFeatureCollection features = new DefaultFeatureCollection();
        GeometryJSON geometryJson = new GeometryJSON(DIGITS_OF_PRECISION);
        FeatureJSON featureJson = new FeatureJSON(geometryJson);
        if (haveSchema) {
            featureJson.setFeatureType(FeatureType.SEGMENT_FEATURE_TYPE);
        }
        FeatureIterator<SimpleFeature> featureIterator = featureJson
                .streamFeatureCollection(file.getCanonicalFile());
        SimpleFeature lastFeature = null;
        try {
            while (featureIterator.hasNext()) {
                lastFeature = featureIterator.next();
                features.add(lastFeature);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error("Last successful feature: " + lastFeature);
        } catch (RuntimeException e) {
            LOGGER.error("Last successful feature: " + lastFeature);
        }
        return features;
    }

    /**
     * Package visibility to facilitate testing.
     * 
     * @param child
     * @return
     */
    SimpleFeature readFeature(File child) {
        SimpleFeature feature = null;
        GeometryJSON geometryJson = new GeometryJSON(DIGITS_OF_PRECISION);
        FeatureJSON featureJson = new FeatureJSON(geometryJson);
        featureJson.setFeatureType(JsonSchemaTypeMap.getSchema(currentType));
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
     * @param fileName
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
     * Writes a feature out to the filename supplied using the Schema and
     * location mapped to the {@link this.currentType}.
     * 
     * @param feature - SimpleFeature
     *            representing a feature of the type that matches the
     *            {@link this.currentType}.
     * @param fileName - String
     *            representation of the simple file name with extension, but not
     *            the directory.
     */
    public void writeFeatureToFile(SimpleFeature feature, String fileName) {

        GeometryJSON geometryJson = new GeometryJSON(DIGITS_OF_PRECISION);
        FeatureJSON featureJson = new FeatureJSON(geometryJson);
        featureJson.setFeatureType(JsonSchemaTypeMap.getSchema(currentType));

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
            // TODO: Need to set the schema on the featureJson
            result = featureJson.toString(features);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param feature
     * @return
     */
    public String toString(SimpleFeature feature) {
        String result = "";
        GeometryJSON geometryJson = new GeometryJSON(DIGITS_OF_PRECISION);
        FeatureJSON featureJson = new FeatureJSON(geometryJson);
        try {
            result = featureJson.toString(feature);
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

    /**
     * @param featureList
     * @return
     */
    public String toString(List<SimpleFeature> featureList) {
        DefaultFeatureCollection features = TranslateUtil
                .featureListToCollection(featureList);
        return toString(features);
    }

    /**
     * @param lineString
     * @return
     */
    public static String toString(LineString lineString) {
        // TODO: we could probably instantiate this once and synchronize
        GeometryJSON geometryJson = new GeometryJSON(DIGITS_OF_PRECISION);
        return geometryJson.toString(lineString);
    }

    /**
     * @param file
     * @return
     */
    public SimpleFeatureType readSchema(File file) {
        GeometryJSON geometryJson = new GeometryJSON(DIGITS_OF_PRECISION);
        FeatureJSON featureJson = new FeatureJSON(geometryJson);
        File directory = new File(JsonStoreLocation.toString(currentType));
        String fullPath;
        try {
            fullPath = directory.getCanonicalPath() + File.separatorChar
                    + file.getName();
            InputStream inputStream = new FileInputStream(fullPath);
            return featureJson.readFeatureCollectionSchema(inputStream, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     */
    public void setSchemaType() {
        haveSchema = true;
    }

}
