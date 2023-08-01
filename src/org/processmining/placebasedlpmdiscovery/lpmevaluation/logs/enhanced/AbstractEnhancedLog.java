package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced;

import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractEnhancedLog {

    protected XLog originalLog;

    public AbstractEnhancedLog(XLog log) {
        this.originalLog = log;
        this.makeLog();
    }

    protected abstract void makeLog();
}
