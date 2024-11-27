package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.IntegerMappedLog;
import org.processmining.placebasedlpmdiscovery.model.logs.traversals.EventLogWindowTraversal;

import java.util.*;

public class WindowLogTraversal implements EventLogWindowTraversal {

    private final IntegerMappedLog log;
    private final int maxWindowSize;

    private final Set<Integer> remainingTraceVariantIds;
    private Integer traceVariantId;
    private List<Integer> traceVariant;
    private int windowCount;
    private int position;
    LinkedList<Integer> window;


    public WindowLogTraversal(IntegerMappedLog log, int maxWindowSize) {
        this.log = log;
        this.maxWindowSize = maxWindowSize;

        this.remainingTraceVariantIds = new HashSet<>(this.log.getTraceVariantIds());
        this.window = new LinkedList<>();
    }

    public boolean hasNext() {
        return this.traceVariantId != null
                && (position < this.traceVariant.size() || this.window.size() > 1)
                || !this.remainingTraceVariantIds.isEmpty();
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

        return new WindowInfo(new ArrayList<>(window), windowCount, traceVariantId, position - window.size(), position - 1);
    }

    private void startTraversingNewTraceVariant() {
        this.traceVariantId = remainingTraceVariantIds.stream().findAny().get(); // get one trace variant id
        this.remainingTraceVariantIds.remove(traceVariantId); // remove trace variant id from the set of remaining

        this.traceVariant = this.log.getTraceVariant(traceVariantId); // get trace variant for the id
        this.windowCount = log.getTraceVariantCount(traceVariant);
        this.position = 0;
        this.window = new LinkedList<>();
    }
}
