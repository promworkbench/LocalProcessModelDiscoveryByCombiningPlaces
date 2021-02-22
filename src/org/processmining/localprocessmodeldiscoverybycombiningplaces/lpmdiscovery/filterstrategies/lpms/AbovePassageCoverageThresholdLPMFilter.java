package org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.filterstrategies.lpms;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.lpmevaluators.LPMEvaluatorId;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.LPMEvaluationResultId;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.concrete.PassageCoverageEvaluationResult;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.LocalProcessModel;

public class AbovePassageCoverageThresholdLPMFilter extends NeedsEvaluationLPMFilter {

    private final double threshold;

    public AbovePassageCoverageThresholdLPMFilter(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean shouldKeep(LocalProcessModel lpm) {
        return lpm.getAdditionalInfo().getEvaluationResult()
                .getSimpleEvaluationResult(PassageCoverageEvaluationResult.class).getResult() > threshold;
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
