package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.Set;

public interface LPMCombinationController {

    void setGuard(CombinationGuard guard);

    LPMDiscoveryResult combine(Set<Place> places, int count);
}
