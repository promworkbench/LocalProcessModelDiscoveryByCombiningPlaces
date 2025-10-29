package src.org.processmining.placebasedlpmdiscovery.placechooser.placepredicates;

import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.placechooser.placepredicates.OrPredicate;

public class OrPlacePredicateTest {

    @Test
    public void givenAllFalsePredicates_whenTestPlace_thenReturnsFalse() {
        // given
        OrPredicate orPredicate = new OrPredicate((p) -> false, (p) -> false, (p) -> false);
        Place mockPlace = Place.from("a | b");

        // when
        boolean actual = orPredicate.testPlace(mockPlace);

        // then
        assert !actual;
    }

    @Test
    public void givenOneTruePredicate_whenTestPlace_thenReturnsTrue() {
        // given
        OrPredicate orPredicate = new OrPredicate((p) -> false, (p) -> true, (p) -> false);
        Place mockPlace = Place.from("a | b");

        // when
        boolean actual = orPredicate.testPlace(mockPlace);

        // then
        assert actual;
    }

    @Test
    public void givenAllTruePredicates_whenTestPlace_thenReturnsTrue() {
        // given
        OrPredicate orPredicate = new OrPredicate((p) -> true, (p) -> true, (p) -> true);
        Place mockPlace = Place.from("a | b");

        // when
        boolean actual = orPredicate.testPlace(mockPlace);

        // then
        assert actual;
    }
}
