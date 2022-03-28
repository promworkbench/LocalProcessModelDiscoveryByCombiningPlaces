package org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters;

import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.function.Function;

public interface PlaceRankConverter extends Function<Place, Double> {

    /**
     * Convert a place into some double number that represents some property of the place that can be used
     * for prioritizing it in building local process models
     * @param place the place whose property we want to extract
     * @return value that represents the importance of the place when a certain property is considered
     */
    Double convert(Place place);

    @Override
    default Double apply(Place place) {
        return convert(place);
    }
}
