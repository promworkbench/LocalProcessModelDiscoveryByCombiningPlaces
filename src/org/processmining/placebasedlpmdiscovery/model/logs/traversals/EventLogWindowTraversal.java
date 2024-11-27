package org.processmining.placebasedlpmdiscovery.model.logs.traversals;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.IWindowInfo;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;

/**
 * The classes of this type are used to traverse an event log using a sliding window.
 */
public interface EventLogWindowTraversal {

    /**
     * Returns if there is a next window for the event log.
     * @return true if there is a next window, false otherwise
     */
    boolean hasNext();

    /**
     * Returns the next window in the event log.
     * @return next window in the event log.
     */
    IWindowInfo next();

    /**
     * Returns a default window traversal instance.
     * @param eventLog - the event log that should be traversed
     * @param windowSize - the size of the sliding window
     * @return a traversal object
     */
    static EventLogWindowTraversal getInstance(EventLog eventLog, int windowSize) {
        return new DefaultEventLogWindowTraversal(eventLog, windowSize);
    }
}
