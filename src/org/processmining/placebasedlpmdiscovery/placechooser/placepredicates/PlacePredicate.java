package org.processmining.placebasedlpmdiscovery.placechooser.placepredicates;

import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.function.Predicate;

/**
 * A boolean condition on a {@link Place} used to filter candidates in a
 * {@link org.processmining.placebasedlpmdiscovery.placechooser.PlaceChooser PlaceChooser} pipeline.
 *
 * <p>Implement {@link #testPlace} with the filtering logic. If the predicate returns {@code false},
 * the place is immediately discarded and no later pipeline steps run for it.
 */
public interface PlacePredicate extends Predicate<Place> {

    /**
     * Returns {@code true} if the place satisfies this predicate and should proceed through the pipeline.
     *
     * @param place the place to evaluate
     * @return {@code true} if the place satisfies the predicate, {@code false} if it doesn't
     */
    boolean testPlace(Place place);

    @Override
    default boolean test(Place place) {
        return testPlace(place);
    }

    static PlacePredicate selfLoop() {
        return new NonSelfLoopPlacePredicate();
    }

    static PlacePredicate emptyIOTransitionSet() {
        return new NonEmptyIOTransitionSetPlacePredicate();
    }

    static PlacePredicate mostKArcs(int k) { return new MostKArcsPlacePredicate(k); }

}
