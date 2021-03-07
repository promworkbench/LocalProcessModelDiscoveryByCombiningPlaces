package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.places;

import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.HashSet;
import java.util.Set;

public class EmptyIOTransitionSetPlaceFilter implements PlaceFilter {
    @Override
    public Set<Place> filter(Set<Place> places) {
        Set<Place> resSet = new HashSet<>();
        for (Place place : places) {
            if (place.getInputTransitions().size() < 1 || place.getOutputTransitions().size() < 1)
                continue;
            resSet.add(place);
        }
        return resSet;
    }
}
