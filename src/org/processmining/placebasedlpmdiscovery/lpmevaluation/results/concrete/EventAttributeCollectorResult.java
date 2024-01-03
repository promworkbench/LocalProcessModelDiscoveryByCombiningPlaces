package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMCollectorResultId;

public class EventAttributeCollectorResult implements LPMCollectorResult {
    @Override
    public LPMCollectorResultId getId() {
        return StandardLPMCollectorResultId.EventAttributeCollectorResult;
    }
}
