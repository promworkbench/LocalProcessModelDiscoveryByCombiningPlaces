package org.processmining.placebasedlpmdiscovery.lpmdiscovery.directors;

import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryBuilder;

public abstract class AbstractLPMDiscoveryDirector implements LPMDiscoveryDirector {

    protected final LPMDiscoveryBuilder builder;

    public AbstractLPMDiscoveryDirector(LPMDiscoveryBuilder builder) {
        this.builder = builder;
    }

}
