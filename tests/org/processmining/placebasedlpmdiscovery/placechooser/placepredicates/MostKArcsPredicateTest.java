package org.processmining.placebasedlpmdiscovery.placechooser.placepredicates;


import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.model.Place;

public class MostKArcsPredicateTest {

    @Test
    public void givenPlaceWithLessThanKArcs_whenTestPlace_thenReturnsTrue() {
        // given
        Place place = Place.from("a | b");
        MostKArcsPlacePredicate predicate = new MostKArcsPlacePredicate(3);

        // when
        boolean actual = predicate.testPlace(place);

        // then
        assert actual;
    }

    @Test
    public void givenPlaceWithEqualToKArcs_whenTestPlace_thenReturnsTrue() {
        // given
        Place place = Place.from("a, b | c");
        MostKArcsPlacePredicate predicate = new MostKArcsPlacePredicate(3);

        // when
        boolean actual = predicate.testPlace(place);

        // then
        assert actual;
    }

    @Test
    public void givenPlaceWithMoreThanKArcs_whenTestPlace_thenReturnsFalse() {
        // given
        Place place = Place.from("a, b | c, d");
        MostKArcsPlacePredicate predicate = new MostKArcsPlacePredicate(3);

        // when
        boolean actual = predicate.testPlace(place);

        // then
        assert !actual;
    }

    @Test
    public void givenPlaceWithNoArcs_whenTestPlace_thenReturnsTrue() {
        // given
        Place place = Place.from(" | ");
        MostKArcsPlacePredicate predicate = new MostKArcsPlacePredicate(3);

        // when
        boolean actual = predicate.testPlace(place);

        // then
        assert actual;
    }

    @Test
    public void givenNegativeK_whenCreatingPredicate_thenThrowsIllegalArgumentException() {
        try {
            MostKArcsPlacePredicate predicate = new MostKArcsPlacePredicate(-1);
            assert false; // Should not reach this line
        } catch (IllegalArgumentException e) {
            assert true; // Expected exception
        }
    }
}
