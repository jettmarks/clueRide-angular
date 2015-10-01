package com.clueride.dao;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.domain.dev.Segment;
import com.clueride.geo.TranslateUtil;
import com.clueride.io.JsonStoreLocation;
import com.clueride.io.JsonStoreType;
import com.clueride.poc.geotools.TrackStore;

public class DefaultNetworkStoreTest {
    private NetworkStore toTest;

    private TrackStore trackStore;

    @Mock
    private Segment segment;

    private Integer[] testTracks = { 5457048, 1158079 };

    @BeforeMethod
    public void beforeMethod() throws IOException {
        initMocks(this);
        JsonStoreLocation.setTestMode();
        toTest = DefaultNetworkStore.getInstance();
        when(segment.getName()).thenReturn("Test Segment");
        trackStore = LoadService.getInstance().loadTrackStore();
    }

    @Test
    public void addNew() {
        Integer expected = 1;
        Integer actual = toTest.addNew(segment);
        assertEquals(actual, expected);
        verify(segment).setSegId(Matchers.eq(1));
    }

    @Test
    public void getSegmentById() {
        Segment expected = segment;
        Integer index = toTest.addNew(segment);
        Segment actual = toTest.getSegmentById(index);
        assertEquals(actual, expected);
    }

    @Test
    public void getSegments() {
        Set<Segment> expected = new HashSet<>();
        expected.add(segment);
        toTest.addNew(segment);
        Set<Segment> actual = toTest.getSegments();
        assertEquals(actual, expected);
    }

    @Test
    public void getStoreLocation() {
        String expected = JsonStoreLocation.toString(JsonStoreType.NETWORK);
        String actual = toTest.getStoreLocation();
        assertEquals(actual, expected);
    }

    @Test
    public void persistAndReload() throws Exception {
        Set<Segment> expected = new HashSet<>();
        for (Integer trackId : testTracks) {
            Segment seg = TranslateUtil.featureToSegment(trackStore
                    .getTrackPerId(trackId));
            expected.add(seg);
            toTest.addNew(seg);
        }
        Set<Segment> firstActual = toTest.getSegments();
        assertEquals(firstActual, expected);

        toTest.persistAndReload();
        Set<Segment> secondActual = toTest.getSegments();
        assertEquals(secondActual, expected);
    }

    @Test
    public void splitSegmentIntegerGeoNode() {
        // throw new RuntimeException("Test not implemented");
    }

    @Test
    public void splitSegmentIntegerPoint() {
        // throw new RuntimeException("Test not implemented");
    }
}
