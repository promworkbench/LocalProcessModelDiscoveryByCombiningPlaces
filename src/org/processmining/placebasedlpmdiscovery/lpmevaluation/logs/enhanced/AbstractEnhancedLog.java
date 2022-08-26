package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced;

import org.deckfour.xes.model.XLog;

public abstract class AbstractEnhancedLog {

    protected XLog originalLog;

    public AbstractEnhancedLog(XLog log) {
        this.originalLog = log;
        this.makeLog();
    }

    protected abstract void makeLog();
}
