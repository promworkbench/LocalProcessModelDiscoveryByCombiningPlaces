package org.processmining.placebasedlpmdiscovery.placechooser.placepredicates;

import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.function.Predicate;

public interface PlacePredicate extends Predicate<Place> {

    /**
     * Returns whether a certain predicate is satisfied for a given place
     * @param place that we want to check for filtering
     * @return true when the predicate is satisfied and false otherwise
     */
    boolean filter(Place place);

    @Override
    default boolean test(Place place) {
        return filter(place);
    }
}
