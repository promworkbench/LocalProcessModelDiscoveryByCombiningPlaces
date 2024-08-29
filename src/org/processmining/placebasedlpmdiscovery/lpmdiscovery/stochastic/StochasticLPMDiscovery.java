package org.processmining.placebasedlpmdiscovery.lpmdiscovery.stochastic;

import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;

public interface StochasticLPMDiscovery {

    LPMDiscoveryResult run(LPMDiscoveryInput input);
}
