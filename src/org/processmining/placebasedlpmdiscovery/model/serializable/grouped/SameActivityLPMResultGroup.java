package org.processmining.placebasedlpmdiscovery.model.serializable.grouped;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SameActivityLPMResultGroup extends LPMResultGroup {

    private static final long serialVersionUID = 5114415142544998926L;

    private Set<String> activities;

    public SameActivityLPMResultGroup() {
        super();
        this.activities = new HashSet<>();
    }

    @Override
    public boolean shouldNotAdd(LocalProcessModel element) {
        return !this.activities.equals(
                element.getTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet()));
    }

    @Override
    public void initializeGroup(LocalProcessModel element) {
        this.activities = element.getTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet());
        this.commonId = this.activities.hashCode();
    }
}
