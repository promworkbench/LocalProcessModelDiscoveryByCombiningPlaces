package org.processmining.placebasedlpmdiscovery.placechooser.placetransformers;

import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LEFRMatrix;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.util.Set;
import java.util.stream.Collectors;

public class PassageUsagePlaceTransformer implements PlaceTransformer {

    //    private final Set<Pair<String, String>> locallyOccurringPassages;
    private final LEFRMatrix lefrMatrix;

    public PassageUsagePlaceTransformer(LEFRMatrix lefrMatrix) {
        this.lefrMatrix = lefrMatrix;
    }

    @Override
    public Place adapt(Place place) {
        // unused input transition labels
        Set<String> unusedInputTransitionLabels = place.getInputTransitions()
                .stream()
                .map(Transition::getLabel)
                .filter(ti -> place.getOutputTransitions().stream().map(Transition::getLabel)
                        .noneMatch(to -> lefrMatrix.get(ti, to) > 0))
                .collect(Collectors.toSet());
        // unused output transition labels
        Set<String> unusedOutputTransitionLabels = place.getOutputTransitions()
                .stream()
                .map(Transition::getLabel)
                .filter(to -> place.getInputTransitions().stream().map(Transition::getLabel)
                        .noneMatch(ti -> lefrMatrix.get(ti, to) > 0))
                .collect(Collectors.toSet());

        // remove unused input transitions
        unusedInputTransitionLabels.forEach(label -> place.removeTransitions(label, true));
        // remove unused output transitions
        unusedOutputTransitionLabels.forEach(label -> place.removeTransitions(label, false));

        return place;
    }
}
