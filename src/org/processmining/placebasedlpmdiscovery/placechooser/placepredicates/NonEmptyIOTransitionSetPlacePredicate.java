package org.processmining.placebasedlpmdiscovery.placechooser.placepredicates;

import org.processmining.placebasedlpmdiscovery.model.Place;

public class NonEmptyIOTransitionSetPlacePredicate implements PlacePredicate {

    /**
     * Checks for empty input or output transition set
     * @param place that we want to check for filtering
     * @return false when the input or output transition set is empty, true otherwise
     */
    @Override
    public boolean testPlace(Place place) {
        return place.getInputTransitions().size() > 0 && place.getOutputTransitions().size() > 0;
    }
}
