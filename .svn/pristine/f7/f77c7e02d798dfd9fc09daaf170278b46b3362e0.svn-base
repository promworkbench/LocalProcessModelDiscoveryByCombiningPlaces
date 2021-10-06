package org.processmining.placebasedlpmdiscovery.model;

import java.io.Serializable;

/**
 * The Arc class is used to represent the logic for the arcs in a PetriNet.
 * <p>
 * There are two different types of arcs:
 * - input arcs: go from a transition to a place
 * - output arcs: go from a place to a transition
 */
public class Arc implements Serializable {

    private static final long serialVersionUID = 8467902054818439558L;

    private Place place;
    private Transition transition;
    private boolean input;

    public Arc(Place place, Transition transition, boolean input) {
        this.place = place;
        this.transition = transition;
        this.input = input;
    }

    public Place getPlace() {
        return place;
    }

    public Transition getTransition() {
        return transition;
    }

    public boolean isInput() {
        return input;
    }
}
