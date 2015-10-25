package com.clueride.dao;

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.clueride.feature.TrackFeature;

public class DefaultTrackStoreTest {
    @BeforeMethod
    public void beforeMethod() {
    }

    @BeforeTest
    public void beforeTest() {
    }

    @Test
    public void DefaultTrackStore() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void getInstance() {
        TrackStore trackStore = DefaultTrackStore.getInstance();
        assertNotNull(trackStore);
    }

    @Test
    public void getLineFeatures() {
        TrackStore trackStore = DefaultTrackStore.getInstance();
        assertNotNull(trackStore.getLineFeatures());
        assertTrue(trackStore.getLineFeatures().size() > 0);
    }

    @Test
    public void getStoreLocation() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void getTrackById() {
        TrackStore trackStore = DefaultTrackStore.getInstance();
        assertNotNull(trackStore.getTrackFeatures());
        TrackFeature track = trackStore.getTrackById(33);
        assertNotNull(track);
        assertNotNull(track.getDisplayName());
        assertNotNull(track.getUrl());
    }

    @Test
    public void getTrackFeatures() {
        TrackStore trackStore = DefaultTrackStore.getInstance();
        assertNotNull(trackStore.getTrackFeatures());
        assertTrue(trackStore.getTrackFeatures().size() > 0);
    }

    @Test
    public void loadAllFeatures() {
        throw new RuntimeException("Test not implemented");
    }
}
