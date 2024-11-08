package org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.LPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;

public interface LPMDiscoveryInput {

    EventLog getLog();

    LPMBuildingInput getLPMBuildingInput();
}
