package org.processmining.lpms.model.petrinets.placebased;

import org.processmining.lpms.model.petrinets.nodes.Transition;

import java.util.Collection;
import java.util.UUID;

public interface PlaceNet {

    UUID getId();

    Collection<Transition> getInputTransitions();

    Collection<Transition> getOutputTransitions();
}
