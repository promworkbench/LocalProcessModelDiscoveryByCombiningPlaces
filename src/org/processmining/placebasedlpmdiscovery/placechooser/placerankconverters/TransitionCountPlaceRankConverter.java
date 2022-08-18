package org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters;

import org.processmining.placebasedlpmdiscovery.model.Place;

public class TransitionCountPlaceRankConverter implements PlaceRankConverter {

    /**
     * Extracts the number of transitions a place contains
     * @param place the place whose property we want to extract
     * @return the number of transitions connected with the place
     */
    @Override
    public Double convert(Place place) {
        return (double) (place.getInputTransitions().size() + place.getOutputTransitions().size());
    }
}
