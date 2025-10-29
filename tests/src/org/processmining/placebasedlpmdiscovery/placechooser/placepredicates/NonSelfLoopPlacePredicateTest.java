package src.org.processmining.placebasedlpmdiscovery.placechooser.placepredicates;

import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.placechooser.placepredicates.NonSelfLoopPlacePredicate;

public class NonSelfLoopPlacePredicateTest {

    @Test
    public void givenSelfLoopPlace_whenTestPlace_thenReturnsFalse() {
        // given
        Place place = Place.from("a | a");
        NonSelfLoopPlacePredicate predicate = new NonSelfLoopPlacePredicate();

        // when
        boolean actual = predicate.testPlace(place);

        // then
        assert !actual;
    }

    @Test
    public void givenNonSelfLoopPlace_whenTestPlace_thenReturnsTrue() {
        // given
        Place place = Place.from("a | b");
        NonSelfLoopPlacePredicate predicate = new NonSelfLoopPlacePredicate();

        // when
        boolean actual = predicate.testPlace(place);

        // then
        assert actual;
    }

    @Test
    public void givenMixedSelfLoopAndNonSelfLoopPlace_whenTestPlace_thenReturnsTrue() {
        // given
        Place place = Place.from("a, b | a, c");
        NonSelfLoopPlacePredicate predicate = new NonSelfLoopPlacePredicate();

        // when
        boolean actual = predicate.testPlace(place);

        // then
        assert actual;
    }

    @Test
    public void givenEmptyInputTransitions_whenTestPlace_thenReturnsFalse() {
        // given
        Place place = Place.from(" | a");
        NonSelfLoopPlacePredicate predicate = new NonSelfLoopPlacePredicate();

        // when
        boolean actual = predicate.testPlace(place);

        // then
        assert !actual;
    }

    @Test
    public void givenEmptyOutputTransitions_whenTestPlace_thenReturnsFalse() {
        // given
        Place place = Place.from("a | ");
        NonSelfLoopPlacePredicate predicate = new NonSelfLoopPlacePredicate();

        // when
        boolean actual = predicate.testPlace(place);

        // then
        assert !actual;
    }

    @Test
    public void givenEmptyPlace_whenTestPlace_thenReturnsFalse() {
        // given
        Place place = Place.from(" | ");
        NonSelfLoopPlacePredicate predicate = new NonSelfLoopPlacePredicate();

        // when
        boolean actual = predicate.testPlace(place);

        // then
        assert !actual;
    }

    @Test
    public void givenPlaceWithMultipleSelfLoops_whenTestPlace_thenReturnsFalse() {
        // given
        Place place = Place.from("a, b | a, b");
        NonSelfLoopPlacePredicate predicate = new NonSelfLoopPlacePredicate();

        // when
        boolean actual = predicate.testPlace(place);

        // then
        assert !actual;
    }

    @Test
    public void givenPlaceWithOnlyInputSelfLoops_whenTestPlace_thenReturnsFalse() {
        // given
        Place place = Place.from("a | a, d");
        NonSelfLoopPlacePredicate predicate = new NonSelfLoopPlacePredicate();

        // when
        boolean actual = predicate.testPlace(place);

        // then
        assert !actual;
    }

    @Test
    public void givenPlaceWithOnlyOutputSelfLoops_whenTestPlace_thenReturnsFalse() {
        // given
        Place place = Place.from("c, a | a");
        NonSelfLoopPlacePredicate predicate = new NonSelfLoopPlacePredicate();

        // when
        boolean actual = predicate.testPlace(place);

        // then
        assert !actual;
    }
}
