package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMEvaluator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.TraceSupportEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

public class TraceSupportEvaluator implements WindowLPMEvaluator<TraceSupportEvaluationResult> {
    @Override
    public TraceSupportEvaluationResult evaluate(LocalProcessModel lpm, LPMTemporaryWindowInfo lpmTemporaryWindowInfo, LPMEvaluationResult existingEvaluation) {
        if (!(existingEvaluation instanceof TraceSupportEvaluationResult)) {
            throw new IllegalArgumentException("The passed evaluation result should be of type " +
                    TraceSupportEvaluationResult.class);
        }

        TraceSupportEvaluationResult result = (TraceSupportEvaluationResult) existingEvaluation;
        result.addTraces(lpmTemporaryWindowInfo.getTraceVariantId(), lpmTemporaryWindowInfo.getWindowCount());
        return result;
    }

    @Override
    public String getKey() {
        return LPMEvaluatorId.TraceSupportCountEvaluator.name();
    }

    @Override
    public String getResultKey() {
        return StandardLPMEvaluationResultId.TraceSupportEvaluationResult.name();
    }

    @Override
    public TraceSupportEvaluationResult createEmptyResult(LocalProcessModel lpm) {
        return new TraceSupportEvaluationResult(lpm);
    }

    @Override
    public Class<TraceSupportEvaluationResult> getResultClass() {
        return TraceSupportEvaluationResult.class;
    }
}
