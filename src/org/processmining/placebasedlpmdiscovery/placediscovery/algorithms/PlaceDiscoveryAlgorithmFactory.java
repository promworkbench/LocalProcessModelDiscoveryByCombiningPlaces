package org.processmining.placebasedlpmdiscovery.placediscovery.algorithms;

import org.processmining.placebasedlpmdiscovery.placediscovery.converters.place.PetriNetPlaceConverter;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.EstMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.HeuristicMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.InductiveMinerPlaceDiscoveryParameters;

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
