package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.LiftEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

public class LiftCollector implements WindowLPMCollector<LiftEvaluationResult> {

    @Override
    public LiftEvaluationResult evaluate(LocalProcessModel lpm,
                                         LPMTemporaryWindowInfo lpmTemporaryWindowInfo,
                                         LPMCollectorResult existingEvaluation) {
        if (!(existingEvaluation instanceof LiftEvaluationResult)) {
            throw new IllegalArgumentException("The passed evaluation result should be of type " +
                    LiftEvaluationResult.class);
        }

        LiftEvaluationResult result = (LiftEvaluationResult) existingEvaluation;

        // the same LPM can be reported more than once for a window (e.g. also built from a branch combination),
        // but the lift counts windows, so each window is counted once
        int firstPos = lpmTemporaryWindowInfo.getWindowFirstEventPos();
        int lastPos = lpmTemporaryWindowInfo.getWindowLastEventPos();
        if (!result.isLastCountedWindow(lpmTemporaryWindowInfo.getTraceVariant(), firstPos, lastPos)) {
            result.updateCount(lpmTemporaryWindowInfo.getWindowCount());
            result.updateLastCountedWindow(lpmTemporaryWindowInfo.getTraceVariant(), firstPos, lastPos);
        }
        return result;
    }

    @Override
    public String getKey() {
        return StandardLPMEvaluatorId.LiftEvaluator.name();
    }

    @Override
    public String getResultKey() {
        return StandardLPMEvaluationResultId.LiftEvaluationResult.name();
    }

    @Override
    public LiftEvaluationResult createEmptyResult(LocalProcessModel lpm) {
        return new LiftEvaluationResult(lpm);
    }

    @Override
    public Class<LiftEvaluationResult> getResultClass() {
        return LiftEvaluationResult.class;
    }
}
