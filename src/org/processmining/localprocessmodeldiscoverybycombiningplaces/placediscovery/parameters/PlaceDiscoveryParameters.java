package org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.parameters;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.algorithms.PlaceDiscoveryAlgorithm;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;

public abstract class PlaceDiscoveryParameters {

    public abstract PlaceDiscoveryAlgorithm<? extends PlaceDiscoveryParameters, ?> getAlgorithm(PlaceDiscoveryAlgorithmFactory factory);
}
