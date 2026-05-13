package org.processmining.lpms.diagnostics;

import org.junit.Assert;
import org.junit.Test;
import org.processmining.lpms.occurrence.LPMOccurrenceList;

import java.util.Arrays;
import java.util.Collections;

public class EventCoverageHistogramTest {

    @Test
    public void givenNoOccurrenceLists_whenConstructed_thenAllEventsInZeroBucket() {
        EventCoverageHistogram h = new EventCoverageHistogram(Collections.emptyList(), 10);
        Assert.assertEquals(Integer.valueOf(10), h.getDistribution().get(0));
        Assert.assertEquals(1, h.getDistribution().size());
    }

    @Test
    public void givenNoOccurrenceLists_whenGetTotalEvents_thenCorrect() {
        EventCoverageHistogram h = new EventCoverageHistogram(Collections.emptyList(), 10);
        Assert.assertEquals(10, h.getTotalEvents());
    }

    @Test
    public void givenSingleOccurrenceListCoveringSomeEvents_whenConstructed_thenCorrectSplit() {
        LPMOccurrenceList list = LPMOccurrenceList.standard();
        list.push(0, 0);
        list.push(0, 1);
        // 2 covered, 3 uncovered out of 5 total
        EventCoverageHistogram h = new EventCoverageHistogram(Collections.singletonList(list), 5);
        Assert.assertEquals(Integer.valueOf(3), h.getDistribution().get(0));
        Assert.assertEquals(Integer.valueOf(2), h.getDistribution().get(1));
    }

    @Test
    public void givenTwoOccurrenceListsWithNoOverlap_whenConstructed_thenEachEventCoveredByOne() {
        LPMOccurrenceList list1 = LPMOccurrenceList.standard();
        list1.push(0, 0);
        LPMOccurrenceList list2 = LPMOccurrenceList.standard();
        list2.push(0, 1);
        // 2 covered by exactly 1 LPM, 1 uncovered, 0 covered by 2 LPMs
        EventCoverageHistogram h = new EventCoverageHistogram(Arrays.asList(list1, list2), 3);
        Assert.assertEquals(Integer.valueOf(1), h.getDistribution().get(0));
        Assert.assertEquals(Integer.valueOf(2), h.getDistribution().get(1));
        Assert.assertNull(h.getDistribution().get(2));
    }

    @Test
    public void givenTwoOccurrenceListsWithOverlap_whenConstructed_thenOverlappingEventsCoveredByTwo() {
        LPMOccurrenceList list1 = LPMOccurrenceList.standard();
        list1.push(0, 0);
        list1.push(0, 1);
        LPMOccurrenceList list2 = LPMOccurrenceList.standard();
        list2.push(0, 1); // overlaps with list1 at (0,1)
        list2.push(0, 2);
        // (0,0) → count 1, (0,1) → count 2, (0,2) → count 1, (0,3) → count 0
        EventCoverageHistogram h = new EventCoverageHistogram(Arrays.asList(list1, list2), 4);
        Assert.assertEquals(Integer.valueOf(1), h.getDistribution().get(0));
        Assert.assertEquals(Integer.valueOf(2), h.getDistribution().get(1));
        Assert.assertEquals(Integer.valueOf(1), h.getDistribution().get(2));
    }

    @Test
    public void givenAnyDistribution_whenConstructed_thenSumOfValuesEqualsTotalEvents() {
        LPMOccurrenceList list = LPMOccurrenceList.standard();
        list.push(0, 0);
        list.push(1, 2);
        EventCoverageHistogram h = new EventCoverageHistogram(Collections.singletonList(list), 7);
        int sum = h.getDistribution().values().stream().mapToInt(Integer::intValue).sum();
        Assert.assertEquals(7, sum);
    }
}
