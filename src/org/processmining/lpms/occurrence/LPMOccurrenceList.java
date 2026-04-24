package org.processmining.lpms.occurrence;

import org.apache.commons.math3.util.Pair;

public interface LPMOccurrenceList extends Iterable<Pair<Integer, Integer>> {

    static LPMOccurrenceList standard() {
        return new StandardLPMOccurrenceList();
    }

    void push(int traceIndex, int eventIndex);

    int size();
}
