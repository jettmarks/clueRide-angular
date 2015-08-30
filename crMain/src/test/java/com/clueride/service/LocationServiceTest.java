package com.clueride.service;

import org.testng.annotations.Test;

public class LocationServiceTest {

	private LocationService toTest;

	@Test
	public void addNewLocation() {
		toTest = new LocationService();
		Double lat = 33.0;
		Double lon = -84.0;
		String actual = toTest.addNewLocation(lat, lon);
		System.out.println(actual);
	}
}
