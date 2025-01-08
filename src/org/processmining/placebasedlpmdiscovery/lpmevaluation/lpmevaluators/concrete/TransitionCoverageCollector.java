package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.TransitionCoverageEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

import java.util.stream.Collectors;

public class TransitionCoverageCollector implements WindowLPMCollector<TransitionCoverageEvaluationResult> {
    @Override
    public TransitionCoverageEvaluationResult evaluate(LocalProcessModel lpm,
                                                       LPMTemporaryWindowInfo lpmTemporaryWindowInfo,
                                                       LPMCollectorResult existingEvaluation) {
        if (!(existingEvaluation instanceof TransitionCoverageEvaluationResult)) {
            throw new IllegalArgumentException("The passed evaluation result should be of type " +
                    TransitionCoverageEvaluationResult.class);
        }

        TransitionCoverageEvaluationResult result = (TransitionCoverageEvaluationResult) existingEvaluation;
        result.updateTransitionCoverageCountMap(
                lpmTemporaryWindowInfo.getActivityFiringSequence().stream().map(Activity::getName).collect(Collectors.toList()),
                lpmTemporaryWindowInfo.getWindow(),
                lpmTemporaryWindowInfo.getWindowCount()
        );
        return result;
    }

    @Override
    public String getKey() {
        return StandardLPMEvaluatorId.TransitionCoverageEvaluator.name();
    }

    @Override
    public String getResultKey() {
        return StandardLPMEvaluationResultId.TransitionCoverageEvaluationResult.name();
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
