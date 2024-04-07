package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.PassageCoverageEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

public class PassageCoverageCollector implements WindowLPMCollector<PassageCoverageEvaluationResult> {
    @Override
    public PassageCoverageEvaluationResult evaluate(LocalProcessModel lpm,
                                                    LPMTemporaryWindowInfo lpmTemporaryWindowInfo,
                                                    LPMCollectorResult existingEvaluation) {
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
        return StandardLPMEvaluatorId.PassageCoverageEvaluator.name();
    }

    @Override
    public String getResultKey() {
        return StandardLPMEvaluationResultId.PassageCoverageEvaluationResult.name();
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
