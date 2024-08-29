package org.processmining.placebasedlpmdiscovery.model.discovery;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryInput;

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
