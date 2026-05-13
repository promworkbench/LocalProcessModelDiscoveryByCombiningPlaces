package org.processmining.lpms.diagnostics;

import org.junit.Assert;
import org.junit.Test;
import org.processmining.mockobjects.MockLPMs;
import org.processmining.mockobjects.MockPlaces;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.util.Arrays;
import java.util.Collections;

public class TransitionUsageHistogramTest {

    @Test
    public void givenEmptyCollection_whenConstructed_thenTotalLPMsIsZero() {
        TransitionUsageHistogram h = new TransitionUsageHistogram(Collections.emptyList());
        Assert.assertEquals(0, h.getTotalLPMs());
    }

    @Test
    public void givenEmptyCollection_whenConstructed_thenHistogramIsEmpty() {
        TransitionUsageHistogram h = new TransitionUsageHistogram(Collections.emptyList());
        Assert.assertTrue(h.getHistogram().isEmpty());
    }

    @Test
    public void givenSingleLPM_whenConstructed_thenVisibleTransitionsCounted() {
        // MockLPMs.getSequenceLPM_abc() has visible transitions a, b, c
        TransitionUsageHistogram h = new TransitionUsageHistogram(
                Collections.singletonList(MockLPMs.getSequenceLPM_abc()));
        Assert.assertEquals(1, h.getTotalLPMs());
        Assert.assertEquals(1, h.getCount("a"));
        Assert.assertEquals(1, h.getCount("b"));
        Assert.assertEquals(1, h.getCount("c"));
    }

    @Test
    public void givenTwoLPMsWithSharedTransition_whenGetCount_thenTwo() {
        // lpm1 has transitions a, b — lpm2 has transitions b, c — "b" is shared
        LocalProcessModel lpm1 = new LocalProcessModel();
        lpm1.addPlace(MockPlaces.getSequencePlace_ab());
        LocalProcessModel lpm2 = new LocalProcessModel();
        lpm2.addPlace(MockPlaces.getSequencePlace_bc());
        TransitionUsageHistogram h = new TransitionUsageHistogram(Arrays.asList(lpm1, lpm2));
        Assert.assertEquals(2, h.getCount("b"));
        Assert.assertEquals(1, h.getCount("a"));
        Assert.assertEquals(1, h.getCount("c"));
    }

    @Test
    public void givenLPMWithInvisibleTransition_whenConstructed_thenInvisibleTransitionNotCounted() {
        Place place = new Place();
        place.addInputTransition(new Transition("a", false));
        place.addOutputTransition(new Transition("tau", true));
        LocalProcessModel lpm = new LocalProcessModel();
        lpm.addPlace(place);
        TransitionUsageHistogram h = new TransitionUsageHistogram(Collections.singletonList(lpm));
        Assert.assertEquals(1, h.getCount("a"));
        Assert.assertEquals(0, h.getCount("tau"));
    }

    @Test
    public void givenLabelNotInAnyLPM_whenGetCount_thenZero() {
        TransitionUsageHistogram h = new TransitionUsageHistogram(Collections.emptyList());
        Assert.assertEquals(0, h.getCount("nonexistent"));
    }
}
