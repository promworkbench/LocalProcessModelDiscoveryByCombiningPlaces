package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMEvaluator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.PassageCoverageEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

public class PassageCoverageEvaluator implements WindowLPMEvaluator<PassageCoverageEvaluationResult> {
    @Override
    public PassageCoverageEvaluationResult evaluate(LocalProcessModel lpm,
                                                    LPMTemporaryWindowInfo lpmTemporaryWindowInfo,
                                                    LPMEvaluationResult existingEvaluation) {
        if (!(existingEvaluation instanceof PassageCoverageEvaluationResult)) {
            throw new IllegalArgumentException("The passed evaluation result should be of type " +
                    PassageCoverageEvaluationResult.class);
        }

        PassageCoverageEvaluationResult result = (PassageCoverageEvaluationResult) existingEvaluation;
        result.updatePassageCoverage(lpmTemporaryWindowInfo.getIntegerUsedPassages());
        return result;
    }

    @Override
    public String getKey() {
        return LPMEvaluatorId.PassageCoverageEvaluator.name();
    }

    @Override
    public String getResultKey() {
        return LPMEvaluationResultId.PassageCoverageEvaluationResult.name();
    }

    @Override
    public PassageCoverageEvaluationResult createEmptyResult(LocalProcessModel lpm) {
        return new PassageCoverageEvaluationResult(lpm);
    }

    @Override
    public Class<PassageCoverageEvaluationResult> getResultClass() {
        return PassageCoverageEvaluationResult.class;
    }
}