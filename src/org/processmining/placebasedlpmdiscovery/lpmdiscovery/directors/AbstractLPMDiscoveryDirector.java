package org.processmining.placebasedlpmdiscovery.lpmdiscovery.directors;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder.LPMDiscoveryAlgBuilder;

public abstract class AbstractLPMDiscoveryDirector implements LPMDiscoveryDirector {

    protected final LPMDiscoveryAlgBuilder builder;

    public AbstractLPMDiscoveryDirector(LPMDiscoveryAlgBuilder builder) {
        this.builder = builder;
    }

}
