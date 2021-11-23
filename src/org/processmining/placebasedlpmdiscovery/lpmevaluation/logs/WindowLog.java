package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.deckfour.xes.model.XLog;

import java.util.*;

public class WindowLog extends IntegerMappedLog {

    public WindowLog(XLog log) {
        super(log);
    }

    /**
     * Returns all windows in a trace
     *
     * @param traceVariantId: the id of the trace for which we want to get all windows
     * @param windowSize:     the size of the windows to be returned
     * @return map of every window and the number of times it occurs in the trace
     */
    public Map<List<Integer>, Integer> getWindowsForTraceVariant(Integer traceVariantId, int windowSize) {
        // TODO: Check if pagination can be used
        Map<List<Integer>, Integer> windows = new HashMap<>();
        LinkedList<Integer> currentWindow = new LinkedList<>();
        for (int event : this.getTraceVariant(traceVariantId)) {
            if (currentWindow.size() >= windowSize)
                currentWindow.removeFirst();
            currentWindow.add(event);
            int count = windows.getOrDefault(new ArrayList<>(currentWindow), 0);
            windows.put(new ArrayList<>(currentWindow), count + 1);
        }
        while (currentWindow.size() > 1) {
            currentWindow.removeFirst();
            int count = windows.getOrDefault(new ArrayList<>(currentWindow), 0);
            windows.put(new ArrayList<>(currentWindow), count + 1);
        }

        return windows;
    }

    /**
     * Returns all windows in the log
     *
     * @param windowSize: the size of the window
     * @return map of every window and the number of times it occurs in the log
     */
    public Map<List<Integer>, Integer> getAllWindows(int windowSize) {
        // TODO: Check if pagination can be used
        Map<List<Integer>, Integer> resMap = new HashMap<>();
        for (Integer traceVariantId : this.getTraceVariantIds()) {
            List<Integer> traceVariant = this.getTraceVariant(traceVariantId);
            int traceCount = this.getTraceVariantCount(traceVariant);
            for (Map.Entry<List<Integer>, Integer> windowEntry : this.getWindowsForTraceVariant(traceVariantId, windowSize).entrySet()) {
                List<Integer> window = windowEntry.getKey();
                Integer windowCount = windowEntry.getValue();
                int count = resMap.getOrDefault(window, 0);
                resMap.put(window, count + traceCount * windowCount);
            }
        }
        return resMap;
    }
}
