package org.processmining.placebasedlpmdiscovery.main;

import java.util.Collection;

public interface MultipleLPMDiscoveryResults extends LPMDiscoveryResult {

    Collection<LPMDiscoveryResult> getResults();
}
