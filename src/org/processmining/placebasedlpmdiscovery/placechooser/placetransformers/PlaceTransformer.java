package org.processmining.placebasedlpmdiscovery.placechooser.placetransformers;

import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.function.Function;

public interface PlaceTransformer extends Function<Place, Place> {

    Place adapt(Place place);

    @Override
    default Place apply(Place place) {
        return adapt(place);
    }
}
