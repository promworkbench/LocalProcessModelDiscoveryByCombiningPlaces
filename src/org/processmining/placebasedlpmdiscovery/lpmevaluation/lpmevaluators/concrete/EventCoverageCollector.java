package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.EventCoverageEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

public class EventCoverageCollector implements WindowLPMCollector<EventCoverageEvaluationResult> {
    @Override
    public EventCoverageEvaluationResult evaluate(LocalProcessModel lpm,
                                                  LPMTemporaryWindowInfo lpmTemporaryWindowInfo,
                                                  LPMCollectorResult existingEvaluation) {
        if (!(existingEvaluation instanceof EventCoverageEvaluationResult)) {
            throw new IllegalArgumentException("The passed evaluation result should be of type " +
                    EventCoverageEvaluationResult.class);
        }

        EventCoverageEvaluationResult result = (EventCoverageEvaluationResult) existingEvaluation;

        if (lpmTemporaryWindowInfo.getFiringSequence().size() != lpmTemporaryWindowInfo.getReplayedEventsIndices().size()) {
            System.out.println("Something is wrong");
        }

        for (int i=0;i<lpmTemporaryWindowInfo.getFiringSequence().size();++i) {
            String activity = lpmTemporaryWindowInfo.getFiringSequence().get(i);
            if (!result.isLastCoveredEvent(activity,
                    lpmTemporaryWindowInfo.getTraceVariantId(),
                    lpmTemporaryWindowInfo.getReplayedEventsIndices().get(i))) {
                result.updateCoveredEventsCount(activity, lpmTemporaryWindowInfo.getWindowCount());
                result.updateLastCoveredEvent(activity, lpmTemporaryWindowInfo.getTraceVariantId(), lpmTemporaryWindowInfo.getReplayedEventsIndices().get(i));
            }
        }

        return result;
    }

    @Override
    public String getKey() {
        return StandardLPMEvaluatorId.EventCoverageEvaluator.name();
    }

    @Override
    public String getResultKey() {
        return StandardLPMEvaluationResultId.EventCoverageEvaluationResult.name();
    }

    @Override
    public EventCoverageEvaluationResult createEmptyResult(LocalProcessModel lpm) {
        return new EventCoverageEvaluationResult(lpm);
    }

    @Override
    public Class<EventCoverageEvaluationResult> getResultClass() {
        return EventCoverageEvaluationResult.class;
    }
}
