package org.processmining.placebasedlpmdiscovery.model.logs.traversals;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.IWindowInfo;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;

/**
 * This class is used to traverse an event log using a sliding window. The idea is that given the log \
 * <a, b, c, d\> and a window size of 3, there are the following windows <a>, <a, b>, <a, b, c>, <b, c, d>, <c, d>,
 * and <d>.
 */
public class DefaultEventLogWindowTraversal implements EventLogWindowTraversal{
    private final int windowSize;

    public DefaultEventLogWindowTraversal(EventLog eventLog, int windowSize) {
        this.windowSize = windowSize;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public IWindowInfo next() {
        return null;
    }
}
