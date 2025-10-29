package org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms;

import org.processmining.placebasedlpmdiscovery.prom.placediscovery.converters.place.PetriNetPlaceConverter;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.EstMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.HeuristicMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.InductiveMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.SPECppPlaceDiscoveryParameters;

public class PlaceDiscoveryAlgorithmFactory {

    public static PlaceDiscoveryAlgorithmFactory getInstance() {
        return new PlaceDiscoveryAlgorithmFactory();
    }

    public EstMinerPlaceDiscoveryAlgorithm createPlaceDiscoveryAlgorithm(EstMinerPlaceDiscoveryParameters parameters) {
        return new EstMinerPlaceDiscoveryAlgorithm(new PetriNetPlaceConverter(), parameters);
    }

    public InductiveMinerPlaceDiscoveryAlgorithm createPlaceDiscoveryAlgorithm(InductiveMinerPlaceDiscoveryParameters parameters) {
        return new InductiveMinerPlaceDiscoveryAlgorithm(new PetriNetPlaceConverter(), parameters);
    }

    public HeuristicMinerPlaceDiscoveryAlgorithm createPlaceDiscoveryAlgorithm(HeuristicMinerPlaceDiscoveryParameters parameters) {
        return new HeuristicMinerPlaceDiscoveryAlgorithm(new PetriNetPlaceConverter(), parameters);
    }

    public SPECppPlaceDiscoveryAlgorithm createPlaceDiscoveryAlgorithm(SPECppPlaceDiscoveryParameters parameters) {
        return new SPECppPlaceDiscoveryAlgorithm(new PetriNetPlaceConverter(), parameters);
    }
}
