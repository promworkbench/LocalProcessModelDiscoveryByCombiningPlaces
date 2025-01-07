package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.ActivityBasedTotallyOrderedEventLogTraceVariant;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.extractors.ActivityBasedTotallyOrderedEventLogTraceVariantExtractor;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.extractors.EventLogTraceVariantExtractor;
import org.processmining.placebasedlpmdiscovery.model.logs.traversals.EventLogWindowTraversal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

public class WindowLogTraversal implements EventLogWindowTraversal {

    private final EventLog log;
    private final int maxWindowSize;

    private final Collection<ActivityBasedTotallyOrderedEventLogTraceVariant> remainingTraceVariants;
    private ActivityBasedTotallyOrderedEventLogTraceVariant traceVariant;
    private int windowCount;
    private int position;
    LinkedList<Activity> window;


    public WindowLogTraversal(EventLog log, int maxWindowSize) {
        this.log = log;
        this.maxWindowSize = maxWindowSize;

        ActivityBasedTotallyOrderedEventLogTraceVariantExtractor tvExtractor =
                EventLogTraceVariantExtractor.getActivityBasedTotallyOrdered(
                "concept:name");
        this.remainingTraceVariants = tvExtractor.extract(this.log);
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

        return new WindowInfo(new ArrayList<>(window), windowCount, position - window.size(), position - 1);
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
