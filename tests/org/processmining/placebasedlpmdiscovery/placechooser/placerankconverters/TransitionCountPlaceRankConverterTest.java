package org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters;

import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.model.Place;

public class TransitionCountPlaceRankConverterTest {

    @Test
    public void givenOneInputAndOneOutputTransition_whenConvert_thenReturnsTwo() {
        Place place = Place.from("a | b");
        TransitionCountPlaceRankConverter converter = new TransitionCountPlaceRankConverter();

        assert converter.convert(place) == 2.0;
    }

    @Test
    public void givenMultipleInputAndOutputTransitions_whenConvert_thenReturnsTotalCount() {
        Place place = Place.from("a, b | c, d");
        TransitionCountPlaceRankConverter converter = new TransitionCountPlaceRankConverter();

        assert converter.convert(place) == 4.0;
    }

    @Test
    public void givenOnlyInputTransitions_whenConvert_thenReturnsInputCount() {
        Place place = Place.from("a, b | ");
        TransitionCountPlaceRankConverter converter = new TransitionCountPlaceRankConverter();

        assert converter.convert(place) == 2.0;
    }

    @Test
    public void givenOnlyOutputTransitions_whenConvert_thenReturnsOutputCount() {
        Place place = Place.from(" | a, b");
        TransitionCountPlaceRankConverter converter = new TransitionCountPlaceRankConverter();

        assert converter.convert(place) == 2.0;
    }

    @Test
    public void givenEmptyPlace_whenConvert_thenReturnsZero() {
        Place place = Place.from(" | ");
        TransitionCountPlaceRankConverter converter = new TransitionCountPlaceRankConverter();

        assert converter.convert(place) == 0.0;
    }
}
