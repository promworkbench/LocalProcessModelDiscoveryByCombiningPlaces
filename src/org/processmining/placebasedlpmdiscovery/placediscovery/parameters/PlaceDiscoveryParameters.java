package org.processmining.placebasedlpmdiscovery.placediscovery.parameters;

import org.processmining.placebasedlpmdiscovery.placediscovery.algorithms.PlaceDiscoveryAlgorithm;
import org.processmining.placebasedlpmdiscovery.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;

public abstract class PlaceDiscoveryParameters {

    public abstract PlaceDiscoveryAlgorithm<? extends PlaceDiscoveryParameters, ?> getAlgorithm(PlaceDiscoveryAlgorithmFactory factory);
}
