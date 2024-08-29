package org.processmining.placebasedlpmdiscovery.view.models;

import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;

public interface MultipleLPMDiscoveryResultsViewModel extends ViewModel {

    LPMDiscoveryResult getLPMDiscoveryResult(String name);

    Collection<LocalProcessModel> intersection(String... setNames);

    Collection<LocalProcessModel> union(String... setNames);

    Collection<LocalProcessModel> diff(String... setNames);
}
