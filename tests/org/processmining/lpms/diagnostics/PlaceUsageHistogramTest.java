package org.processmining.lpms.diagnostics;

import org.junit.Assert;
import org.junit.Test;
import org.processmining.mockobjects.MockPlaces;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.Arrays;
import java.util.Collections;

public class PlaceUsageHistogramTest {

    @Test
    public void givenEmptyCollection_whenConstructed_thenTotalLPMsIsZero() {
        PlaceUsageHistogram h = new PlaceUsageHistogram(Collections.emptyList());
        Assert.assertEquals(0, h.getTotalLPMs());
    }

    @Test
    public void givenEmptyCollection_whenConstructed_thenHistogramIsEmpty() {
        PlaceUsageHistogram h = new PlaceUsageHistogram(Collections.emptyList());
        Assert.assertTrue(h.getHistogram().isEmpty());
    }

    @Test
    public void givenSingleLPMWithOnePlace_whenConstructed_thenTotalLPMsIsOne() {
        LocalProcessModel lpm = new LocalProcessModel();
        lpm.addPlace(MockPlaces.getSequencePlace_ab());
        PlaceUsageHistogram h = new PlaceUsageHistogram(Collections.singletonList(lpm));
        Assert.assertEquals(1, h.getTotalLPMs());
    }

    @Test
    public void givenSingleLPMWithOnePlace_whenGetCount_thenOne() {
        Place place = MockPlaces.getSequencePlace_ab();
        LocalProcessModel lpm = new LocalProcessModel();
        lpm.addPlace(place);
        PlaceUsageHistogram h = new PlaceUsageHistogram(Collections.singletonList(lpm));
        Assert.assertEquals(1, h.getCount(place));
    }

    @Test
    public void givenTwoLPMsWithSamePlace_whenGetCount_thenTwo() {
        Place place = MockPlaces.getSequencePlace_ab();
        LocalProcessModel lpm1 = new LocalProcessModel();
        lpm1.addPlace(place);
        LocalProcessModel lpm2 = new LocalProcessModel();
        lpm2.addPlace(place);
        PlaceUsageHistogram h = new PlaceUsageHistogram(Arrays.asList(lpm1, lpm2));
        Assert.assertEquals(2, h.getCount(place));
    }

    @Test
    public void givenTwoLPMsWithDifferentPlaces_whenGetCount_thenOneEach() {
        Place ab = MockPlaces.getSequencePlace_ab();
        Place bc = MockPlaces.getSequencePlace_bc();
        LocalProcessModel lpm1 = new LocalProcessModel();
        lpm1.addPlace(ab);
        LocalProcessModel lpm2 = new LocalProcessModel();
        lpm2.addPlace(bc);
        PlaceUsageHistogram h = new PlaceUsageHistogram(Arrays.asList(lpm1, lpm2));
        Assert.assertEquals(1, h.getCount(ab));
        Assert.assertEquals(1, h.getCount(bc));
    }

    @Test
    public void givenPlaceNotInAnyLPM_whenGetCount_thenZero() {
        PlaceUsageHistogram h = new PlaceUsageHistogram(Collections.emptyList());
        Assert.assertEquals(0, h.getCount(MockPlaces.getSequencePlace_ab()));
    }
}
