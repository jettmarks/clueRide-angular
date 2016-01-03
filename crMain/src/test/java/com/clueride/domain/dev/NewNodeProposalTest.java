package com.clueride.domain.dev;

import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.rec.NetworkRecImpl;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

public class NewNodeProposalTest {
    private NewNodeProposal toTest;
    private GeoNode newLoc = new DefaultGeoNode();

    @BeforeMethod
    public void beforeMethod() {
    }

    @Test
    public void NewLocProposal() {
        toTest = new NewNodeProposal(newLoc);
        assertNotNull(toTest);
    }

    @Test
    public void getId() {
        toTest = new NewNodeProposal(newLoc);
        assertNotNull(toTest);
        assertTrue(toTest.getId() > 0);
        Integer currentId = toTest.getId();
        Integer expectedId = currentId + 1;
        toTest = new NewNodeProposal(newLoc);
        Integer anotherId = toTest.getId();
        assertEquals(anotherId, expectedId);
    }

    @Test
    // TODO: This should be setting Recs to exercise the logic
    public void getNodeNetworkState() {
        toTest = new NewNodeProposal(newLoc);
        AssertJUnit.assertEquals(NodeNetworkState.UNDEFINED, toTest
                .getNodeNetworkState());
        NodeNetworkState expected = NodeNetworkState.ON_SINGLE_TRACK;
        // toTest.setNodeNetworkState(expected);
        NodeNetworkState actual = toTest.getNodeNetworkState();
        AssertJUnit.assertEquals(actual, expected);
    }

    @Test
    public void getRecommendations() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void hasMultipleRecommendations() {
        toTest = new NewNodeProposal(newLoc);
        assertFalse(toTest.hasMultipleRecommendations());
        toTest.add(new NetworkRecImpl());
        assertFalse(toTest.hasMultipleRecommendations());
        toTest.add(new NetworkRecImpl());
        assertTrue(toTest.hasMultipleRecommendations());
    }

    @Test
    public void toJson() {
        throw new RuntimeException("Test not implemented");
    }
}
