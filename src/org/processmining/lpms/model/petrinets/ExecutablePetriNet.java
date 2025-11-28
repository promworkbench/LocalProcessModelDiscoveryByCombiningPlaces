package org.processmining.lpms.model.petrinets;

import org.processmining.lpms.model.ExecutableLPM;
import org.processmining.lpms.model.petrinets.nodes.Transition;
import org.processmining.lpms.model.traits.DeepCopyable;

import java.util.Collection;

public interface ExecutablePetriNet extends PetriNet, ExecutableLPM, DeepCopyable<ExecutablePetriNet> {

    boolean fire(Transition transition);

    Collection<Transition> getEnabledForLabel(String label);

    boolean fireOnPosition(NodePosition nodePosition);
}
