package org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters;

import org.processmining.placebasedlpmdiscovery.model.Place;

/**
 * Ranks places by their total number of connected transitions (input + output).
 *
 * <p>Lower scores are preferred, so places with fewer transitions rank higher. This
 * favours simpler, more specific places over large hub-like ones when selecting
 * candidates for LPM discovery.
 */
public class TransitionCountPlaceRankConverter implements PlaceRankConverter {

    @Override
    public Double convert(Place place) {
        return (double) (place.getInputTransitions().size() + place.getOutputTransitions().size());
    }
}
