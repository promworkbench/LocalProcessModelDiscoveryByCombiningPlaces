package org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.LPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.LPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.parameters.LPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;

public interface LPMDiscoveryAlg {

    LPMDiscoveryResult run(LPMDiscoveryInput input, LPMDiscoveryParameters parameters);
}
