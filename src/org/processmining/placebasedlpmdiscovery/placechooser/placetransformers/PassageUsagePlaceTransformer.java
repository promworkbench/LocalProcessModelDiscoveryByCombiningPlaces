package org.processmining.placebasedlpmdiscovery.placechooser.placetransformers;

import org.apache.commons.math3.util.Pair;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.util.Set;
import java.util.stream.Collectors;

public class PassageUsagePlaceTransformer implements PlaceTransformer {

    private final Set<Pair<String, String>> locallyOccurringPassages;

    public PassageUsagePlaceTransformer(Set<Pair<String, String>> locallyOccurringPassages) {
        this.locallyOccurringPassages = locallyOccurringPassages;
    }

    @Override
    public Place adapt(Place place) {
        // used input transitions given the proximity
        Set<String> usedInputTransitionLabels =
                locallyOccurringPassages.stream().map(Pair::getKey).collect(Collectors.toSet());
        // used output transitions given the proximity
        Set<String> usedOutputTransitionLabels =
                locallyOccurringPassages.stream().map(Pair::getValue).collect(Collectors.toSet());

        // unused input transition labels
        Set<String> unusedInputTransitionLabels = place.getInputTransitions().stream()
                .map(Transition::getLabel)
                .filter(label -> !usedInputTransitionLabels.contains(label))
                .collect(Collectors.toSet());
        // unused output transition labels
        Set<String> unusedOutputTransitionLabels = place.getOutputTransitions().stream()
                .map(Transition::getLabel)
                .filter(label -> !usedOutputTransitionLabels.contains(label))
                .collect(Collectors.toSet());

        // remove unused input transitions
        unusedInputTransitionLabels.forEach(label -> place.removeTransitions(label, true));
        // remove unused output transitions
        unusedOutputTransitionLabels.forEach(label -> place.removeTransitions(label, false));

        return place;
    }
}
