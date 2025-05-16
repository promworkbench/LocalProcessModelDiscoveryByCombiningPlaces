package org.processmining.eventlogs.window;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.SlidingWindowInfo;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;

import java.util.Iterator;

/**
 * The window based event log iterates the event log using a sliding window. However, the iteration is made throughout
 * the trace variants and not the traces itself in order to avoid duplication. The {@link SlidingWindowInfo} keeps
 * information in how many traces of that variant the window occurs. Additionally, the sliding window assumes there
 * is a padding at the beginning and end of each trace variant so that each element is represented in equal number of
 * windows.
 * <p>
 * Example: Let us assume that the sliding window is of size 3 and the event log has one trace variant [a, b, c, b].
 * The sliding window used for iteration would generate 6 windows: [a], [a, b], [a, b, c], [b, c, b], [c, b], and [b].
 */
public class WindowBasedEventLog implements Iterable<SlidingWindowInfo> {
    private final EventLog eventLog;
    private final int windowSize;

    public WindowBasedEventLog(EventLog eventLog, int windowSize) {
        this.eventLog = eventLog;
        this.windowSize = windowSize;
    }
    @Override
    public Iterator<SlidingWindowInfo> iterator() {
        return EventLogWindowIterator.getInstance(eventLog, windowSize);
    }

    public static WindowBasedEventLog getInstance(EventLog eventLog, int windowSize) {
        return new WindowBasedEventLog(eventLog, windowSize);
    }
}
