package org.processmining.placebasedlpmdiscovery.placechooser.placefilterpredicates;

import org.processmining.placebasedlpmdiscovery.model.Place;

public class EmptyIOTransitionSetPlaceFilterPredicate implements PlaceFilterPredicate {

    /**
     * Checks for empty input or output transition set
     * @param place that we want to check for filtering
     * @return true when the input or output transition set is empty, false otherwise
     */
    @Override
    public boolean filter(Place place) {
        return place.getInputTransitions().size() < 1 || place.getOutputTransitions().size() < 1;
    }
}
