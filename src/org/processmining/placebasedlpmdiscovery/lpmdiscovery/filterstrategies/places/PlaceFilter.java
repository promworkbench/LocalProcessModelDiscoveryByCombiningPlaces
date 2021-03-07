package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.places;

import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.Set;

/**
 * Interface that needs to be implemented by all Place filters
 */
public interface PlaceFilter {

    Set<Place> filter(Set<Place> places);
}
