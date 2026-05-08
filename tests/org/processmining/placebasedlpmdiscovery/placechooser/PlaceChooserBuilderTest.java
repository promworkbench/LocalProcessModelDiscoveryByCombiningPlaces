package org.processmining.placebasedlpmdiscovery.placechooser;

import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PlaceChooserBuilderTest {

    @Test(expected = IllegalStateException.class)
    public void givenNoRankConverter_whenBuild_thenThrows() {
        new PlaceChooserBuilder().build();
    }

    @Test
    public void givenFilter_whenChoose_thenExcludesNonMatchingPlaces() {
        // given
        Place included = Place.from("a | b");
        Place excluded = Place.from("c | d");
        Set<Place> places = new HashSet<>(Arrays.asList(included, excluded));

        PlaceChooser chooser = new PlaceChooserBuilder()
                .withFilter(p -> p == included)
                .rankBy(p -> 1.0)
                .build();

        // when
        Set<Place> result = chooser.choose(places, 10);

        // then
        assert result.size() == 1;
        assert result.contains(included);
    }

    @Test
    public void givenCountLimit_whenChoose_thenRespectsLimit() {
        // given
        Set<Place> places = new HashSet<>(Arrays.asList(
                Place.from("a | b"),
                Place.from("a | c"),
                Place.from("a | d")
        ));

        PlaceChooser chooser = new PlaceChooserBuilder()
                .rankBy(p -> 1.0)
                .build();

        // when
        Set<Place> result = chooser.choose(places, 2);

        // then
        assert result.size() == 2;
    }

    @Test
    public void givenTransformer_whenChoose_thenTransformerAppliedBeforeFilter() {
        // given: transformer maps all inputs to a sentinel; filter only accepts the sentinel
        Place sentinel = Place.from("a | b");
        Set<Place> places = new HashSet<>(Arrays.asList(
                Place.from("c | d"),
                Place.from("e | f")
        ));

        PlaceChooser chooser = new PlaceChooserBuilder()
                .withTransformer(p -> sentinel)
                .withFilter(p -> p == sentinel)
                .rankBy(p -> 1.0)
                .build();

        // when
        Set<Place> result = chooser.choose(places, 10);

        // then: transformer ran (otherwise both places would be rejected by the filter);
        // set deduplication collapses both sentinel references to one
        assert result.size() == 1;
        assert result.contains(sentinel);
    }

    @Test
    public void givenMultipleFilters_whenChoose_thenAllFiltersMustPass() {
        // given
        Place p1 = Place.from("a | b");
        Place p2 = Place.from("a | c");
        Set<Place> places = new HashSet<>(Arrays.asList(p1, p2));

        PlaceChooser chooser = new PlaceChooserBuilder()
                .withFilter(p -> p == p1 || p == p2)   // both pass
                .withFilter(p -> p == p1)               // only p1 passes
                .rankBy(p -> 1.0)
                .build();

        // when
        Set<Place> result = chooser.choose(places, 10);

        // then
        assert result.size() == 1;
        assert result.contains(p1);
    }
}
