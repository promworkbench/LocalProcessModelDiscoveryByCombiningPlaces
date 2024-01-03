package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.deckfour.xes.model.XTrace;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMCollectorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMCollectorResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.EventAttributeCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.EventCoverageEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.EventAttributeSummaryController;

public class EventAttributeCollector implements WindowLPMCollector<EventAttributeCollectorResult> {
    @Override
    public EventAttributeCollectorResult evaluate(LocalProcessModel lpm,
                                                  LPMTemporaryWindowInfo lpmTemporaryWindowInfo,
                                                  LPMEvaluationResult existingEvaluation) {

        if (!(existingEvaluation instanceof EventAttributeCollectorResult)) {
            throw new IllegalArgumentException("The passed evaluation result should be of type " +
                    EventAttributeCollectorResult.class);
        }

        EventAttributeCollectorResult result = (EventAttributeCollectorResult) existingEvaluation;

        EventAttributeSummaryController eaSummaryController = new EventAttributeSummaryController();

        for (XTrace trace : lpmTemporaryWindowInfo.getOriginalTraces()) {
            for (int replayIndex : lpmTemporaryWindowInfo.getReplayedEventsIndices()) {
                eaSummaryController.computeEventAttributeSummary(result, trace.get(replayIndex));
            }
        }
        return result;
    }

    @Override
    public String getKey() {
        return StandardLPMCollectorId.EventAttributeCollector.name();
    }

    @Override
    public String getResultKey() {
        return StandardLPMCollectorResultId.EventAttributeCollectorResult.name();
    }

    @Override
    public EventAttributeCollectorResult createEmptyResult(LocalProcessModel lpm) {
        return new EventAttributeCollectorResult();
    }

    @Override
    public Class<EventAttributeCollectorResult> getResultClass() {
        return EventAttributeCollectorResult.class;
    }
}
