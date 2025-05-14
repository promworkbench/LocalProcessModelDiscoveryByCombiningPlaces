package org.processmining.eventlogs.window;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.IWindowInfo;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;

import java.util.Iterator;

public class WindowBasedEventLog implements Iterable<IWindowInfo> {
    private final EventLog eventLog;
    private final int windowSize;

    public WindowBasedEventLog(EventLog eventLog, int windowSize) {
        this.eventLog = eventLog;
        this.windowSize = windowSize;
    }
    @Override
    public Iterator<IWindowInfo> iterator() {
        return EventLogWindowIterator.getInstance(eventLog, windowSize);
    }

    public static WindowBasedEventLog getInstance(EventLog eventLog, int windowSize) {
        return new WindowBasedEventLog(eventLog, windowSize);
    }
}
