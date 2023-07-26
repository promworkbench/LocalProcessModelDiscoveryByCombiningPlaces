package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.TransitionsOverlappingEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public class AboveTransitionOverlappingThresholdLPMFilter extends NeedsEvaluationLPMFilter {
    private final double threshold;

    public AboveTransitionOverlappingThresholdLPMFilter(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean shouldKeep(LocalProcessModel lpm) {
        return lpm.getAdditionalInfo().getEvaluationResult(
                LPMEvaluationResultId.TransitionOverlappingEvaluationResult.name(),
                TransitionsOverlappingEvaluationResult.class).getResult() > this.threshold;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public LPMEvaluatorId getEvaluatorId() {
        return LPMEvaluatorId.TransitionOverlappingEvaluator;
    }

    @Override
    public LPMEvaluationResultId getEvaluationId() {
        return LPMEvaluationResultId.TransitionOverlappingEvaluationResult;
    }
}
