package org.processmining.placebasedlpmdiscovery.placechooser.placetransformers;

import com.google.common.collect.Sets;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.util.HashSet;
import java.util.Set;

public class IncludedActivitiesPlaceTransformer implements PlaceTransformer {

    private final Set<String> activities;

    public IncludedActivitiesPlaceTransformer(Set<String> activities) {
        this.activities = activities;
    }

    @Override
    public Place adapt(Place place) {
        Set<Transition> transitions = new HashSet<>(Sets.union(place.getInputTransitions(), place.getOutputTransitions()));
        transitions.forEach(t -> {
            if (!t.isInvisible() && !activities.contains(t.getLabel()))
                place.removeTransitions(t.getLabel());
        });
        return place;
    }
}
