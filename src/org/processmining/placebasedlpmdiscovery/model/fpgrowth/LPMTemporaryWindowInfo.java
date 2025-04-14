package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import org.apache.commons.math3.util.Pair;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XTrace;
import org.processmining.placebasedlpmdiscovery.model.SimplePlace;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.ActivityCache;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.EventLogTraceVariant;

import java.util.*;
import java.util.stream.Collectors;

public class LPMTemporaryWindowInfo {

    private final List<Activity> firingSequence;
    private final List<Integer> replayedEventsIndices;
    private final Set<Pair<Integer, Integer>> usedPassages;
    private final Set<SimplePlace<Integer>> usedPlaces;
    private int windowCount;
    private List<Activity> window;
    private int windowLastEventPos;

    private final EventLogTraceVariant traceVariant;
    private Set<XTrace> traces;

    private final Map<Integer, String> reverseLabelMap;

    public LPMTemporaryWindowInfo(List<Activity> firingSequence,
                                  List<Integer> replayedEventsIndices,
                                  List<Activity> window,
                                  Set<Pair<Integer, Integer>> usedPassages,
                                  Set<SimplePlace<Integer>> usedPlaces,
                                  Map<Integer, String> reverseLabelMap,
                                  int windowCount,
                                  int windowLastEventPos,
                                  EventLogTraceVariant traceVariant,
                                  Set<XTrace> traces) {
        this.firingSequence = firingSequence;
        this.replayedEventsIndices = replayedEventsIndices;
        this.window = window;
        this.usedPassages = usedPassages;
        this.usedPlaces = usedPlaces;

        this.reverseLabelMap = reverseLabelMap;

        this.windowCount = windowCount;

        this.windowLastEventPos = windowLastEventPos;
        this.traceVariant = traceVariant;
        this.traces = traces;
    }

    public List<Activity> getActivityFiringSequence() {
        return firingSequence;
    }

    public Set<Pair<Integer, Integer>> getIntegerUsedPassages() {
        return usedPassages;
    }

    public Set<Pair<String, String>> getUsedPassages() {
        return usedPassages.stream()
                .map(p -> new Pair<>(this.reverseLabelMap.get(p.getFirst()),
                        this.reverseLabelMap.get(p.getSecond()))).collect(Collectors.toSet());
    }

    public Set<SimplePlace<Integer>> getIntegerUsedPlaces() {
        return usedPlaces;
    }

    public int getWindowCount() {
        return windowCount;
    }

    public List<Integer> getIntegerWindow() {
        return window.stream().map(a -> ActivityCache.getInstance().getIntForActivityId(a.getId()))
                .collect(Collectors.toList());
    }

    public Map<Integer, String> getReverseLabelMap() {
        return reverseLabelMap;
    }

    public EventLogTraceVariant getTraceVariant() {
        return traceVariant;
    }

    public Set<XTrace> getOriginalTraces() {
        return this.traces;
    }

    /**
     * Returns the position of the first event of the window in the corresponding traces
     *
     * @return position of first event (inclusive)
     */
    public int getWindowFirstEventPos() {
        return this.windowLastEventPos - this.window.size() + 1;
    }

    /**
     * Returns the position of the event where the window ends in the corresponding traces
     *
     * @return position of window end (inclusive)
     */
    public int getWindowLastEventPos() {
        return this.windowLastEventPos;
    }

    public List<Integer> getReplayedEventsIndices() {
        return replayedEventsIndices;
    }

    public List<String> getWindow() {
        List<String> window = new ArrayList<>();
        Optional<XTrace> oTrace = this.traces.stream().findFirst();
        if (!oTrace.isPresent()) throw new IllegalStateException("For each window there should be at least one trace.");
        XTrace trace = oTrace.get();
        for (int i = this.getWindowFirstEventPos(); i <= this.windowLastEventPos; ++i) {
            XAttributeLiteral eventLabel =
                    (XAttributeLiteral) trace.get(i).getAttributes().get(XConceptExtension.KEY_NAME);
            window.add(eventLabel.getValue());
        }
        return window;
    }

    public Set<SimplePlace<String>> getUsedPlaces() {
        return this.usedPlaces.stream()
                .map(p -> new SimplePlace<>(
                        p.getInputs().stream().map(reverseLabelMap::get).collect(Collectors.toSet()),
                        p.getOutputs().stream().map(reverseLabelMap::get).collect(Collectors.toSet())))
                .collect(Collectors.toSet());
    }

    public Integer getTraceVariantId() {
        return traceVariant.getId();
    }
}
