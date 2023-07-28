package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import org.apache.commons.math3.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LPMTemporaryWindowInfo {

    private final List<Integer> firingSequence;
    private final Set<Pair<Integer, Integer>> usedPassages;
    private int windowCount;
    private List<Integer> window;
    private Integer traceVariantId;

    private final Map<Integer, String> reverseLabelMap;

    public LPMTemporaryWindowInfo(List<Integer> firingSequence,
                                  List<Integer> window,
                                  Set<Pair<Integer, Integer>> usedPassages,
                                  Map<Integer, String> reverseLabelMap,
                                  int windowCount,
                                  Integer traceVariantId) {
        this.firingSequence = firingSequence;
        this.window = window;
        this.usedPassages = usedPassages;

        this.reverseLabelMap = reverseLabelMap;

        this.windowCount = windowCount;
        this.traceVariantId = traceVariantId;
    }

    public List<Integer> getIntegerFiringSequence() {
        return firingSequence;
    }

    public Set<Pair<Integer, Integer>> getIntegerUsedPassages() {
        return usedPassages;
    }

    public List<String> getFiringSequence() {
        return firingSequence.stream().map(this.reverseLabelMap::get).collect(Collectors.toList());
    }

    public Set<Pair<String, String>> getUsedPassages() {
        return usedPassages.stream()
                .map(p -> new Pair<>(this.reverseLabelMap.get(p.getFirst()),
                        this.reverseLabelMap.get(p.getSecond()))).collect(Collectors.toSet());
    }

    public int getWindowCount() {
        return windowCount;
    }

    public List<Integer> getWindow() {
        return window;
    }

    public Integer getTraceVariantId() {
        return traceVariantId;
    }

    public Map<Integer, String> getReverseLabelMap() {
        return reverseLabelMap;
    }
}
