package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.functional;

public class FunctionalLog<T extends CompositeLog> {

    protected T log;

    public FunctionalLog(T log) {
        this.log = log;
    }
}
