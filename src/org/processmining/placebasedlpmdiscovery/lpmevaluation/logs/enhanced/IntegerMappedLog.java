package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.extra.AbstractActivityMapping;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.extra.IntegerActivityMapping;

public class IntegerMappedLog extends AbstractRemappedActivitiesLog<Integer> {

    public IntegerMappedLog(XLog log) {
        super(log);
    }

    @Override
    protected AbstractActivityMapping<Integer> createMapping() {
        return new IntegerActivityMapping();
    }
}
