package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.FittingWindowsEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

public class FittingWindowCollector implements WindowLPMCollector<FittingWindowsEvaluationResult> {

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
        return StandardLPMEvaluatorId.FittingWindowEvaluator.name();
    }

    @Override
    public String getResultKey() {
        return StandardLPMEvaluationResultId.FittingWindowsEvaluationResult.name();
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
