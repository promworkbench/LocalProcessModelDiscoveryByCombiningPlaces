package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WindowLogTraversal {

    private final WindowLog windowLog;
    private final int maxWindowSize;

    private final Set<Integer> remainingTraceVariantIds;
    private Integer traceVariantId;
    private List<Integer> traceVariant;
    private int windowCount;
    private int position;
    LinkedList<Integer> window;


    public WindowLogTraversal(WindowLog windowLog, int maxWindowSize) {
        this.windowLog = windowLog;
        this.maxWindowSize = maxWindowSize;

        this.remainingTraceVariantIds = this.windowLog.getTraceVariantIds();
        this.window = new LinkedList<>();
    }

    public boolean hasNext() {
        return position < this.traceVariant.size() || !this.remainingTraceVariantIds.isEmpty();
    }

    public WindowInfo next() {
        if (traceVariant == null) {
            startTraversingNewTraceVariant();
        }
        // if no space in the window or at the end of the trace
        if (window.size() >= maxWindowSize || position >= traceVariant.size()) {
            window.removeFirst(); // remove the first event
        }
        if (position < traceVariant.size()) {
            window.add(traceVariant.get(position++));
        }
        return new WindowInfo(new ArrayList<>(window), windowCount, traceVariantId, position - window.size() + 1, position);
    }

    private void startTraversingNewTraceVariant() {
        this.traceVariantId = remainingTraceVariantIds.stream().findAny().get(); // get one trace variant id
        this.remainingTraceVariantIds.remove(traceVariantId); // remove trace variant id from the set of remaining

        this.traceVariant = this.windowLog.getTraceVariant(traceVariantId); // get trace variant for the id
        this.windowCount = windowLog.getTraceVariantCount(traceVariant);
        this.position = 0;
    }
}
