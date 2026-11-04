package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.helpers;

import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WindowTotalCounter {

    private final Map<String, Integer> windowsContainingActivityCount; // each window counted once per activity
    private int windowCount;

    public WindowTotalCounter() {
        windowsContainingActivityCount = new HashMap<>();
    }

    public int getWindowCount() {
        return windowCount;
    }

    public Map<String, Integer> getWindowsContainingActivityCount() {
        return windowsContainingActivityCount;
    }

    public void update(List<Activity> window, int count) {
        this.windowCount += count;
        Set<String> activities = window.stream().map(Activity::getName).collect(Collectors.toSet());
        for (String activity : activities) {
            windowsContainingActivityCount.merge(activity, count, Integer::sum);
        }
    }
}
