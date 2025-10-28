package org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters;

import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithm;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;

public interface PlaceDiscoveryParameters {

    PlaceDiscoveryAlgorithm<? extends PlaceDiscoveryParameters, ?> getAlgorithm(PlaceDiscoveryAlgorithmFactory factory);
}
