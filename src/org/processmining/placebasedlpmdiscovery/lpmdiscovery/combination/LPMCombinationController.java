package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination;

import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;

import java.util.Set;

public interface LPMCombinationController {

    LPMDiscoveryResult combine(Set<Place> places, int count);
}
