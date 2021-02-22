package org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.filterstrategies.lpms;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.lpmevaluators.LPMEvaluatorId;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.LPMEvaluationResultId;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.LocalProcessModel;

public class AbovePassageRepetitionThresholdLPMFilter extends NeedsEvaluationLPMFilter {

    private final double threshold;

    public AbovePassageRepetitionThresholdLPMFilter(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean shouldKeep(LocalProcessModel lpm) {
        return lpm.getAdditionalInfo().getEvaluationResult()
                .getEvaluationResult(LPMEvaluationResultId.PassageRepetitionEvaluationResult).getResult() > threshold;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public LPMEvaluatorId getEvaluatorId() {
        return LPMEvaluatorId.PassageRepetitionEvaluator;
    }

    @Override
    public LPMEvaluationResultId getEvaluationId() {
        return LPMEvaluationResultId.PassageRepetitionEvaluationResult;
    }
}
