package org.processmining.placebasedlpmdiscovery.main;

import org.deckfour.xes.model.XLog;

public class StandardLPMDiscoveryInput implements LPMDiscoveryInput {

    private final XLog log;

    public StandardLPMDiscoveryInput(XLog log) {
        this.log = log;
    }

    @Override
    public XLog getLog() {
        return this.log;
    }
}
