package org.processmining.placebasedlpmdiscovery.placediscovery.converters.place;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;
import org.processmining.placebasedlpmdiscovery.utils.TransitionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PetriNetPlaceConverter extends AbstractPlaceConverter<AcceptingPetriNet> {
    @Override
    public Set<Place> convert(AcceptingPetriNet result) {
        Set<Place> discoveredPlaces = new HashSet<>();
        // create transitions
        Set<Transition> transitions = new HashSet<>();
        for (org.processmining.models.graphbased.directed.petrinet.elements.Transition t : result.getNet().getTransitions()) {
            String label = t.getLabel();
            if (t.isInvisible())
                label = t.getLabel() + "-" + t.getId().toString();
            transitions.add(new Transition(label, t.isInvisible()));
        }
        Map<String, Transition> transitionMap = TransitionUtils.mapLabelsIntoTransitions(transitions);


        Collection<org.processmining.models.graphbased.directed.petrinet.elements.Place> pnPlaces = result.getNet().getPlaces();
        for (org.processmining.models.graphbased.directed.petrinet.elements.Place pnPlace : pnPlaces) { // for every place
            Place place = new Place(); // create a place

            // add all input transitions
            result.getNet().getInEdges(pnPlace)
                    .stream()
                    .map(edge -> (org.processmining.models.graphbased.directed.petrinet.elements.Transition) edge.getSource())
                    .forEach(transition -> {
                        if (transition.isInvisible())
                            place.addInputTransition(transitionMap.get(transition.getLabel() + "-" + transition.getId().toString()));
                        else
                            place.addInputTransition(transitionMap.get(transition.getLabel()));
                    });

            // add all output transitions
            result.getNet().getOutEdges(pnPlace)
                    .stream()
                    .map(edge -> (org.processmining.models.graphbased.directed.petrinet.elements.Transition) edge.getTarget())
                    .forEach(transition -> {
                        if (transition.isInvisible())
                            place.addOutputTransition(transitionMap.get(transition.getLabel() + "-" + transition.getId().toString()));
                        else
                            place.addOutputTransition(transitionMap.get(transition.getLabel()));
                    });
            discoveredPlaces.add(place);
        }
//        PlaceUtils.print(discoveredPlaces);
        return discoveredPlaces;
    }
}
