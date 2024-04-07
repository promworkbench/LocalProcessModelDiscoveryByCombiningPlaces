package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.TraceSupportEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

public class TraceSupportCollector implements WindowLPMCollector<TraceSupportEvaluationResult> {
    @Override
    public TraceSupportEvaluationResult evaluate(LocalProcessModel lpm,
                                                 LPMTemporaryWindowInfo lpmTemporaryWindowInfo,
                                                 LPMCollectorResult existingEvaluation) {
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
        return StandardLPMEvaluatorId.TraceSupportCountEvaluator.name();
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
