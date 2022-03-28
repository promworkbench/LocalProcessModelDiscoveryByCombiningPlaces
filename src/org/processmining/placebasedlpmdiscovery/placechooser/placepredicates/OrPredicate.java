package org.processmining.placebasedlpmdiscovery.placechooser.placepredicates;

import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class OrPredicate implements PlacePredicate {
    Collection<PlacePredicate> predicates;

    public OrPredicate (PlacePredicate ... predicates) {
        this.predicates = new ArrayList<>();
        this.predicates.addAll(Arrays.asList(predicates));
    }
    @Override
    public boolean filter(Place place) {
        for (PlacePredicate predicate : predicates) {
            if (predicate.filter(place)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean test(Place place) {
        return filter(place);
    }
}
