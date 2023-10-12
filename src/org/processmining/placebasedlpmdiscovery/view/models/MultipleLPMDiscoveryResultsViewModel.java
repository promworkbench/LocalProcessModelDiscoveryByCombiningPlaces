package org.processmining.placebasedlpmdiscovery.view.models;

import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;

public interface MultipleLPMDiscoveryResultsViewModel extends ViewModel {

    LPMDiscoveryResult getLPMDiscoveryResult(String name);

}
