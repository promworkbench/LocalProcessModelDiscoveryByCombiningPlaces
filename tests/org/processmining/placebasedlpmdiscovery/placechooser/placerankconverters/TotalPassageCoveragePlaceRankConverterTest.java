package org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters;

import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import org.processmining.mockobjects.MockLEFRMatrix;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LEFRMatrix;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.util.HashMap;
import java.util.Map;

public class TotalPassageCoveragePlaceRankConverterTest {

    private static Map<Pair<String, String>, Integer> knownPairs() {
        Map<Pair<String, String>, Integer> values = new HashMap<>();
        values.put(new Pair<>("a", "c"), 3);
        values.put(new Pair<>("a", "d"), 2);
        values.put(new Pair<>("b", "c"), 1);
        values.put(new Pair<>("b", "d"), 4);
        return values;
    }

    @Test
    public void givenPlaceWithSinglePassage_whenConvert_thenReturnsPassageCount() {
        Place place = Place.from("a | b");
        TotalPassageCoveragePlaceRankConverter converter =
                new TotalPassageCoveragePlaceRankConverter(MockLEFRMatrix.returning(5));

        assert converter.convert(place) == 5.0;
    }

    @Test
    public void givenPlaceWithMultiplePassages_whenConvert_thenReturnsSumOfAllPassageCounts() {
        Place place = Place.from("a, b | c, d");
        TotalPassageCoveragePlaceRankConverter converter =
                new TotalPassageCoveragePlaceRankConverter(MockLEFRMatrix.returning(2));

        assert converter.convert(place) == 8.0;
    }

    @Test
    public void givenPlaceWithInvisibleInputTransition_whenConvert_thenInvisibleTransitionIsIgnored() {
        Place place = new Place();
        place.addInputTransition(new Transition("a", true));
        place.addOutputTransition(new Transition("b", false));
        TotalPassageCoveragePlaceRankConverter converter =
                new TotalPassageCoveragePlaceRankConverter(MockLEFRMatrix.returning(5));

        assert converter.convert(place) == 0.0;
    }

    @Test
    public void givenPlaceWithInvisibleOutputTransition_whenConvert_thenInvisibleTransitionIsIgnored() {
        Place place = new Place();
        place.addInputTransition(new Transition("a", false));
        place.addOutputTransition(new Transition("b", true));
        TotalPassageCoveragePlaceRankConverter converter =
                new TotalPassageCoveragePlaceRankConverter(MockLEFRMatrix.returning(5));

        assert converter.convert(place) == 0.0;
    }

    @Test
    public void givenPlaceWithTransitionsNotInLefr_whenConvert_thenReturnsZero() {
        Place place = Place.from("x | y");
        LEFRMatrix lefr = MockLEFRMatrix.returning(knownPairs());
        TotalPassageCoveragePlaceRankConverter converter = new TotalPassageCoveragePlaceRankConverter(lefr);

        assert converter.convert(place) == 0.0;
    }

    @Test
    public void givenPlaceWithPassagesNotInLefr_whenConvert_thenReturnsZero() {
        Place place = Place.from("x, a, b | y, d");
        LEFRMatrix lefr = MockLEFRMatrix.returning(knownPairs());
        TotalPassageCoveragePlaceRankConverter converter = new TotalPassageCoveragePlaceRankConverter(lefr);

        assert converter.convert(place) == 6.0;
    }
}
