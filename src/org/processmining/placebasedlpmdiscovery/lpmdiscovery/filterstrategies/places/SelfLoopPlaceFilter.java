package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.places;

import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.util.HashSet;
import java.util.Set;

public class SelfLoopPlaceFilter implements PlaceFilter {
    @Override
    public Set<Place> filter(Set<Place> places) {
        Set<Place> resPlaces = new HashSet<>(); // initialize empty resulting set

        for (Place queryPlace : places) { // for every place in the set
            boolean allInputSelfLoops = true;
            for (Transition t : queryPlace.getInputTransitions())
                if (!queryPlace.isSelfLoop(t.getLabel()))
                    allInputSelfLoops = false;

            if (allInputSelfLoops) // && queryPlace.getNumTokens() < 1)
                continue;

            boolean allOutputSelfLoops = true;
            for (Transition t : queryPlace.getOutputTransitions())
                if (!queryPlace.isSelfLoop(t.getLabel()))
                    allOutputSelfLoops = false;

            if (allOutputSelfLoops) // && !queryPlace.isFinal())
                continue;

            resPlaces.add(queryPlace);
        }
        return resPlaces; // return a set that contains no self loops
    }
}
