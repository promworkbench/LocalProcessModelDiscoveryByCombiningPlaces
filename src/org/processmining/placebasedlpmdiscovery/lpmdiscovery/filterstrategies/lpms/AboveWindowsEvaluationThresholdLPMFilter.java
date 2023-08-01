package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.FittingWindowsEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public class AboveWindowsEvaluationThresholdLPMFilter extends NeedsEvaluationLPMFilter {

    private final double evaluationThreshold;

    public AboveWindowsEvaluationThresholdLPMFilter(double evaluationThreshold) {
        this.evaluationThreshold = evaluationThreshold;
    }

    @Override
    public boolean shouldKeep(LocalProcessModel lpm) {
        return lpm.getAdditionalInfo().getEvaluationResult(
                LPMEvaluationResultId.FittingWindowsEvaluationResult.name(),
                FittingWindowsEvaluationResult.class).getResult() >= this.evaluationThreshold;
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public LPMEvaluatorId getEvaluatorId() {
        return LPMEvaluatorId.FittingWindowEvaluator;
    }

    @Override
    public LPMEvaluationResultId getEvaluationId() {
        return LPMEvaluationResultId.FittingWindowsEvaluationResult;
    }
}
