package com.clueride.domain.dev;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.rec.NetworkRecImpl;

public class NewLocProposalTest {
    private NewLocProposal toTest;
    private GeoNode newLoc = new DefaultGeoNode();

    @BeforeMethod
    public void beforeMethod() {
    }

    @Test
    public void NewLocProposal() {
        toTest = new NewLocProposal(newLoc);
        assertNotNull(toTest);
    }

    @Test
    public void getId() {
        toTest = new NewLocProposal(newLoc);
        assertNotNull(toTest);
        assertTrue(toTest.getId() > 0);
        Integer currentId = toTest.getId();
        Integer expectedId = currentId + 1;
        toTest = new NewLocProposal(newLoc);
        Integer anotherId = toTest.getId();
        assertEquals(anotherId, expectedId);
    }

    @Test
    public void getNodeNetworkState() {
        toTest = new NewLocProposal(newLoc);
        AssertJUnit.assertEquals(NodeNetworkState.UNDEFINED, toTest
                .getNodeNetworkState());
        NodeNetworkState expected = NodeNetworkState.ON_SINGLE_TRACK;
        toTest.setNodeNetworkState(expected);
        NodeNetworkState actual = toTest.getNodeNetworkState();
        AssertJUnit.assertEquals(actual, expected);
    }

    @Test
    public void getRecommendations() {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void hasMultipleRecommendations() {
        toTest = new NewLocProposal(newLoc);
        assertFalse(toTest.hasMultipleRecommendations());
        toTest.add(new NetworkRecImpl());
        assertFalse(toTest.hasMultipleRecommendations());
        toTest.add(new NetworkRecImpl());
        assertTrue(toTest.hasMultipleRecommendations());
    }

    @Test
    public void setNodeNetworkState() {
        toTest = new NewLocProposal(newLoc);
        AssertJUnit.assertEquals(NodeNetworkState.UNDEFINED, toTest
                .getNodeNetworkState());
        NodeNetworkState expected = NodeNetworkState.ON_MULTI_TRACK;
        toTest.setNodeNetworkState(expected);
        NodeNetworkState actual = toTest.getNodeNetworkState();
        AssertJUnit.assertEquals(actual, expected);
    }

    @Test
    public void toJson() {
        throw new RuntimeException("Test not implemented");
    }
}
