package org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters;

import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LEFRMatrix;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;

public class TotalPassageCoveragePlaceRankConverterTest {

    private static LEFRMatrix lefrReturning(int countForAll) {
        return new LEFRMatrix(null, 0) {
            @Override
            public void calculateMatrix() {
            }

            @Override
            public int get(String rowName, String colName) {
                return countForAll;
            }
        };
    }

    @Test
    public void givenPlaceWithSinglePassage_whenConvert_thenReturnsPassageCount() {
        Place place = Place.from("a | b");
        TotalPassageCoveragePlaceRankConverter converter =
                new TotalPassageCoveragePlaceRankConverter(lefrReturning(5));

        assert converter.convert(place) == 5.0;
    }

    @Test
    public void givenPlaceWithMultiplePassages_whenConvert_thenReturnsSumOfAllPassageCounts() {
        Place place = Place.from("a, b | c, d");
        LEFRMatrix lefr = lefrReturning(2);
        TotalPassageCoveragePlaceRankConverter converter = new TotalPassageCoveragePlaceRankConverter(lefr);

        assert converter.convert(place) == 8.0;
    }

    @Test
    public void givenPlaceWithInvisibleInputTransition_whenConvert_thenInvisibleTransitionIsIgnored() {
        Place place = new Place();
        place.addInputTransition(new Transition("a", true));
        place.addOutputTransition(new Transition("b", false));
        TotalPassageCoveragePlaceRankConverter converter =
                new TotalPassageCoveragePlaceRankConverter(lefrReturning(5));

        assert converter.convert(place) == 0.0;
    }

    @Test
    public void givenPlaceWithInvisibleOutputTransition_whenConvert_thenInvisibleTransitionIsIgnored() {
        Place place = new Place();
        place.addInputTransition(new Transition("a", false));
        place.addOutputTransition(new Transition("b", true));
        TotalPassageCoveragePlaceRankConverter converter =
                new TotalPassageCoveragePlaceRankConverter(lefrReturning(5));

        assert converter.convert(place) == 0.0;
    }

    @Test
    public void givenPlaceWithTransitionsNotInLefr_whenConvert_thenReturnsZero() {
        Place place = Place.from("x | y");
        LEFRMatrix lefr = new LEFRMatrix(null, 0) {
            @Override
            public void calculateMatrix() {
            }

            @Override
            public int get(String rowName, String colName) {
                if (rowName.equals("a") && colName.equals("c")) return 3;
                if (rowName.equals("a") && colName.equals("d")) return 2;
                if (rowName.equals("b") && colName.equals("c")) return 1;
                if (rowName.equals("b") && colName.equals("d")) return 4;
                return 0;
            }
        };
        TotalPassageCoveragePlaceRankConverter converter = new TotalPassageCoveragePlaceRankConverter(lefr);

        assert converter.convert(place) == 0.0;
    }

    @Test
    public void givenPlaceWithPassagesNotInLefr_whenConvert_thenReturnsZero() {
        Place place = Place.from("x, a, b | y, d");
        LEFRMatrix lefr = new LEFRMatrix(null, 0) {
            @Override
            public void calculateMatrix() {
            }

            @Override
            public int get(String rowName, String colName) {
                if (rowName.equals("a") && colName.equals("c")) return 3;
                if (rowName.equals("a") && colName.equals("d")) return 2;
                if (rowName.equals("b") && colName.equals("c")) return 1;
                if (rowName.equals("b") && colName.equals("d")) return 4;
                return 0;
            }
        };
        TotalPassageCoveragePlaceRankConverter converter = new TotalPassageCoveragePlaceRankConverter(lefr);

        assert converter.convert(place) == 6.0;
    }
}
