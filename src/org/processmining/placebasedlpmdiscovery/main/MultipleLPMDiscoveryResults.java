package org.processmining.placebasedlpmdiscovery.main;

import java.util.Map;

public interface MultipleLPMDiscoveryResults extends LPMDiscoveryResult {

    Map<String, LPMDiscoveryResult> getResults();

    LPMDiscoveryResult getResult(String name);
}
