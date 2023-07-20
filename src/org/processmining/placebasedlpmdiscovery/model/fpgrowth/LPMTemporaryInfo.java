package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import org.apache.commons.math3.util.Pair;

import java.util.List;
import java.util.Set;

public class LPMTemporaryInfo {

    private final List<Integer> firingSequence;
    private final Set<Pair<Integer, Integer>> usedPassages;
    private int count;
    private List<Integer> window;
    private Integer traceVariantId;

    public LPMTemporaryInfo(List<Integer> firingSequence, Set<Pair<Integer, Integer>> usedPassages) {
        this.firingSequence = firingSequence;
        this.usedPassages = usedPassages;
    }

    public List<Integer> getFiringSequence() {
        return firingSequence;
    }

    public Set<Pair<Integer, Integer>> getUsedPassages() {
        return usedPassages;
    }
}
