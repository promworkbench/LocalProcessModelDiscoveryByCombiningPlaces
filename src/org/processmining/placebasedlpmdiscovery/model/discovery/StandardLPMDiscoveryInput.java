package org.processmining.placebasedlpmdiscovery.model.discovery;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;

public class StandardLPMDiscoveryInput implements LPMDiscoveryInput {

    private final EventLog log;

    public StandardLPMDiscoveryInput(XLog log) {
        this.log = new XLogWrapper(log);
    }

    @Override
    public EventLog getLog() {
        return this.log;
    }
}
