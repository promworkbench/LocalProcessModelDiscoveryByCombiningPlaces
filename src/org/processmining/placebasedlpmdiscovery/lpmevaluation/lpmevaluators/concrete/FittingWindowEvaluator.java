package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMEvaluator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.FittingWindowsEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

public class FittingWindowEvaluator implements WindowLPMEvaluator<FittingWindowsEvaluationResult> {

    @Override
    public FittingWindowsEvaluationResult evaluate(LocalProcessModel lpm,
                                                   LPMTemporaryWindowInfo lpmTemporaryWindowInfo,
                                                   LPMEvaluationResult existingEvaluation) {
        if (!(existingEvaluation instanceof FittingWindowsEvaluationResult)) {
            throw new IllegalArgumentException("The passed evaluation result should be of type " +
                    FittingWindowsEvaluationResult.class);
        }

        FittingWindowsEvaluationResult result = (FittingWindowsEvaluationResult) existingEvaluation;
        result.updateCount(lpmTemporaryWindowInfo.getWindowCount());
        result.updateWeightedCount(1.0 * lpmTemporaryWindowInfo.getIntegerFiringSequence().size() *
                lpmTemporaryWindowInfo.getWindowCount() / lpmTemporaryWindowInfo.getIntegerWindow().size());

        return result;
    }

    @Override
    public String getKey() {
        return LPMEvaluatorId.FittingWindowEvaluator.name();
    }

    @Override
    public String getResultKey() {
        return LPMEvaluationResultId.FittingWindowsEvaluationResult.name();
    }

    @Override
    public FittingWindowsEvaluationResult createEmptyResult(LocalProcessModel lpm) {
        return new FittingWindowsEvaluationResult(lpm);
    }

    @Override
    public Class<FittingWindowsEvaluationResult> getResultClass() {
        return FittingWindowsEvaluationResult.class;
    }
}
