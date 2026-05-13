package org.processmining.lpms.diagnostics;

import org.junit.Assert;
import org.junit.Test;
import org.processmining.mockobjects.MockLPMs;
import org.processmining.mockobjects.MockPlaces;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Arrays;
import java.util.Collections;

public class LPMComplexityHistogramTest {

    @Test
    public void givenEmptyCollection_whenConstructed_thenBothDistributionsEmpty() {
        LPMComplexityHistogram h = new LPMComplexityHistogram(Collections.emptyList());
        Assert.assertTrue(h.getPlaceCountDistribution().isEmpty());
        Assert.assertTrue(h.getTransitionCountDistribution().isEmpty());
    }

    @Test
    public void givenSingleLPM_whenConstructed_thenPlaceCountDistributionCorrect() {
        // MockLPMs.getSequenceLPM_abc() has 2 places (ab, bc)
        LPMComplexityHistogram h = new LPMComplexityHistogram(
                Collections.singletonList(MockLPMs.getSequenceLPM_abc()));
        Assert.assertEquals(Integer.valueOf(1), h.getPlaceCountDistribution().get(2));
        Assert.assertEquals(1, h.getPlaceCountDistribution().size());
    }

    @Test
    public void givenSingleLPM_whenConstructed_thenTransitionCountDistributionCorrect() {
        // MockLPMs.getSequenceLPM_abc() has 3 transitions (a, b, c)
        LPMComplexityHistogram h = new LPMComplexityHistogram(
                Collections.singletonList(MockLPMs.getSequenceLPM_abc()));
        Assert.assertEquals(Integer.valueOf(1), h.getTransitionCountDistribution().get(3));
        Assert.assertEquals(1, h.getTransitionCountDistribution().size());
    }

    @Test
    public void givenTwoLPMsWithSamePlaceCount_whenConstructed_thenCountIsTwo() {
        LPMComplexityHistogram h = new LPMComplexityHistogram(
                Arrays.asList(MockLPMs.getSequenceLPM_abc(), MockLPMs.getSequenceLPM_abc()));
        Assert.assertEquals(Integer.valueOf(2), h.getPlaceCountDistribution().get(2));
    }

    @Test
    public void givenTwoLPMsWithDifferentPlaceCounts_whenConstructed_thenBothCountsPresent() {
        // lpm1 has 2 places, lpm2 has 1 place
        LocalProcessModel lpm2 = new LocalProcessModel();
        lpm2.addPlace(MockPlaces.getSequencePlace_ab());
        LPMComplexityHistogram h = new LPMComplexityHistogram(
                Arrays.asList(MockLPMs.getSequenceLPM_abc(), lpm2));
        Assert.assertEquals(Integer.valueOf(1), h.getPlaceCountDistribution().get(2));
        Assert.assertEquals(Integer.valueOf(1), h.getPlaceCountDistribution().get(1));
        Assert.assertEquals(2, h.getPlaceCountDistribution().size());
        Assert.assertEquals(Integer.valueOf(1), h.getTransitionCountDistribution().get(2));
        Assert.assertEquals(Integer.valueOf(1), h.getTransitionCountDistribution().get(3));
        Assert.assertEquals(2, h.getTransitionCountDistribution().size());
    }
}
