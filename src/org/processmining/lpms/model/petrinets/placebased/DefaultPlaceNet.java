package org.processmining.lpms.model.petrinets.placebased;

import org.processmining.lpms.model.petrinets.nodes.Transition;

import java.util.Collection;
import java.util.UUID;

public class DefaultPlaceNet implements PlaceNet {

    private final UUID id;
    private final Collection<Transition> inputTransitions;
    private final Collection<Transition> outputTransitions;


    public DefaultPlaceNet(Collection<Transition> inputTransitions, Collection<Transition> outputTransitions) {
        this.id = UUID.randomUUID();
        this.inputTransitions = inputTransitions;
        this.outputTransitions = outputTransitions;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public Collection<Transition> getInputTransitions() {
        return this.inputTransitions;
    }

    @Override
    public Collection<Transition> getOutputTransitions() {
        return this.outputTransitions;
    }
}
