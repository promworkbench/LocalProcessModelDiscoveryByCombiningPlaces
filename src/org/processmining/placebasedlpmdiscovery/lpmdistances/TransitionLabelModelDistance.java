package org.processmining.placebasedlpmdiscovery.lpmdistances;

import com.google.common.collect.Sets;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.util.Set;
import java.util.stream.Collectors;

public class TransitionLabelModelDistance implements ModelDistance {
    @Override
    public double calculateDistance(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        Set<String> lpm1Labels = lpm1.getTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet());
        Set<String> lpm2Labels = lpm2.getTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet());
        int intersectionSize = Sets.intersection(lpm1Labels, lpm2Labels).size();
        return  2.0 * intersectionSize / (lpm1Labels.size() + lpm2Labels.size());
    }
}
