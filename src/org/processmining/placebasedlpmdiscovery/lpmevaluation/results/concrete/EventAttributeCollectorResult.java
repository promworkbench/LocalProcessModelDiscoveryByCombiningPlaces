package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMCollectorResultId;

public class EventAttributeCollectorResult extends AttributeCollectorResult {

    @Override
    public LPMCollectorResultId getId() {
        return StandardLPMCollectorResultId.EventAttributeCollectorResult;
    }

}
