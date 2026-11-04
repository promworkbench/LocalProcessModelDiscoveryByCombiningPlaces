package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.helpers;

import org.junit.Assert;
import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.ActivityCache;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WindowTotalCounterTest {

    private static List<Activity> window(String... activities) {
        return Arrays.stream(activities).map(ActivityCache.getInstance()::getActivity).collect(Collectors.toList());
    }

    @Test
    public void givenWindowWithRepeatedActivity_whenUpdate_thenWindowCountedOnce() {
        WindowTotalCounter counter = new WindowTotalCounter();

        counter.update(window("a", "b", "a"), 1);

        Assert.assertEquals(Integer.valueOf(1), counter.getWindowsContainingActivityCount().get("a"));
        Assert.assertEquals(Integer.valueOf(1), counter.getWindowsContainingActivityCount().get("b"));
    }

    @Test
    public void givenMultipleWindows_whenUpdate_thenWindowMultiplicityIsSummed() {
        WindowTotalCounter counter = new WindowTotalCounter();

        counter.update(window("a", "b"), 3);
        counter.update(window("b", "c"), 2);

        Assert.assertEquals(Integer.valueOf(3), counter.getWindowsContainingActivityCount().get("a"));
        Assert.assertEquals(Integer.valueOf(5), counter.getWindowsContainingActivityCount().get("b"));
        Assert.assertEquals(Integer.valueOf(2), counter.getWindowsContainingActivityCount().get("c"));
        Assert.assertNull(counter.getWindowsContainingActivityCount().get("d"));
        Assert.assertEquals(5, counter.getWindowCount());
    }
}
