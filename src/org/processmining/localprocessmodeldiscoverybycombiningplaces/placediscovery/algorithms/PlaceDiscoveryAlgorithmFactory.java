package org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.algorithms;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.converters.place.PetriNetPlaceConverter;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.parameters.EstMinerPlaceDiscoveryParameters;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.parameters.HeuristicMinerPlaceDiscoveryParameters;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.parameters.InductiveMinerPlaceDiscoveryParameters;

public class PlaceDiscoveryAlgorithmFactory {

    public EstMinerPlaceDiscoveryAlgorithm createPlaceDiscoveryAlgorithm(EstMinerPlaceDiscoveryParameters parameters) {
        return new EstMinerPlaceDiscoveryAlgorithm(new PetriNetPlaceConverter(), parameters);
    }

    public InductiveMinerPlaceDiscoveryAlgorithm createPlaceDiscoveryAlgorithm(InductiveMinerPlaceDiscoveryParameters parameters) {
        return new InductiveMinerPlaceDiscoveryAlgorithm(new PetriNetPlaceConverter(), parameters);
    }

    public HeuristicMinerPlaceDiscoveryAlgorithm createPlaceDiscoveryAlgorithm(HeuristicMinerPlaceDiscoveryParameters parameters) {
        return new HeuristicMinerPlaceDiscoveryAlgorithm(new PetriNetPlaceConverter(), parameters);
    }
}
