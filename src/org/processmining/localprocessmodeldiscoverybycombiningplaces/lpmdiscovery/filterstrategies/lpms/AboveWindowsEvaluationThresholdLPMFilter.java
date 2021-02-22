package org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.filterstrategies.lpms;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.lpmevaluators.LPMEvaluatorId;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.LPMEvaluationResultId;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.concrete.FittingWindowsEvaluationResult;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.LocalProcessModel;

public class AboveWindowsEvaluationThresholdLPMFilter extends NeedsEvaluationLPMFilter {

    private final double evaluationThreshold;

    public AboveWindowsEvaluationThresholdLPMFilter(double evaluationThreshold) {
        this.evaluationThreshold = evaluationThreshold;
    }

    @Override
    public boolean shouldKeep(LocalProcessModel lpm) {
        return lpm.getAdditionalInfo().getEvaluationResult().
                getSimpleEvaluationResult(FittingWindowsEvaluationResult.class).getResult()
                >= this.evaluationThreshold;
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public LPMEvaluatorId getEvaluatorId() {
        return LPMEvaluatorId.WindowEvaluator;
    }

    @Override
    public LPMEvaluationResultId getEvaluationId() {
        return LPMEvaluationResultId.FittingWindowsEvaluationResult;
    }
}
