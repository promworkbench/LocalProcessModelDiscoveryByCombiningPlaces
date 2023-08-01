package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.SimpleEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransitionCoverageEvaluationResult extends SimpleEvaluationResult {
    private static final long serialVersionUID = 3899013599519921214L;

    private final Map<String, Integer> transitionCountMap;
    private final Map<String, Integer> transitionTotalCounts;

    /**
     * denotes the calculated transition coverage given the two maps
     */
    private double score;
    /**
     * denotes that at least one of the two maps was changed after the last calculation of score
     */
    private boolean updated;

    public TransitionCoverageEvaluationResult(LocalProcessModel lpm) {
        super(lpm, LPMEvaluationResultId.TransitionCoverageEvaluationResult);
        this.transitionCountMap = new HashMap<>();
        this.transitionTotalCounts = new HashMap<>();
    }

    /**
     * Updates tha map that counts in how many fitting windows a transition has appeared
     *
     * @param usedTransitions: all usedTransitions for which we want to update the count
     * @param allTransitions: all activities that appear in the window
     * @param windowCount: the number of windows we update
     */
    public void updateTransitionCoverageCountMap(List<String> usedTransitions,
                                                 List<String> allTransitions,
                                                 int windowCount) {
        for (String transition : usedTransitions) {
            Integer count = this.transitionCountMap.getOrDefault(transition, 0);
            this.transitionCountMap.put(transition, count + windowCount);
        }
        for (String transition : allTransitions) {
            Integer count = this.transitionTotalCounts.getOrDefault(transition, 0);
            this.transitionTotalCounts.put(transition, count + windowCount);
        }
        updated = true;
    }

    private double calculateTransitionCoverageScore() {
        if (updated) {
            double sum = 0;
            int countTr = 0;
            for (Transition tr : lpm.getTransitions()) {
                if (!tr.isInvisible()) {
                    int count = transitionCountMap.getOrDefault(tr.getLabel(), 0);
                    int total = transitionTotalCounts.getOrDefault(tr.getLabel(), 0);
                    countTr++;
                    sum += total != 0 ? count * 1.0 / total : 0;
                }
            }
            updated = false;
            score = countTr == 0 ? 0 : sum / countTr;
        }
        return score;
    }

    @Override
    public double getResult() {
        return this.calculateTransitionCoverageScore();
    }

    @Override
    public double getNormalizedResult() {
        return this.getResult();
    }
}
