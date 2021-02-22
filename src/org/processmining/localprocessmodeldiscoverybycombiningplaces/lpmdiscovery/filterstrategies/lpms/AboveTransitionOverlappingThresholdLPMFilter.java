package org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.filterstrategies.lpms;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.lpmevaluators.LPMEvaluatorId;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.LPMEvaluationResultId;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.concrete.TransitionsOverlappingEvaluationResult;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.LocalProcessModel;

public class AboveTransitionOverlappingThresholdLPMFilter extends NeedsEvaluationLPMFilter {
    private final double threshold;

    public AboveTransitionOverlappingThresholdLPMFilter(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean shouldKeep(LocalProcessModel lpm) {
        return lpm.getAdditionalInfo().getEvaluationResult()
                .getSimpleEvaluationResult(TransitionsOverlappingEvaluationResult.class).getResult() > this.threshold;
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
