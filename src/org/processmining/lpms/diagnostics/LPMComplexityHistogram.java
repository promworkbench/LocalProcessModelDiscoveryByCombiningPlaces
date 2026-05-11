package org.processmining.lpms.diagnostics;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LPMComplexityHistogram {

    private final Map<Integer, Integer> placeCountDistribution;
    private final Map<Integer, Integer> transitionCountDistribution;

    public LPMComplexityHistogram(Collection<LocalProcessModel> lpms) {
        this.placeCountDistribution = new HashMap<>();
        this.transitionCountDistribution = new HashMap<>();
        for (LocalProcessModel lpm : lpms) {
            placeCountDistribution.merge(lpm.getPlaces().size(), 1, Integer::sum);
            transitionCountDistribution.merge(lpm.getVisibleTransitions().size(), 1, Integer::sum);
        }
    }

    public Map<Integer, Integer> getPlaceCountDistribution() {
        return placeCountDistribution;
    }

    public Map<Integer, Integer> getTransitionCountDistribution() {
        return transitionCountDistribution;
    }
}
