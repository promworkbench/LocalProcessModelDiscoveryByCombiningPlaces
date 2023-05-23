package org.processmining.placebasedlpmdiscovery.placechooser.placepredicates;

import org.processmining.placebasedlpmdiscovery.model.Place;

public class MostKArcsPredicate implements PlacePredicate {

    private final int k;

    public MostKArcsPredicate(int k) {
        this.k = k;
    }

    /**
     * Checks if the number of arcs is less or equal than k
     * @param place that we want to check for filtering
     * @return true if all the number of arcs is less or equal to k, false otherwise
     */
    @Override
    public boolean testPlace(Place place) {
        return place.getInputTransitions().size() + place.getOutputTransitions().size() <= this.k;
    }
}
