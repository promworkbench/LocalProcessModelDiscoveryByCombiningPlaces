package org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters;

import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithm;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;

public abstract class PlaceDiscoveryParameters {

    public abstract PlaceDiscoveryAlgorithm<? extends PlaceDiscoveryParameters, ?> getAlgorithm(PlaceDiscoveryAlgorithmFactory factory);
}
