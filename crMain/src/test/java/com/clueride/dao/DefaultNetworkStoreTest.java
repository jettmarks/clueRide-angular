package com.clueride.dao;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.io.JsonStoreLocation;
import com.clueride.io.JsonStoreType;
import com.clueride.poc.geotools.TrackStore;

public class DefaultNetworkStoreTest {
	private NetworkStore toTest;

	private TrackStore trackStore;

	@BeforeMethod
	public void beforeMethod() {
		toTest = new DefaultNetworkStore();
	}

	@Test
	public void addNew() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void getSegmentById() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void getSegments() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void getStoreLocation() {
		String expected = JsonStoreLocation.toString(JsonStoreType.NETWORK);
		String actual = toTest.getStoreLocation();
		assertEquals(actual, expected);
	}

	@Test
	public void newSegmentId() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void persistAndReload() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void splitSegmentIntegerGeoNode() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void splitSegmentIntegerPoint() {
		throw new RuntimeException("Test not implemented");
	}
}
