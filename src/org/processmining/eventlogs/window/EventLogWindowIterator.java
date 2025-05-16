package org.processmining.eventlogs.window;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.SlidingWindowInfo;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;

import java.util.Iterator;

/**
 * The classes of this type are used to traverse an event log using a sliding window.
 */
interface EventLogWindowIterator extends Iterator<SlidingWindowInfo> {
    /**
     * Returns a default window traversal instance.
     * @param eventLog - the event log that should be traversed
     * @param windowSize - the size of the sliding window
     * @return a traversal object
     */
    static EventLogWindowIterator getInstance(EventLog eventLog, int windowSize) {
        return new EventLogWindowIteratorImpl(eventLog, windowSize);
    }
}
