package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.apache.commons.math3.util.Pair;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMCollectorResultId;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PassageVisitsCollectorResult implements LPMCollectorResult {

    private final Map<Pair<Integer, Integer>, Integer> passageCount;
    private final Map<Activity, Integer> firstTransitionCount;
    private final Map<Activity, Integer> lastTransitionCount;

    public PassageVisitsCollectorResult(LocalProcessModel lpm) {
        this.passageCount = new HashMap<>();
        this.firstTransitionCount = new HashMap<>();
        this.lastTransitionCount = new HashMap<>();
    }

    @Override
    public LPMCollectorResultId getId() {
        return StandardLPMCollectorResultId.PassageVisitsCollectorResult;
    }

    public void updatePassagesUsed(Set<Pair<Integer, Integer>> usedPassages, int windowCount) {
        for (Pair<Integer, Integer> passage : usedPassages) {
            int count = this.passageCount.getOrDefault(passage, 0);
            this.passageCount.put(passage, count + windowCount);
        }
    }

    public void updateFirstPassage(Activity firstTransition, int windowCount) {
        int count = this.firstTransitionCount.getOrDefault(firstTransition, 0);
        this.firstTransitionCount.put(firstTransition, count + windowCount);
    }

    public void updateLastPassage(Activity lastTransition, int windowCount) {
        int count = this.lastTransitionCount.getOrDefault(lastTransition, 0);
        this.lastTransitionCount.put(lastTransition, count + windowCount);
    }
}
