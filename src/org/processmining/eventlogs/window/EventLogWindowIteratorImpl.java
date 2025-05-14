package org.processmining.eventlogs.window;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.WindowInfo;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.ActivityBasedTotallyOrderedEventLogTraceVariant;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.extractors.ActivityBasedTotallyOrderedEventLogTraceVariantExtractor;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.extractors.EventLogTraceVariantExtractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

/**
 * This class is used to traverse an event log using a sliding window. The idea is that given the log \
 * <a, b, c, d\> and a window size of 3, there are the following windows <a>, <a, b>, <a, b, c>, <b, c, d>, <c, d>,
 * and <d>.
 */
class EventLogWindowIteratorImpl implements EventLogWindowIterator {

    private final int maxWindowSize;

    private final Collection<ActivityBasedTotallyOrderedEventLogTraceVariant> remainingTraceVariants;
    private ActivityBasedTotallyOrderedEventLogTraceVariant traceVariant;
    private int windowCount;
    private int position;
    LinkedList<Activity> window;


    public EventLogWindowIteratorImpl(EventLog log, int maxWindowSize) {
        this.maxWindowSize = maxWindowSize;

        ActivityBasedTotallyOrderedEventLogTraceVariantExtractor tvExtractor =
                EventLogTraceVariantExtractor.getActivityBasedTotallyOrdered(
                "concept:name");
        this.remainingTraceVariants = tvExtractor.extract(log);
        this.window = new LinkedList<>();
    }

    public boolean hasNext() {
        return this.traceVariant != null
                && (position < this.traceVariant.size() || this.window.size() > 1)
                || !this.remainingTraceVariants.isEmpty();
    }

    public WindowInfo next() {
        boolean startNewVariant = traceVariant == null // hasn't started traversing
                || position >= traceVariant.size() && window.size() == 1; // finishing a trace variant
        if (startNewVariant) {
            startTraversingNewTraceVariant();
        }

        // remove first event if no space in the window or at the end of the trace
        if (window.size() >= maxWindowSize || position >= traceVariant.size()) {
            window.removeFirst();
        }

        // add next event
        if (position < traceVariant.size()) {
            window.add(traceVariant.get(position++));
        }

        return new WindowInfo(new ArrayList<>(window), windowCount, position - window.size(), position - 1, traceVariant);
    }

    private void startTraversingNewTraceVariant() {
        Optional<ActivityBasedTotallyOrderedEventLogTraceVariant> nextVariant = this.remainingTraceVariants.stream().findFirst();
        if (nextVariant.isPresent()) {
            this.traceVariant = nextVariant.get();
            this.windowCount = this.traceVariant.getCardinality();
            this.position = 0;
            this.window = new LinkedList<>();
            this.remainingTraceVariants.remove(this.traceVariant);
        }
    }

    public ActivityBasedTotallyOrderedEventLogTraceVariant currentWindowParentSequence() {
        return traceVariant;
    }
}
