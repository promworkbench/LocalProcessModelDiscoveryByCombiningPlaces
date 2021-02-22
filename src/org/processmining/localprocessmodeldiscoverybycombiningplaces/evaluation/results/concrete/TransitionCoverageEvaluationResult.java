package org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.concrete;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.LPMEvaluationResultId;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.SimpleEvaluationResult;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.LocalProcessModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransitionCoverageEvaluationResult extends SimpleEvaluationResult {
    private static final long serialVersionUID = 3899013599519921214L;

    private final Map<Integer, Integer> transitionCountMap;
    private Map<Integer, Integer> transitionTotalCounts;

    /**
     * denotes the calculated transition coverage given the two maps
     */
    private double score;
    /**
     * denotes that at least one of the two maps was changed after the last calculation of score
     */
    private boolean updated;

    public TransitionCoverageEvaluationResult(LocalProcessModel lpm) {
        super(lpm);
        this.transitionCountMap = new HashMap<>();
    }

    /**
     * Updates tha map that counts in how many fitting windows a transition has appeared
     *
     * @param transitions: all transitions for which we want to update the count
     * @param windowCount: the number of windows we update
     */
    public void updateTransitionCoverageCountMap(List<Integer> transitions, int windowCount) {
        for (Integer transition : transitions) {
            Integer count = this.transitionCountMap.getOrDefault(transition, 0);
            this.transitionCountMap.put(transition, count + windowCount);
        }
        updated = true;
    }

    public void updateTotal(List<Integer> transitions, int windowCount) {
        for (Integer transition : transitions) {
            Integer count = this.transitionTotalCounts.getOrDefault(transition, 0);
            this.transitionTotalCounts.put(transition, count + windowCount);
        }
        updated = true;
    }

    public void setTransitionTotalCounts(Map<Integer, Integer> transitionTotalCounts) {
        this.transitionTotalCounts = transitionTotalCounts;
    }

    private double calculateTransitionCoverageScore() {
        if (updated) {
            double sum = 0;
            for (Integer tr : this.transitionCountMap.keySet()) {
                int count = transitionCountMap.get(tr);
                int total = transitionTotalCounts.get(tr);
                if (count > total)
                    throw new IllegalStateException("We can't have more fitting windows than total (fitting + unfitting)");
                sum += total != 0 ? count * 1.0 / total : 0;
                updated = false;
            }
            score = this.transitionCountMap.isEmpty() ? 0 : sum / this.transitionCountMap.size();
        }
        return score;
    }

    @Override
    public LPMEvaluationResultId getId() {
        return LPMEvaluationResultId.TransitionCoverageEvaluationResult;
    }

    @Override
    public double getResult() {
        return this.calculateTransitionCoverageScore();
    }
}
