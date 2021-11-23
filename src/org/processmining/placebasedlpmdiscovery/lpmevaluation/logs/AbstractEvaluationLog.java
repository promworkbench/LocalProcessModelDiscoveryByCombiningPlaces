package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.deckfour.xes.model.XLog;

public abstract class AbstractEvaluationLog {

    protected XLog originalLog;

    public AbstractEvaluationLog(XLog log) {
        this.originalLog = log;
        this.makeLog();
    }

    protected abstract void makeLog();
}
