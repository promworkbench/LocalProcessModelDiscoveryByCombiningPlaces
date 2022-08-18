package org.processmining.placebasedlpmdiscovery.placechooser.placetransformers;

import org.apache.commons.math3.util.Pair;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PassageUsagePlaceTransformer implements PlaceTransformer {

    private final Set<Pair<String, String>> eventuallyFollowedActivities;

    public PassageUsagePlaceTransformer(Set<Pair<String, String>> eventuallyFollowedActivities) {
        this.eventuallyFollowedActivities = eventuallyFollowedActivities;
    }

    @Override
    public Place adapt(Place place) {
        // Transitions to be removed
        Set<String> inTransitionsToBeRemoved = new HashSet<>();
        Set<String> outTransitionsToBeRemoved = new HashSet<>();

        // get all pairs of input-output transition labels
        Set<Pair<String, String>> placeInOutPair = PlaceUtils.getVisibleInoutPairsInPlace(place);
        // keep only the pairs that are not happening in the given window
        placeInOutPair.removeAll(eventuallyFollowedActivities);
        // extract all labels that appear as input in the set of uncovered input-output transition pairs
        Set<String> inputs = placeInOutPair.stream().map(Pair::getKey).collect(Collectors.toSet());
        // map every input label with which output labels doesn't appear
        Map<String, Set<String>> forwardMapping = inputs
                .stream()
                .collect(Collectors.toMap(
                        x -> x,
                        x -> placeInOutPair
                                .stream()
                                .filter(p -> p.getKey().equals(x))
                                .map(Pair::getValue)
                                .collect(Collectors.toSet())));
        // mark all input transitions which don't happen with any of the output transitions
        for (String inTr : forwardMapping.keySet()) {
            if (place.getOutputTransitions().size() > 0 &&
                    place.getOutputTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet())
                            .equals(forwardMapping.get(inTr)))
                inTransitionsToBeRemoved.add(inTr);
        }
        // extract all labels that appear as output in the set of uncovered input-output transition pairs
        Set<String> outputs = placeInOutPair.stream().map(Pair::getValue).collect(Collectors.toSet());
        // map every output label with which input labels doesn't appear
        Map<String, Set<String>> backwardMapping = outputs
                .stream()
                .collect(Collectors.toMap(
                        x -> x,
                        x -> placeInOutPair
                                .stream()
                                .filter(p -> p.getValue().equals(x))
                                .map(Pair::getKey)
                                .collect(Collectors.toSet())));
        // mark all output transitions which don't happen with any of the input transitions
        for (String outTr : backwardMapping.keySet()) {
            if (place.getInputTransitions().size() > 0 &&
                    place.getInputTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet())
                            .equals(backwardMapping.get(outTr)))
                outTransitionsToBeRemoved.add(outTr);
        }

        // remove the transitions from the place
        for (String label : inTransitionsToBeRemoved)
            place.removeTransitions(label, true);
        for (String label : outTransitionsToBeRemoved)
            place.removeTransitions(label, false);

        return place;
    }
}
