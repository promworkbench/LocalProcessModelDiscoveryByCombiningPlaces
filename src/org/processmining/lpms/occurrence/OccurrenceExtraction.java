package org.processmining.lpms.occurrence;


import org.processmining.plugins.petrinet.replayresult.PNMatchInstancesRepResult;

public interface OccurrenceExtraction<T> {

    @SuppressWarnings("unchecked")
    static <T> OccurrenceExtraction<T> consider(Class<T> tClass) {
        if (tClass.equals(PNMatchInstancesRepResult.class)) {
            return (OccurrenceExtraction<T>) new PNMatchInstOccurrenceExtraction();
        }
        throw new IllegalArgumentException("Unsupported class: " + tClass);
    }

    LPMOccurrenceList from(T result);
}
