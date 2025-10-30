package src.org.processmining.placebasedlpmdiscovery.placechooser.placepredicates;

import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.placechooser.placepredicates.AndPlacePredicate;

public class AndPlacePredicateTest {

    @Test
    public void givenAllTruePredicates_whenTestPlace_thenReturnsTrue() {
        // given
        AndPlacePredicate andPredicate = new AndPlacePredicate((p) -> true, (p) -> true, (p) -> true);
        Place mockPlace = Place.from("a | b");

        // when
        boolean actual = andPredicate.testPlace(mockPlace);

        // then
        assert actual;
    }

    @Test
    public void givenOneFalsePredicate_whenTestPlace_thenReturnsFalse() {
        // given
        AndPlacePredicate andPredicate = new AndPlacePredicate((p) -> true, (p) -> false, (p) -> true);
        Place mockPlace = Place.from("a | b");

        // when
        boolean actual = andPredicate.testPlace(mockPlace);

        // then
        assert !actual;
    }

    @Test
    public void givenAllFalsePredicates_whenTestPlace_thenReturnsFalse() {
        // given
        AndPlacePredicate andPredicate = new AndPlacePredicate((p) -> false, (p) -> false, (p) -> false);
        Place mockPlace = Place.from("a | b");

        // when
        boolean actual = andPredicate.testPlace(mockPlace);

        // then
        assert !actual;
    }
}
