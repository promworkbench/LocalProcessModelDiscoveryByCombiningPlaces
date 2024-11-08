package org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.LPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;

public class StandardLPMDiscoveryInput implements LPMDiscoveryInput {

    private final EventLog log;
    private final LPMBuildingInput input;

    public StandardLPMDiscoveryInput(EventLog log, LPMBuildingInput input) {
        this.log = log;
        this.input = input;
    }
    @Override
    public EventLog getLog() {
        return this.log;
    }

    @Override
    public LPMBuildingInput getLPMBuildingInput() {
        return this.input;
    }
}
