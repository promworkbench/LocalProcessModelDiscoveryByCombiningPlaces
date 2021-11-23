package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.SimpleEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class TransitionCoverageEvaluationResult extends SimpleEvaluationResult {
    private static final long serialVersionUID = 3899013599519921214L;

    private final Map<Integer, Integer> transitionCountMap;
    private Map<Integer, Integer> transitionTotalCounts;
    private int total;

    /**
     * denotes the calculated transition coverage given the two maps
     */
    private double score;
    /**
     * denotes that at least one of the two maps was changed after the last calculation of score
     */
    private boolean updated;
    private int transitionsCount;
    private Map<String, Integer> labelMap = new HashMap<>();

    public TransitionCoverageEvaluationResult(LocalProcessModel lpm, Map<String, Integer> labelMap) {
        super(lpm);
        this.transitionCountMap = new HashMap<>();
        this.transitionTotalCounts = new HashMap<>();
        this.total = 0;
        this.labelMap = labelMap;
    }

    /**
     * Updates tha map that counts in how many fitting windows a transition has appeared
     *
     * @param usedTransitions: all usedTransitions for which we want to update the count
     * @param windowCount:     the number of windows we update
     */
    public void updateTransitionCoverageCountMap(List<Integer> usedTransitions, List<Integer> allTransitions, int windowCount) {
        for (Integer transition : usedTransitions) {
            Integer count = this.transitionCountMap.getOrDefault(transition, 0);
            this.transitionCountMap.put(transition, count + windowCount);
        }
        for (Integer transition : allTransitions) {
            Integer count = this.transitionTotalCounts.getOrDefault(transition, 0);
            this.transitionTotalCounts.put(transition, count + windowCount);
        }
        this.total += windowCount;
        updated = true;
    }

    public void updateTotal(List<Integer> transitions, int windowCount) {
        for (Integer transition : new HashSet<>(transitions)) {
            Integer count = this.transitionTotalCounts.getOrDefault(transition, 0);
//            this.transitionTotalCounts.put(transition, count + windowCount);
        }
        updated = true;
    }

    public void setTransitionTotalCounts(Map<Integer, Integer> transitionTotalCounts) {
        this.transitionTotalCounts = transitionTotalCounts;
    }

    private double calculateTransitionCoverageScore() {
        if (updated) {
            double sum = 0;
            int countTr = 0;
            for (Transition tr : lpm.getTransitions()) {
                if (!tr.isInvisible()) {
                    int count = transitionCountMap.getOrDefault(labelMap.get(tr.getLabel()), 0);
                    int total = transitionTotalCounts.getOrDefault(labelMap.get(tr.getLabel()), 0);
                    countTr++;
//                if (count > total)
//                    throw new IllegalStateException("We can't have more fitting windows than total (fitting + unfitting)");
                    sum += total != 0 ? count * 1.0 / total : 0;
                }
            }
            updated = false;
            score = countTr == 0 ? 0 : sum / countTr;
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

    @Override
    public double getNormalizedResult() {
        return this.getResult();
    }

    public int getTransitionsCount() {
        return transitionsCount;
    }

    public void setTransitionsCount(int transitionsCount) {
        this.transitionsCount = transitionsCount;
    }
}
