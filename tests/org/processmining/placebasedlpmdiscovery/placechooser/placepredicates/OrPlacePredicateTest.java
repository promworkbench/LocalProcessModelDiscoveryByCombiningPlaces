package org.processmining.placebasedlpmdiscovery.placechooser.placepredicates;

import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.model.Place;

public class OrPlacePredicateTest {

    @Test
    public void givenAllFalsePredicates_whenTestPlace_thenReturnsFalse() {
        // given
        OrPlacePredicate orPredicate = new OrPlacePredicate((p) -> false, (p) -> false, (p) -> false);
        Place mockPlace = Place.from("a | b");

        // when
        boolean actual = orPredicate.testPlace(mockPlace);

        // then
        assert !actual;
    }

    @Test
    public void givenOneTruePredicate_whenTestPlace_thenReturnsTrue() {
        // given
        OrPlacePredicate orPredicate = new OrPlacePredicate((p) -> false, (p) -> true, (p) -> false);
        Place mockPlace = Place.from("a | b");

        // when
        boolean actual = orPredicate.testPlace(mockPlace);

        // then
        assert actual;
    }

    @Test
    public void givenAllTruePredicates_whenTestPlace_thenReturnsTrue() {
        // given
        OrPlacePredicate orPredicate = new OrPlacePredicate((p) -> true, (p) -> true, (p) -> true);
        Place mockPlace = Place.from("a | b");

        // when
        boolean actual = orPredicate.testPlace(mockPlace);

        // then
        assert actual;
    }
}
