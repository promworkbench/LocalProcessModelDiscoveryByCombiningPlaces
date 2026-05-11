package org.processmining.lpms.diagnostics;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TransitionUsageHistogram {

    private final Map<String, Integer> histogram;
    private final int totalLPMs;

    public TransitionUsageHistogram(Collection<LocalProcessModel> lpms) {
        this.histogram = new HashMap<>();
        this.totalLPMs = lpms.size();
        for (LocalProcessModel lpm : lpms) {
            for (Transition t : lpm.getVisibleTransitions()) {
                histogram.merge(t.getLabel(), 1, Integer::sum);
            }
        }
    }

    public Map<String, Integer> getHistogram() {
        return histogram;
    }

    public int getTotalLPMs() {
        return totalLPMs;
    }

    public int getCount(String activityLabel) {
        return histogram.getOrDefault(activityLabel, 0);
    }
}
