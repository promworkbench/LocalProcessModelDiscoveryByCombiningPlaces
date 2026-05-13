package org.processmining.placebasedlpmdiscovery.view.components.general;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistogramEntriesTest {

    // --- from() ---

    @Test
    public void givenEmptyDistribution_whenFrom_thenEmptyList() {
        List<Map.Entry<String, Integer>> result = HistogramEntries.from(new HashMap<>());
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void givenSingleEntry_whenFrom_thenOneEntryWithStringKey() {
        Map<Integer, Integer> distribution = new HashMap<>();
        distribution.put(5, 3);

        List<Map.Entry<String, Integer>> result = HistogramEntries.from(distribution);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("5", result.get(0).getKey());
        Assert.assertEquals(Integer.valueOf(3), result.get(0).getValue());
    }

    @Test
    public void givenMultipleEntries_whenFrom_thenSortedByKeyAscending() {
        Map<Integer, Integer> distribution = new HashMap<>();
        distribution.put(3, 2);
        distribution.put(1, 5);
        distribution.put(2, 3);

        List<Map.Entry<String, Integer>> result = HistogramEntries.from(distribution);

        Assert.assertEquals(3, result.size());
        Assert.assertEquals("1", result.get(0).getKey());
        Assert.assertEquals("2", result.get(1).getKey());
        Assert.assertEquals("3", result.get(2).getKey());
        Assert.assertEquals(1, result.get(0).getValue().intValue());
        Assert.assertEquals(1, result.get(1).getValue().intValue());
        Assert.assertEquals(1, result.get(2).getValue().intValue());
    }

    @Test
    public void givenDistributionWithGaps_whenFrom_thenGapsFilledWithZero() {
        Map<Integer, Integer> distribution = new HashMap<>();
        distribution.put(0, 5);
        distribution.put(3, 2);

        List<Map.Entry<String, Integer>> result = HistogramEntries.from(distribution);

        Assert.assertEquals(4, result.size());
        Assert.assertEquals("0", result.get(0).getKey());
        Assert.assertEquals(Integer.valueOf(5), result.get(0).getValue());
        Assert.assertEquals("1", result.get(1).getKey());
        Assert.assertEquals(Integer.valueOf(0), result.get(1).getValue());
        Assert.assertEquals("2", result.get(2).getKey());
        Assert.assertEquals(Integer.valueOf(0), result.get(2).getValue());
        Assert.assertEquals("3", result.get(3).getKey());
        Assert.assertEquals(Integer.valueOf(2), result.get(3).getValue());
    }

    // --- binned() ---

    @Test
    public void givenEmptyDistribution_whenBinned_thenEmptyList() {
        List<Map.Entry<String, Integer>> result = HistogramEntries.binned(new HashMap<>());
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void givenSmallRange_whenBinned_thenBinSizeOneAndPlainLabels() {
        // range 0-10 (11 values) -> binSize = ceil(11/20) = 1 -> plain labels, no grouping
        Map<Integer, Integer> distribution = new HashMap<>();
        distribution.put(0, 5);
        distribution.put(10, 3);

        List<Map.Entry<String, Integer>> result = HistogramEntries.binned(distribution);

        Assert.assertEquals(11, result.size());
        Assert.assertEquals("0", result.get(0).getKey());
        Assert.assertEquals("10", result.get(10).getKey());
    }

    @Test
    public void givenRangeOfHundred_whenBinned_thenTwentyBins() {
        // range 0-99 (100 values) -> binSize = ceil(100/20) = 5 -> exactly 20 bins
        Map<Integer, Integer> distribution = new HashMap<>();
        distribution.put(0, 10);
        distribution.put(99, 5);

        List<Map.Entry<String, Integer>> result = HistogramEntries.binned(distribution);

        Assert.assertEquals(20, result.size());
    }

    @Test
    public void givenRangeOfHundred_whenBinned_thenBinLabelsAreRanges() {
        Map<Integer, Integer> distribution = new HashMap<>();
        distribution.put(0, 10);
        distribution.put(99, 5);

        List<Map.Entry<String, Integer>> result = HistogramEntries.binned(distribution);

        Assert.assertEquals("0-4", result.get(0).getKey());
        Assert.assertEquals("95-99", result.get(19).getKey());
    }

    @Test
    public void givenRangeOfHundred_whenBinned_thenCountsSummedPerBin() {
        // key 0 (count 10) falls in bin "0-4"; key 99 (count 5) falls in bin "95-99"; all others 0
        Map<Integer, Integer> distribution = new HashMap<>();
        distribution.put(0, 10);
        distribution.put(99, 5);

        List<Map.Entry<String, Integer>> result = HistogramEntries.binned(distribution);

        Assert.assertEquals(Integer.valueOf(10), result.get(0).getValue());
        Assert.assertEquals(Integer.valueOf(5), result.get(19).getValue());
        Assert.assertEquals(Integer.valueOf(0), result.get(1).getValue());
    }

    @Test
    public void givenMultipleKeysInSameBin_whenBinned_thenCountsSummed() {
        // range 0-99, binSize=5; keys 0,1,2 all fall in bin "0-4"
        Map<Integer, Integer> distribution = new HashMap<>();
        distribution.put(0, 3);
        distribution.put(1, 4);
        distribution.put(2, 2);
        distribution.put(99, 1);

        List<Map.Entry<String, Integer>> result = HistogramEntries.binned(distribution);

        Assert.assertEquals(Integer.valueOf(9), result.get(0).getValue());
    }
}
