package org.processmining.lpms.model.petrinets.placebased;

import org.processmining.lpms.model.petrinets.PBPetriNet;
import org.processmining.lpms.model.petrinets.PetriNet;
import org.processmining.lpms.model.petrinets.nodes.*;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultPBPetriNet implements PBPetriNet {

    private Set<PlaceNet> placeNets;

    public DefaultPBPetriNet(PetriNet lpm) { // QA: Is it better to have a copy constructor or a converter?
        Collection<Place> lpmPlaces = lpm.getPlaces();
        this.placeNets = new HashSet<>(lpmPlaces.size());

        for (Place place : lpmPlaces) {
            Set<Transition> outTransitions = lpm.getArcsFrom(place).stream()
                    .map(Arc::getTarget).map(Transition.class::cast).collect(Collectors.toSet());
            Set<Transition> inTransitions = lpm.getArcsTo(place).stream()
                    .map(Arc::getSource).map(Transition.class::cast).collect(Collectors.toSet());
            this.placeNets.add(new DefaultPlaceNet(inTransitions, outTransitions));
        }
    }

    @Override
    public Collection<Place> getPlaces() {
        return this.placeNets.stream()
                .map(placeNet -> new DefaultPlace(placeNet.getId()))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<Transition> getTransitions() {
        return placeNets.stream()
                .flatMap(placeNet -> Stream.concat(placeNet.getInputTransitions().stream(),
                        placeNet.getOutputTransitions().stream()))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<Arc> getArcsFrom(Node node) {
        return Collections.emptyList();
    }

    @Override
    public Collection<Arc> getArcsTo(Node node) {
        return Collections.emptyList();
    }

    @Override
    public boolean addInitialPlaceWithActivity(Activity activity) {
        if (activity == null) {
            return false;
        }
        this.placeNets.add(new DefaultPlaceNet(Collections.emptySet(),
                Collections.singleton(new DefaultTransition(activity.getName()))));
        return true;
    }
}
