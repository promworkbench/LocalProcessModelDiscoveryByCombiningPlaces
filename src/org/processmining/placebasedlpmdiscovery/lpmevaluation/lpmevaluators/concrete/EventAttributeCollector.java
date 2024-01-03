package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.EventAttributeCollectorResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

public class EventAttributeCollector implements WindowLPMCollector<EventAttributeCollectorResult> {
    @Override
    public EventAttributeCollectorResult evaluate(LocalProcessModel lpm, LPMTemporaryWindowInfo lpmTemporaryWindowInfo, LPMEvaluationResult existingEvaluation) {
        return null;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public String getResultKey() {
        return null;
    }

    @Override
    public EventAttributeCollectorResult createEmptyResult(LocalProcessModel lpm) {
        return null;
    }

    @Override
    public Class<EventAttributeCollectorResult> getResultClass() {
        return null;
    }
}
