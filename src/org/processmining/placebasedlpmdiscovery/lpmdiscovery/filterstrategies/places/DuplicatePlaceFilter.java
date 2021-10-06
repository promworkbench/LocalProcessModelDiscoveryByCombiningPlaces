package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.places;

import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.HashSet;
import java.util.Set;

public class DuplicatePlaceFilter implements PlaceFilter {

    @Override
    public Set<Place> filter(Set<Place> places) {
        Set<Place> resPlaces = new HashSet<>(); // initialize empty resulting set

        for (Place queryPlace : places) { // for every place in the set
            boolean isDuplicate = false; // assume that is not duplicate
            for (Place place : places) { // and iterate through all places
                if (queryPlace.equals(place)) // if we are checking the exact same place
                    continue; // ignore it
                // otherwise check if the places have the same input and output transition sets
                if (queryPlace.getOutputTransitions().equals(place.getOutputTransitions())
                        && queryPlace.getInputTransitions().equals(place.getInputTransitions())) { // if true
                    // check whether the other place is already in the resulting set
                    if (resPlaces.contains(place)) { // if it is contained
                        isDuplicate = true; // mark the query place as duplicate
                        break; // and stop the iteration
                    }
                }
            }
            if (!isDuplicate) // if the query place is not duplicate to any other
                resPlaces.add(queryPlace); // add it in the resulting set
        }
        return resPlaces; // return a set that contains no duplicate places
    }
}
