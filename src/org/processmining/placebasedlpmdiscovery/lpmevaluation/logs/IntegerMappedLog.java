package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

import java.util.*;

public class IntegerMappedLog extends AbstractRemappedActivitiesLog<Integer> {

    public IntegerMappedLog(XLog log) {
        super(log);
    }

    @Override
    protected AbstractActivityMapping<Integer> createMapping() {
        return new IntegerActivityMapping();
    }
}
