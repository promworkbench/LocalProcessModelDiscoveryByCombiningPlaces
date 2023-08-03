package org.processmining.placebasedlpmdiscovery.prom.placediscovery.converters.place;

import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.Set;

public interface PlaceConverter<R> {

    /**
     * @param result: the structure in which the places are sent
     * @return set of places
     */
    Set<Place> convert(R result);
}
