package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.helpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindowTotalCounter {

    private final Map<Integer, Integer> transitionCount;
    private int windowCount;

    public WindowTotalCounter() {
        transitionCount = new HashMap<>();
    }

    public int getWindowCount() {
        return windowCount;
    }

    public Map<Integer, Integer> getTransitionCount() {
        return transitionCount;
    }

    public void update(List<Integer> window, int count) {
        this.windowCount += count;
        for (Integer tr : window) {
            int trCount = transitionCount.getOrDefault(tr, 0);
            transitionCount.put(tr, trCount + count);
        }
    }
}
