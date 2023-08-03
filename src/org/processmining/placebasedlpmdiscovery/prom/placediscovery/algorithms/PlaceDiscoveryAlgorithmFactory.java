package org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms;

import org.processmining.placebasedlpmdiscovery.prom.placediscovery.converters.place.PetriNetPlaceConverter;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.EstMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.HeuristicMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.InductiveMinerPlaceDiscoveryParameters;

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
