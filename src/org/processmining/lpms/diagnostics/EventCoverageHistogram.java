package org.processmining.lpms.diagnostics;

import org.apache.commons.math3.util.Pair;
import org.processmining.lpms.occurrence.LPMOccurrenceList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EventCoverageHistogram {

    private final Map<Integer, Integer> distribution;
    private final int totalEvents;

    public EventCoverageHistogram(Collection<LPMOccurrenceList> occurrenceLists, int totalEvents) {
        this.totalEvents = totalEvents;

        Map<Pair<Integer, Integer>, Integer> coveragePerEvent = new HashMap<>();
        for (LPMOccurrenceList occurrenceList : occurrenceLists) {
            for (Pair<Integer, Integer> event : occurrenceList) {
                coveragePerEvent.merge(event, 1, Integer::sum);
            }
        }

        this.distribution = new HashMap<>();
        for (Integer count : coveragePerEvent.values()) {
            distribution.merge(count, 1, Integer::sum);
        }

        int coveredEvents = coveragePerEvent.size();
        distribution.put(0, totalEvents - coveredEvents);
    }

    public Map<Integer, Integer> getDistribution() {
        return distribution;
    }

    public int getTotalEvents() {
        return totalEvents;
    }
}
