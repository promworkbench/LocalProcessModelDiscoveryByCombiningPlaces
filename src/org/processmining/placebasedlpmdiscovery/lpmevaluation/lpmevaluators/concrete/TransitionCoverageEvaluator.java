package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMEvaluator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.PassageCoverageEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.TransitionCoverageEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

public class TransitionCoverageEvaluator implements WindowLPMEvaluator<TransitionCoverageEvaluationResult> {
    @Override
    public TransitionCoverageEvaluationResult evaluate(LocalProcessModel lpm, LPMTemporaryWindowInfo lpmTemporaryWindowInfo, LPMEvaluationResult existingEvaluation) {
        if (!(existingEvaluation instanceof TransitionCoverageEvaluationResult)) {
            throw new IllegalArgumentException("The passed evaluation result should be of type " +
                    TransitionCoverageEvaluationResult.class);
        }

        TransitionCoverageEvaluationResult result = (TransitionCoverageEvaluationResult) existingEvaluation;
        result.updateTransitionCoverageCountMap(
                lpmTemporaryWindowInfo.getIntegerFiringSequence(),
                lpmTemporaryWindowInfo.getWindow(),
                lpmTemporaryWindowInfo.getWindowCount(),
                lpmTemporaryWindowInfo.getReverseLabelMap());
        return result;
    }

    @Override
    public String getKey() {
        return LPMEvaluatorId.TransitionCoverageEvaluator.name();
    }

    @Override
    public String getResultKey() {
        return LPMEvaluationResultId.TransitionCoverageEvaluationResult.name();
    }

    @Override
    public TransitionCoverageEvaluationResult createEmptyResult(LocalProcessModel lpm) {
        return new TransitionCoverageEvaluationResult(lpm);
    }

    @Override
    public Class<TransitionCoverageEvaluationResult> getResultClass() {
        return TransitionCoverageEvaluationResult.class;
    }
}
