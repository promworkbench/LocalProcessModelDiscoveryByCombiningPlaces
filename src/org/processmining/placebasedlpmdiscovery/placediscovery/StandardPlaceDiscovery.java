package org.processmining.placebasedlpmdiscovery.placediscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.placediscovery.algorithms.PlaceDiscoveryAlgorithm;
import org.processmining.placebasedlpmdiscovery.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.PlaceDiscoveryParameters;

public class StandardPlaceDiscovery implements PlaceDiscovery {

    private final XLog log;

    private final PlaceDiscoveryParameters parameters;

    public StandardPlaceDiscovery(XLog log, PlaceDiscoveryParameters parameters){
        this.log = log;
        this.parameters = parameters;
    }
    public PlaceDiscoveryResult getPlaces() {
        PlaceDiscoveryAlgorithmFactory factory = new PlaceDiscoveryAlgorithmFactory(); // TODO: Why not using the factory directly?
        PlaceDiscoveryAlgorithm<? extends PlaceDiscoveryParameters, ?> algorithm = parameters.getAlgorithm(factory);
        return algorithm.getPlaces(log);
    }
}
