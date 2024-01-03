package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMCollectorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMCollectorResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.EventAttributeCollectorResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

public class EventAttributeCollector implements WindowLPMCollector<EventAttributeCollectorResult> {
    @Override
    public EventAttributeCollectorResult evaluate(LocalProcessModel lpm,
                                                  LPMTemporaryWindowInfo lpmTemporaryWindowInfo,
                                                  LPMEvaluationResult existingEvaluation) {
        return null;
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
