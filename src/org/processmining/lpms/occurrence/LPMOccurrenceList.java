package org.processmining.lpms.occurrence;

import org.apache.commons.math3.util.Pair;

import java.util.Arrays;

public interface LPMOccurrenceList extends Iterable<Pair<Integer, Integer>> {

    static LPMOccurrenceList standard() {
        return new StandardLPMOccurrenceList();
    }

    static LPMOccurrenceList merge(LPMOccurrenceList... occurrenceLists) {
        return Arrays.stream(occurrenceLists)
                .reduce((first, second) -> {
                    second.forEach(v -> first.push(v.getFirst(), v.getSecond()));
                    return first;
                })
                .orElse(LPMOccurrenceList.standard());
    }

    void push(int traceIndex, int eventIndex);

    int size();
}
