package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.PassageCoverageEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.PassageRepetitionEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public class AbovePassageRepetitionThresholdLPMFilter extends NeedsEvaluationLPMFilter {

    private final double threshold;

    public AbovePassageRepetitionThresholdLPMFilter(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean shouldKeep(LocalProcessModel lpm) {
        return lpm.getAdditionalInfo().getEvaluationResult(
                LPMEvaluationResultId.PassageRepetitionEvaluationResult.name(),
                PassageRepetitionEvaluationResult.class).getResult() > threshold;
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
