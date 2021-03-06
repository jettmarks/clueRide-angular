package com.clueride.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.feature.Edge;
import com.clueride.io.JsonStoreLocation;
import com.clueride.service.EdgeIDProvider;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

public class JsonNetworkStoreTest {
    private NetworkStore toTest;
    private JsonNetworkStore toTestImpl;

    private TrackStore trackStore;

    @Mock
    private Edge edge;

    private Integer[] testTracks = { 5457048, 1158079 };

    /**
     * This particular test suite takes advantage of package-visibility methods
     * on the Test instance (a Singleton) while making it more clear which
     * methods are under test versus part of the setup: those on toTest are part
     * of the test and those on toTestImpl are part of the setup.
     *
     * TODO: Come up with better names for these two instances.
     * 
     * @throws IOException
     */
    @BeforeMethod
    public void beforeMethod() throws IOException {
        initMocks(this);
        JsonStoreLocation.setTestMode();
        toTest = JsonNetworkStore.getInstance();
        toTestImpl = JsonNetworkStore.getInstance();
        toTestImpl.setTestMode(false);

        when(edge.getDisplayName()).thenReturn("Test Edge");
        // trackStore = LoadService.getInstance().loadTrackStore();
    }

     @Test
    public void addNewNoDataSource() {
        toTestImpl.setTestMode(true);
        Integer expected = 1;
        Integer actual = toTestImpl.addNew(edge);
        assertEquals(actual, expected);
        // verify(edge).setId(Matchers.eq(1));
    }

     @Test
    public void addNewWithDataSource() {
        Integer expectedId = 42;
        EdgeIDProvider idProvider = new DataBasedEdgeIDProvider();
        idProvider.registerId(expectedId);

        Integer actual = toTestImpl.getLastEdgeId();
        assertEquals(actual, expectedId);

        Integer expected = toTestImpl.getLastEdgeId() + 1;
        actual = toTestImpl.addNew(edge);
        assertEquals(actual, expected);
    }

    @Test
    public void addNewAfterLoadingNetwork() {
        toTestImpl.setTestMode(false);
        Integer lastId = toTestImpl.getLastEdgeId();
//        TrackFeature proposedTrack =
    }

    // @Test
    /** This tests the ability to fully capture the details of the input object. */
    public void getEdgeById() {
        Edge expected = edge;
        Integer index = toTest.addNew(edge);
        Edge actual = toTest.getEdgeById(index);
        hasMatch(expected, actual);
    }

    /**
     * @param expected
     * @param actual
     */
    private void hasMatch(Edge expected, Edge actual) {
        assertEquals(actual.getDisplayName(), expected.getDisplayName());
        assertEquals(actual.getUrl(), expected.getUrl());
        assertEquals(actual.getStart(), expected.getStart());
        assertEquals(actual.getEnd(), expected.getEnd());
        // These won't be equal under this method of assigning IDs
        // assertEquals(actual.getId(), expected.getId());
    }

    // @Test
    public void getEdges() {
        List<Edge> expected = new ArrayList<>();
        expected.add(edge);
        toTest.addNew(edge);
        List<Edge> actual = toTest.getEdges();
    }

//    @Test
    public void persist() throws Exception {
        JsonStoreLocation.clearTestMode();
        // TODO: Commented out until I can sort out what happens to the data stores in each case.
        toTestImpl.setTestMode(false);
        toTestImpl.loadAllFeatures();
        toTest.persist();
    }

    // @Test
    // public void persistAndReload() throws Exception {
    // Set<Edge> expected = new HashSet<>();
    // for (Integer trackId : testTracks) {
    // Edge seg = (Edge) new EdgeImpl(trackStore
    // .getTrackPerId(trackId));
    // // Edge seg = TranslateUtil.featureToSegment(trackStore
    // // .getTrackPerId(trackId));
    // expected.add(seg);
    // toTest.addNew(seg);
    // }
    // Set<Edge> firstActual = toTest.getEdges();
    // assertEquals(firstActual, expected);
    //
    // toTest.persistAndReload();
    // Set<Edge> secondActual = toTest.getEdges();
    // assertEquals(secondActual, expected);
    // }

    // @Test
    public void splitSegmentIntegerGeoNode() {
        // throw new RuntimeException("Test not implemented");
    }

    // @Test
    public void splitSegmentIntegerPoint() {
        // throw new RuntimeException("Test not implemented");
    }

    // TODO: Try a test where a segment is retrieved as an Edge and vice versa
}
