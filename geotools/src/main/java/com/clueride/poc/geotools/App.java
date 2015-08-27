package com.clueride.poc.geotools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.FactoryFinder;

import com.clueride.domain.SegmentImpl;
import com.clueride.domain.dev.Segment;
import com.clueride.domain.factory.SegmentFactory;
import com.clueride.geo.TranslateUtil;
import com.clueride.gpx.TrackUtil;
import com.jettmarks.gmaps.encoder.Track;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

/**
 * Proof of Concept for converting GPX(tag) -> Track -> LineString -> GeoJSON.
 *
 * There are two different types of geometries:
 * <UL>
 * <LI>org.opengis packages (also referred to as the JTS packages)
 * <LI>org.geotools packages
 * </UL>
 */
public class App {
	public static int DIGITS_OF_PRECISION = 5;

	public static void main(String[] args) {
		String tag = "bikeTrain";
		List<Track> tracks = TrackUtil.getTracksForTag(tag);
		System.out.println("Number of tracks: " + tracks.size());

		Map<Track, LineString> linesByName = TrackUtil
				.trackToLineString(tracks);

		Collection<LineString> lineCollection = linesByName.values();
		GeometryFactory gtFactory = FactoryFinder.getGeometryFactory(null);
		GeometryCollection wholeThing = (GeometryCollection) gtFactory
				.buildGeometry(lineCollection);
		Date t1 = new Date();
		MultiLineString combined = (MultiLineString) wholeThing.union();
		Date t2 = new Date();
		System.out.println("Union required " + (t2.getTime() - t1.getTime())
				+ " milliseconds");

		List<Segment> singlePointSegments = new ArrayList<Segment>();
		List<Segment> multiPointSegments = new ArrayList<Segment>();
		int geomCount = combined.getNumGeometries();
		for (int i = 0; i < geomCount; i++) {
			Geometry geometry = combined.getGeometryN(i);
			int pointCount = geometry.getNumPoints();
			// System.out.println("Type: " + geometry.getGeometryType() +
			// " has "
			// + pointCount + " points");
			Segment segment = SegmentFactory.getInstance(geometry);
			segment.setSegId(i);
			if (pointCount == 2 && validateSegment(segment)) {
				segment.setName("SinglePoint LineString - " + i);
				singlePointSegments.add(segment);
			} else {
				segment.setName("MultiPoint LineString - " + i);
				multiPointSegments.add(segment);
			}
		}

		DefaultFeatureCollection features = TranslateUtil
				.segmentsToFeatureCollection(singlePointSegments);
		// JsonUtil.writeFeaturesToFile(features, "singleLineSegments.geojson");
		features = TranslateUtil
				.segmentsToFeatureCollection(multiPointSegments);
		// JsonUtil.writeFeaturesToFile(features, "multiLineSegments.geojson");

		// findIntersection(linesByName);
		// lineStringToJSON(lineString);

		GeometryJSON geometryJson = new GeometryJSON(DIGITS_OF_PRECISION);
		File outFile = new File("combined.geojson");
		try {
			OutputStream oStream = new FileOutputStream(outFile);
			geometryJson.write(combined, oStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param singlePointSegments
	 * @param string
	 */
	private static void writeSegmentsToFile(List<Segment> segments,
			String fileName) {
		GeometryJSON geometryJson = new GeometryJSON(DIGITS_OF_PRECISION);
		File outFile = new File(fileName);
		OutputStream oStream = null;
		try {
			oStream = new FileOutputStream(outFile);
			for (Segment segment : segments) {
				geometryJson.write(((SegmentImpl) segment).getLineString(),
						oStream);
			}
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
	 * @param segment
	 */
	private static boolean validateSegment(Segment segment) {
		if (segment.getStart().matchesLocation((segment.getEnd()))) {
			System.out.println("Single Point Line?");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param linesByName
	 */
	private static void findIntersection(Map<String, LineString> linesByName) {
		for (Entry<String, LineString> entryA : linesByName.entrySet()) {
			for (Entry<String, LineString> entryB : linesByName.entrySet()) {
				System.out.println("Checking " + entryA.getKey() + " against "
						+ entryB.getKey());
				if (entryA.equals(entryB))
					continue;
				// TODO: Introduce PreparedGeometry
				if (entryA.getValue().intersects(entryB.getValue())) {
					System.out.println(entryA.getKey() + " intersects "
							+ entryB.getKey());
					lineStringToJSON(entryA);
					lineStringToJSON(entryB);
					System.out
							.println("Intersection is "
									+ entryA.getValue().intersection(
											entryB.getValue()));
					System.exit(0);
				}
			}
		}
	}

	/**
	 * @param entry
	 *            Representing the pair Name : LineString.
	 */
	private static void lineStringToJSON(Entry<String, LineString> entry) {
		int digitsOfPrecision = 5;
		GeometryJSON geometryJson = new GeometryJSON(digitsOfPrecision);
		File outFile = new File(entry.getKey() + ".geojson");
		try {
			OutputStream oStream = new FileOutputStream(outFile);
			geometryJson.writeLine(entry.getValue(), oStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
