package src.org.processmining.placebasedlpmdiscovery.placechooser.placepredicates;

import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.placechooser.placepredicates.NonEmptyIOTransitionSetPlacePredicate;

public class NonEmptyIOTransitionSetPlacePredicateTest {

    @Test
    public void givenEmptyIOTransitionSet_whenTestPlace_thenReturnsFalse() {
        // given
        Place place = Place.from(" | ");
        NonEmptyIOTransitionSetPlacePredicate predicate = new NonEmptyIOTransitionSetPlacePredicate();

        // when
        boolean actual = predicate.testPlace(place);

        // then
        assert !actual;
    }

    @Test
    public void givenEmptyInputTransitionSet_whenTestPlace_thenReturnsFalse() {
        // given
        Place place = Place.from(" | b");
        NonEmptyIOTransitionSetPlacePredicate predicate = new NonEmptyIOTransitionSetPlacePredicate();

        // when
        boolean actual = predicate.testPlace(place);

        // then
        assert !actual;
    }

    @Test
    public void givenEmptyOutputTransitionSet_whenTestPlace_thenReturnsFalse() {
        // given
        Place place = Place.from("a | ");
        NonEmptyIOTransitionSetPlacePredicate predicate = new NonEmptyIOTransitionSetPlacePredicate();

        // when
        boolean actual = predicate.testPlace(place);

        // then
        assert !actual;
    }

    @Test
    public void givenNonEmptyIOTransitionSet_whenTestPlace_thenReturnsTrue() {
        // given
        Place place = Place.from("a | b");
        NonEmptyIOTransitionSetPlacePredicate predicate = new NonEmptyIOTransitionSetPlacePredicate();

        // when
        boolean actual = predicate.testPlace(place);

        // then
        assert actual;
    }
}
