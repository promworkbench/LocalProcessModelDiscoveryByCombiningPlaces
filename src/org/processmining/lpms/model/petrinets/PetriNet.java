package org.processmining.lpms.model.petrinets;

import org.processmining.lpms.model.LPM;
import org.processmining.lpms.model.petrinets.nodes.Arc;
import org.processmining.lpms.model.petrinets.nodes.Node;
import org.processmining.lpms.model.petrinets.nodes.Place;
import org.processmining.lpms.model.petrinets.nodes.Transition;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

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

    boolean addInitialPlaceWithActivity(Activity activity);
}
