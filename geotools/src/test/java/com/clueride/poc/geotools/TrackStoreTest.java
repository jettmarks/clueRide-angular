package com.clueride.poc.geotools;

import org.testng.annotations.BeforeMethod;

import static org.mockito.MockitoAnnotations.initMocks;

public class TrackStoreTest {

    private TrackStore toTest;

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);

//         LoadService service = LoadService.getInstance();
//         toTest = service.loadTrackStore();
    }

//    @Test
    public void TrackStore() {
         throw new RuntimeException("Test not implemented");
    }

//    @Test
    public void getFirstFeature() {
         throw new RuntimeException("Test not implemented");
    }

//    @Test
    public void getFirstLineFeature() {
        System.out.println(toTest.getFirstLineFeature());
    }

//    @Test
    public void getFirstLineString() {
         throw new RuntimeException("Test not implemented");
    }

//    @Test
    public void getFirstPoint() {
         throw new RuntimeException("Test not implemented");
    }

//    @Test
    public void getMidPoint() {
         throw new RuntimeException("Test not implemented");
    }
}
