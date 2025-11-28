package org.processmining.lpms.model.petrinets;

import org.processmining.lpms.model.LPM;
import org.processmining.lpms.model.petrinets.nodes.Arc;
import org.processmining.lpms.model.petrinets.nodes.Node;
import org.processmining.lpms.model.petrinets.nodes.Place;
import org.processmining.lpms.model.petrinets.nodes.Transition;

import java.util.Collection;

/**
 * Represents an LPM using a Petri net.
 *
 * @author Viki Peeva
 */
public interface PetriNet extends LPM {

    Collection<Place> getPlaces();

    Collection<Transition> getTransitions();

    Collection<Arc> getArcsFrom(Node node);

    Collection<Arc> getArcsTo(Node node);

    Collection<Node> getPreset(Node node);

    Collection<Node> getPostset(Node node);

    Place addPlace();

    Place addPlace(Collection<Transition> inputTransitions, Collection<Transition> outputTransitions);

    Transition addTransition(String label);

    Transition addInvisibleTransition();

    Transition addTransition(Collection<Place> inputPlaces, Collection<Place> outputPlaces, String label);

    Transition addInvisibleTransition(Collection<Place> inputPlaces, Collection<Place> outputPlaces);

    NodePosition getNodePosition(Node node);
}
