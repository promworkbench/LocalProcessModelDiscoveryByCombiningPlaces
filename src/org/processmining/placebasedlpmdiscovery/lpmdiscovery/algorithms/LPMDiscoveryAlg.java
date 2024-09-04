package org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms;

import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;

public interface LPMDiscoveryAlg<I extends LPMDiscoveryInput> {

    LPMDiscoveryResult run(I input);
}
