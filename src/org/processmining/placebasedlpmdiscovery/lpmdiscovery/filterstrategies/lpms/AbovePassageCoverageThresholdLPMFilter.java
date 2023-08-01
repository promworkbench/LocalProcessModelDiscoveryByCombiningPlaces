package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.PassageCoverageEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public class AbovePassageCoverageThresholdLPMFilter extends NeedsEvaluationLPMFilter {

    private final double threshold;

    public AbovePassageCoverageThresholdLPMFilter(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean shouldKeep(LocalProcessModel lpm) {
        return lpm.getAdditionalInfo()
                .getEvaluationResult(
                        LPMEvaluationResultId.PassageCoverageEvaluationResult.name(),
                        PassageCoverageEvaluationResult.class).getResult() > threshold;
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public LPMEvaluatorId getEvaluatorId() {
        return LPMEvaluatorId.PassageCoverageEvaluator;
    }

    @Override
    public LPMEvaluationResultId getEvaluationId() {
        return LPMEvaluationResultId.PassageCoverageEvaluationResult;
    }
}
