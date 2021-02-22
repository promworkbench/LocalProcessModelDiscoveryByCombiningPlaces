package org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.filterstrategies.places;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.Place;

import java.util.Set;

/**
 * Interface that needs to be implemented by all Place filters
 */
public interface PlaceFilter {

    Set<Place> filter(Set<Place> places);
}
