package org.processmining.lpms.model.petrinets;

import org.junit.Test;
import org.processmining.lpms.model.petrinets.nodes.Place;
import org.processmining.lpms.model.petrinets.nodes.Transition;

import java.util.Collections;

public interface PetriNetTest {

    PetriNet getPetriNetInstance();

    @Test
    default void givenEmptyPetriNet_whenAddingPlace_thenPlaceIsAdded() {
        PetriNet petriNet = getPetriNetInstance();

        Place place = petriNet.addPlace();

        assert petriNet.getPlaces().size() == 1 : "Petri net did not contain exactly one place.";
        assert petriNet.getTransitions().isEmpty() : "Petri net should not contain any transitions.";
        assert petriNet.getPlaces().contains(place) : "Place was not added to the Petri net.";
    }

    @Test
    default void givenPetriNet_whenAddingPlaceWithExistingTransitions_thenPlaceIsAddedWithConnections() {
        PetriNet petriNet = getPetriNetInstance();

        // Add some transitions first
        Transition t1 = petriNet.addTransition("T1");
        Transition t2 = petriNet.addTransition("T2");

        // Now add a place connected to these transitions
        Place place = petriNet.addPlace(Collections.singleton(t1), Collections.singleton(t2));

        assert petriNet.getPlaces().size() == 1 : "Petri net did not contain exactly one place.";
        assert petriNet.getTransitions().size() == 2 : "Petri net should contain two transitions.";
        assert petriNet.getPlaces().contains(place) : "Place was not added to the Petri net.";
        assert petriNet.getPreset(place).contains(t1) : "Input transition T1 is not connected to the place.";
        assert petriNet.getPostset(place).contains(t2) : "Output transition T2 is not connected to the place.";
    }

    @Test
    default void givenEmptyPetriNet_whenAddingPlaceWithNonExistingTransitions_thenExceptionIsThrown() {
        PetriNet petriNet = getPetriNetInstance();

        try {
            petriNet.addPlace(Collections.emptyList(), Collections.emptyList());
            assert false : "Expected exception was not thrown when adding place with non-existing transitions.";
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}
